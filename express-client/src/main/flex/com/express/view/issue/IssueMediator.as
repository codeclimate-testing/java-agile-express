package com.express.view.issue {

import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.view.form.FormMediator;
import com.express.view.form.FormUtility;

import flash.events.MouseEvent;

import mx.events.CloseEvent;

import org.puremvc.as3.interfaces.INotification;

public class IssueMediator extends FormMediator
{
   public static const NAME:String = "IssueMediator";
   public static const CREATE:String = "IssueMediator.CREATE";
   public static const EDIT:String = "IssueMediator.EDIT";

   private var _proxy : ProjectProxy;

   public function IssueMediator(viewComp: IssueForm) {
      super(NAME, viewComp);
      _proxy = ProjectProxy(facade.retrieveProxy(ProjectProxy.NAME));
      viewComp.btnCancel.addEventListener(MouseEvent.CLICK, handleCancel);
      viewComp.btnSave.addEventListener(MouseEvent.CLICK, handleIterationSave);
   }

   override public function registerValidators():void {
      _validators.push(view.titleValidator);
      _validators.push(view.startValidator);
      _validators.push(view.endValidator);
   }


   override public function listNotificationInterests():Array {
      return [CREATE, EDIT];
   }

   override public function handleNotification(notification : INotification):void {
      bindForm();
      view.focusManager.setFocus(view.issueTitle);
   }

   override public function bindForm():void {
      view.visible = true;
      view.issueTitle.text = _proxy.newIteration.title;
      view.description.text = _proxy.newIteration.description;
      view.startDate.selectedDate = _proxy.newIteration.startDate;
      view.endDate.selectedDate = _proxy.newIteration.endDate;
      FormUtility.clearValidationErrors(_validators);
      if(_proxy.newIteration.id) {
         view.btnSave.label = "Update";
      }
      else {
         view.btnSave.label = "Create";
      }
   }

   override public function bindModel():void {
      _proxy.newIteration.title = view.issueTitle.text;
      _proxy.newIteration.description = view.description.text;
      _proxy.newIteration.startDate = view.startDate.selectedDate;
      _proxy.newIteration.endDate = view.endDate.selectedDate;
      _proxy.selectedProject.iterations.addItem(_proxy.newIteration);
   }

   private function handleIterationSave(event : MouseEvent) : void {
      if (validate(true)) {
         bindModel();
         if(_proxy.newIteration.id > 0) {
            sendNotification(ApplicationFacade.NOTE_UPDATE_ITERATION);
         }
         else {
            sendNotification(ApplicationFacade.NOTE_CREATE_ITERATION);
         }
         closeWindow();
      }
      else {
         event.stopImmediatePropagation();
      }
   }

   private function handleCancel(event : MouseEvent) : void {
      closeWindow();
   }

   private function closeWindow() : void {
      view.parent.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
   }

   protected function get view():IssueForm
   {
      return viewComponent as IssueForm;
   }

}
}