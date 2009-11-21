package com.express.view.backlogItem
{
import com.express.ApplicationFacade;
import com.express.controller.event.GridButtonEvent;
import com.express.model.ProjectProxy;
import com.express.model.domain.AcceptanceCriteria;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.Project;
import com.express.model.domain.Theme;
import com.express.model.domain.User;
import com.express.model.request.CreateBacklogItemRequest;
import com.express.view.form.FormMediator;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.TitleWindow;
import mx.events.CloseEvent;

import org.puremvc.as3.interfaces.INotification;

public class BacklogItemMediator extends FormMediator {
   public static const NAME : String = "BacklogItemMediator";
   public static const EDIT : String = "Note.start.EditBacklogItem";
   public static const CREATE : String = "Note.start.CreateBacklogItem";

   private var _proxy : BacklogItemProxy;
   private var _projectProxy : ProjectProxy;

   public function BacklogItemMediator(viewComp : BacklogItemView, mediatorName : String = NAME) {
      super(mediatorName, viewComp);
      _proxy = facade.retrieveProxy(BacklogItemProxy.NAME) as BacklogItemProxy;
      _projectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      viewComp.backlogItemForm.itemStatus.dataProvider = _proxy.statusList;
      viewComp.backlogItemForm.lstTheme.dataProvider = _projectProxy.themes;

      viewComp.acceptanceCriteriaView.grdCriteria.addEventListener(GridButtonEvent.CLICK, handleGridButton);
      viewComp.btnSave.addEventListener(MouseEvent.CLICK, handleItemSave);
      viewComp.btnCancel.addEventListener(MouseEvent.CLICK, handleCancel);
      viewComp.backlogItemForm.defaultButton = viewComp.btnSave;

      viewComp.acceptanceCriteriaView.btnAdd.addEventListener(MouseEvent.CLICK, handleAddCriteria);
   }


   override public function registerValidators():void {
      view.backlogItemForm.summaryValidator.trigger = view.btnSave;
      view.backlogItemForm.summaryValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.summaryValidator);
      view.backlogItemForm.asAValidator.trigger = view.btnSave;
      view.backlogItemForm.asAValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.asAValidator);
      view.backlogItemForm.iWantValidator.trigger = view.btnSave;
      view.backlogItemForm.iWantValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.iWantValidator);
      view.backlogItemForm.soThatValidator.trigger = view.btnSave;
      view.backlogItemForm.soThatValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.soThatValidator);
      view.backlogItemForm.effortValidator.trigger = view.btnSave;
      view.backlogItemForm.effortValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.effortValidator);
      view.backlogItemForm.valueValidator.trigger = view.btnSave;
      view.backlogItemForm.valueValidator.triggerEvent = MouseEvent.CLICK;
      _validators.push(view.backlogItemForm.valueValidator);
   }

   private function handleAddCriteria(event : Event) : void {
      var criteria : AcceptanceCriteria = new AcceptanceCriteria();
      criteria.backlogItem = _proxy.selectedBacklogItem;
      _proxy.selectedBacklogItem.acceptanceCriteria.addItem(criteria);
      var focusedCell:Object = new Object();;
      view.acceptanceCriteriaView.grdCriteria.editedItemPosition =
         {rowIndex: _proxy.selectedBacklogItem.acceptanceCriteria.length -1, columnIndex: 0};
   }

   private function handleGridButton(event : GridButtonEvent) : void {
      var index: int = view.acceptanceCriteriaView.grdCriteria.selectedIndex;
      _proxy.selectedBacklogItem.acceptanceCriteria.removeItemAt(index);
   }

   private function handleItemSave(event : MouseEvent) : void {
      if (validate(true)) {
         bindModel();
         if (_proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CHILD_EDIT ||
             _proxy.viewAction == BacklogItemProxy.ACTION_ITEM_EDIT) {
            _projectProxy.productBacklogRequest = _proxy.selectedBacklogItem.inProductBacklog();
            sendNotification(ApplicationFacade.NOTE_UPDATE_BACKLOG_ITEM, _proxy.selectedBacklogItem);
         }
         else {
            var request : CreateBacklogItemRequest = new CreateBacklogItemRequest();
            request.backlogItem = _proxy.selectedBacklogItem;
            if (request.backlogItem.project != null) {
               request.type = CreateBacklogItemRequest.UNCOMMITED_STORY;
               request.parentId = request.backlogItem.project.id;
            }
            else if (request.backlogItem.iteration != null) {
               request.type = CreateBacklogItemRequest.STORY;
               request.parentId = request.backlogItem.iteration.id;
            }
            else {
               request.type = CreateBacklogItemRequest.TASK;
               request.parentId = request.backlogItem.parent.id;
            }
            _projectProxy.productBacklogRequest = request.backlogItem.inProductBacklog();
            sendNotification(ApplicationFacade.NOTE_CREATE_BACKLOG_ITEM, request);
         }
         closeWindow();
      }
      else {
         event.stopImmediatePropagation();
      }
   }

   public function handleCancel(event : MouseEvent) : void {
      removeUnsavedAcceptanceCriteria();
      closeWindow();
   }

   private function closeWindow() : void {
      TitleWindow(view.parent).removeEventListener(CloseEvent.CLOSE, handleWindowClose);
      view.parent.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
   }

   public override function bindModel() : void {
      setTypeSpecificFields();
      _proxy.selectedBacklogItem.title = view.backlogItemForm.title.text;
      _proxy.selectedBacklogItem.effort = int(view.backlogItemForm.effort.text);
      _proxy.selectedBacklogItem.businessValue = int(view.backlogItemForm.businessValue.text);
      _proxy.selectedBacklogItem.status = view.backlogItemForm.itemStatus.selectedLabel;
      _proxy.selectedBacklogItem.assignedTo = view.backlogItemForm.assignedToList.selectedItem as User;
      _proxy.selectedBacklogItem.detailedDescription = view.backlogItemForm.descriptionEditor.htmlText;
   }

   private function setTypeSpecificFields() : void {
      if (_proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CHILD_CREATE ||
          _proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CHILD_EDIT) {
         _proxy.selectedBacklogItem.summary = view.backlogItemForm.summary.text;
      }
      else {
         _proxy.selectedBacklogItem.themes.source = view.backlogItemForm.lstTheme.selectedItems;
         _proxy.selectedBacklogItem.asA = view.backlogItemForm.asA.text;
         _proxy.selectedBacklogItem.want = view.backlogItemForm.iWant.text;
         _proxy.selectedBacklogItem.soThat = view.backlogItemForm.soThat.text;
         _proxy.selectedBacklogItem.summary = "As " + view.backlogItemForm.asA.text + " I want " + view.backlogItemForm.iWant.text +
                                              " so that " + view.backlogItemForm.soThat.text;
      }
   }

   public override function bindForm() : void {
      view.backlogItemForm.asA.dataProvider = _projectProxy.selectedProject.actors;
      view.acceptanceCriteriaView.grdCriteria.dataProvider = _proxy.selectedBacklogItem.acceptanceCriteria;
      if (_proxy.selectedBacklogItem != null) {
         view.backlogItemForm.title.text = _proxy.selectedBacklogItem.title;
         view.backlogItemForm.itemStatus.selectedItem = _proxy.selectedBacklogItem.status;
         view.backlogItemForm.effort.text = _proxy.selectedBacklogItem.effort.toString();
         view.backlogItemForm.businessValue.text = _proxy.selectedBacklogItem.businessValue.toString();
         view.backlogItemForm.assignedToList.dataProvider = _projectProxy.developers;
         view.backlogItemForm.assignedToList.selectedIndex = _projectProxy.getDeveloperIndex(_proxy.selectedBacklogItem.assignedTo);
         view.backlogItemForm.descriptionEditor.textArea.htmlText = _proxy.selectedBacklogItem.detailedDescription;
         if (_proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CHILD_CREATE ||
             _proxy.viewAction == BacklogItemProxy.ACTION_ITEM_CHILD_EDIT) {
            view.backlogItemForm.summary.text = _proxy.selectedBacklogItem.summary;
            view.backlogItemForm.focusManager.setFocus(view.backlogItemForm.summary);
         }
         else {
            view.backlogItemForm.asA.text = _proxy.selectedBacklogItem.asA;
            view.backlogItemForm.iWant.text = _proxy.selectedBacklogItem.want;
            view.backlogItemForm.soThat.text = _proxy.selectedBacklogItem.soThat;
            view.backlogItemForm.lstTheme.selectedIndices = getSelectedThemeIndices(_proxy.selectedBacklogItem.themes.source);
            view.backlogItemForm.focusManager.setFocus(view.backlogItemForm.asA);
         }
         resetValidation();
         view.tbViews.selectedIndex = 0;
      }
   }

   private function handleWindowClose(event : Event) : void {
      removeUnsavedAcceptanceCriteria();
   }

   private function removeUnsavedAcceptanceCriteria() : void {
      var copy : Array= _proxy.selectedBacklogItem.acceptanceCriteria.source.concat();
      _proxy.selectedBacklogItem.acceptanceCriteria.source = [];
      for each(var criteria : AcceptanceCriteria in copy) {
         if(criteria.id > 0) {
            _proxy.selectedBacklogItem.acceptanceCriteria.addItem(criteria);
         }
      }
   }

   private function getSelectedThemeIndices(themes : Array) : Array {
      var indices : Array = [];
      var projectThemes : ArrayCollection = _projectProxy.themes;
      for each(var theme : Theme in themes) {
         for(var index : int = 0; index < projectThemes.length; index++) {
            if(theme.id == projectThemes[index].id) {
               indices.push(index);
               break;
            }
         }
      }
      return indices;
   }

   public override function listNotificationInterests():Array {
      return [EDIT, CREATE];
   }

   public override function handleNotification(notification:INotification):void {
      TitleWindow(view.parent).addEventListener(CloseEvent.CLOSE, handleWindowClose);
      _proxy.selectedBacklogItem = notification.getBody() as BacklogItem;
      switch (notification.getName()) {
         case CREATE :
            if (_proxy.selectedBacklogItem.parent == null) {
               _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_CREATE;
               TitleWindow(view.parent).title = "New Story";
               setStoryView();
            }
            else {
               _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_CHILD_CREATE;
               TitleWindow(view.parent).title = "Add Task for " + _proxy.selectedBacklogItem.parent.reference;
               setTaskView();
            }
            break;
         case EDIT :
            if (_proxy.selectedBacklogItem.parent == null) {
               _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_EDIT;
               TitleWindow(view.parent).title = "Editing Story " + _proxy.selectedBacklogItem.reference;
               setStoryView();
            }
            else {
               _proxy.viewAction = BacklogItemProxy.ACTION_ITEM_CHILD_EDIT;
               TitleWindow(view.parent).title = "Editing Task " + _proxy.selectedBacklogItem.reference;
               setTaskView();
            }
            break;
      }
      bindForm();
   }

   private function setTaskView() : void {
      view.backlogItemForm.businessValueItem.visible = false;
      view.backlogItemForm.businessValueItem.includeInLayout = false;
      view.backlogItemForm.effortUnitLabel.text = Project.EFFORT_HOURS;
      view.backlogItemForm.asItem.visible = false;
      view.backlogItemForm.asItem.includeInLayout = false;
      view.backlogItemForm.iWantItem.visible = false;
      view.backlogItemForm.iWantItem.includeInLayout = false;
      view.backlogItemForm.soThatItem.visible = false;
      view.backlogItemForm.soThatItem.includeInLayout = false;
      view.backlogItemForm.themeItem.visible = false;
      view.backlogItemForm.themeItem.includeInLayout = false;
      view.backlogItemForm.summaryItem.visible = true;
      view.backlogItemForm.summaryItem.includeInLayout = true;
   }

   private function setStoryView() : void {
      view.backlogItemForm.businessValueItem.visible = true;
      view.backlogItemForm.businessValueItem.includeInLayout = true;
      view.backlogItemForm.effortUnitLabel.text = _proxy.selectedBacklogItem.getProject().effortUnit;
      view.backlogItemForm.asItem.visible = true;
      view.backlogItemForm.asItem.includeInLayout = true;
      view.backlogItemForm.iWantItem.visible = true;
      view.backlogItemForm.iWantItem.includeInLayout = true;
      view.backlogItemForm.soThatItem.visible = true;
      view.backlogItemForm.soThatItem.includeInLayout = true;
      view.backlogItemForm.themeItem.visible = true;
      view.backlogItemForm.themeItem.includeInLayout = true;
      view.backlogItemForm.summaryItem.visible = false;
      view.backlogItemForm.summaryItem.includeInLayout = false;
   }

   public function get view() : BacklogItemView {
      return viewComponent as BacklogItemView;
   }
}
}