package com.express.view.issue {

import com.express.ApplicationFacade;
import com.express.model.domain.User;
import com.express.model.request.AddImpedimentRequest;
import com.express.view.backlogItem.BacklogItemProxy;
import com.express.view.form.FormMediator;
import com.express.view.form.FormUtility;

import flash.events.MouseEvent;

import mx.events.CloseEvent;

import org.puremvc.as3.interfaces.INotification;

public class IssueMediator extends FormMediator {
   public static const NAME:String = "IssueMediator";
   public static const CREATE:String = "IssueMediator.CREATE";
   public static const EDIT:String = "IssueMediator.EDIT";

   private var _proxy:BacklogItemProxy;

   public function IssueMediator(viewComp:IssueForm) {
      super(NAME, viewComp);
      _proxy = BacklogItemProxy(facade.retrieveProxy(BacklogItemProxy.NAME));
      viewComp.cboItems.dataProvider = _proxy.selectedBacklog;
      viewComp.lstResponsible.dataProvider = _proxy.assignToList;
      viewComp.btnCancel.addEventListener(MouseEvent.CLICK, handleCancel);
      viewComp.btnSave.addEventListener(MouseEvent.CLICK, handleImpedimentSave);
   }

   override public function registerValidators():void {
      _validators.push(view.titleValidator);
      _validators.push(view.descriptionValidator);
      _validators.push(view.cboItemsValidator);
   }


   override public function listNotificationInterests():Array {
      return [ApplicationFacade.NOTE_EDIT_IMPEDIMENT,
         ApplicationFacade.NOTE_CREATE_IMPEDIMENT];
   }

   override public function handleNotification(notification:INotification):void {
      switch (notification.getName()) {
         case ApplicationFacade.NOTE_CREATE_IMPEDIMENT :
            _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_CREATE;
            view.btnSave.label = "Save";
            break;
         case ApplicationFacade.NOTE_EDIT_IMPEDIMENT :
            _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_EDIT;
            view.btnSave.label = "Update";
            break;
      }
      bindForm();
      view.focusManager.setFocus(view.issueTitle);
   }

   override public function bindForm():void {
      view.issueTitle.text = _proxy.currentIssue.title;
      view.description.text = _proxy.currentIssue.description;
      view.cboItems.selectedItem = _proxy.currentBacklogItem;
      if (_proxy.currentIssue.responsible) {
         view.lstResponsible.selectedIndex = getSelectedUser(
               _proxy.currentIssue.responsible.id, view.lstResponsible.dataProvider.source);
      }
      else {
         view.lstResponsible.selectedIndex = -1;
      }
      FormUtility.clearValidationErrors(_validators);
   }

   private function getSelectedUser(id:Number, array:Array):int {
      for (var index:int = 0; index < array.length; index++) {
         if (array[index].id == id) {
            return index;
         }
      }
      return -1;
   }

   override public function bindModel():void {
      _proxy.currentIssue.title = view.issueTitle.text;
      _proxy.currentIssue.description = view.description.text;
      _proxy.currentIssue.responsible = view.lstResponsible.selectedItem as User;
   }

   private function handleImpedimentSave(event:MouseEvent):void {
      if (validate(true)) {
         bindModel();
         if (_proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CREATE) {
            var request:AddImpedimentRequest = new AddImpedimentRequest();
            request.impediment = _proxy.currentIssue;
            request.iterationId = _proxy.currentIteration.id;
            request.backlogItemId = view.cboItems.selectedItem.id;
            sendNotification(ApplicationFacade.NOTE_ADD_IMPEDIMENT, request);
         }
         else {
            sendNotification(ApplicationFacade.NOTE_UPDATE_IMPEDIMENT, _proxy.currentBacklogItem.impediment);
         }
         closeWindow();
      }
      else {
         event.stopImmediatePropagation();
      }
   }

   private function handleCancel(event:MouseEvent):void {
      closeWindow();
   }

   private function closeWindow():void {
      view.parent.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
   }

   protected function get view():IssueForm {
      return viewComponent as IssueForm;
   }

}
}