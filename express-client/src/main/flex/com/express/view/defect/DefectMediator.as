package com.express.view.defect
{
import com.express.ApplicationFacade;
import com.express.controller.event.GridButtonEvent;
import com.express.model.ProjectProxy;
import com.express.model.domain.Issue;
import com.express.model.domain.User;
import com.express.view.display.DefectDisplay;
import com.express.view.form.DefectForm;

import flash.events.MouseEvent;

import mx.controls.Alert;
import mx.events.CloseEvent;
import mx.events.ListEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class DefectMediator extends Mediator
{
   public static const NAME : String = "DefectViewMediator";

   public static const ACTION_ITEM_CREATE : int = 1;
   public static const ACTION_ITEM_EDIT : int = 2;

   private var _viewAction :int = 0;

   private var _projectProxy : ProjectProxy;

   public function DefectMediator(viewComp : DefectView)
   {
      super(NAME, viewComp);
      _projectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      viewComp.defectGrid.addEventListener(GridButtonEvent.CLICK, handleGridButtonClick);
      viewComp.defectGrid.addEventListener(ListEvent.ITEM_CLICK, handleGridRowClicked);
      //viewComp.defectGrid.addEventListener(DragEvent.DRAG_COMPLETE, handleAssignItem);
      //      viewComp.assignToIterationList.cancelButton.addEventListener(MouseEvent.CLICK, handleAddIterationCancel);
      viewComp.defectForm.cancelButton.addEventListener(MouseEvent.CLICK, handleItemCancel);
      viewComp.defectForm.saveButton.addEventListener(MouseEvent.CLICK, handleDefectSave);
      viewComp.createDefectButton.addEventListener(MouseEvent.CLICK, handleCreateNewDefectItem);
      //viewComp.defectForm.itemStatus.dataProvider = _projectProxy.statusList;
      viewComp.defectGrid.dataProvider = _projectProxy.defectList;
   }

//   override public function listNotificationInterests():Array {
//      return [ApplicationFacade.NOTE_ITERATION_SELECTED];
//   }
//
//   override public function handleNotification(notification:INotification):void {
//      view.defectGrid.dataProvider = _projectProxy.defectList;
//   }

   public function handleAssignIteration(event : MouseEvent) : void {
      switchToAssignIteration();
   }

   public function handleItemCancel(event : MouseEvent) : void {
      view.drawer.visible = false;
   }

   public function handleDefectSave(event : MouseEvent) : void {
      var form : DefectForm = view.defectForm;
      var defect : Issue = _projectProxy.selectedDefect;
      defect.title = form.itemTitle.text;
      defect.description = form.description.text;
      defect.assignedTo = form.assignedTo.selectedItem as User;
      defect.createdBy = form.createdBy.selectedItem as User;
      defect.createdDate = form.createdDate as Date;
      defect.status = form.itemStatus.text;
      if (ACTION_ITEM_CREATE == _viewAction) {
         sendNotification(ApplicationFacade.NOTE_CREATE_DEFECT, defect);
      }
      else {
         sendNotification(ApplicationFacade.NOTE_UPDATE_DEFECT, defect);
      }
      view.drawer.visible = false;
   }

   public function handleAddIterationCancel(event : MouseEvent) : void {
      view.drawer.visible = false;
   }

   public function handleGridButtonClick(event : GridButtonEvent) : void {
      _projectProxy.selectedDefect = event.data as Issue;
      switch (event.action) {
         case GridButtonEvent.ACTION_EDIT :
            _viewAction = ACTION_ITEM_EDIT;
            bindDefectForm();
            switchToDefectEdit();
            break;
         case GridButtonEvent.ACTION_VIEW :
            bindDefectDisplay();
            switchToDefectDisplay();
            break;
         case GridButtonEvent.ACTION_REMOVE :
            Alert.show("Delete Defect, are you sure?", "Delete Defect", Alert.YES + Alert.NO, null, handleRemoveDefectAlert, null, Alert.NO);
            break;
      }
   }

   private function handleRemoveDefectAlert(eventObj:CloseEvent):void
   {
      if (eventObj.detail == Alert.YES) {
         sendNotification(ApplicationFacade.NOTE_REMOVE_DEFECT, _projectProxy.selectedDefect);
      }
   }

   public function handleGridRowClicked(event: ListEvent) : void {
      //_projectProxy.selectedDefect = _projectProxy.defectList.getItemAt(event.rowIndex) as Issue;
      //bindDefectDisplay();
      //switchToDefectDisplay();
   }


   public function handleCreateNewDefectItem(event : MouseEvent) : void {
      _projectProxy.selectedDefect = new Issue();
      clearDefectForm();
      _viewAction = ACTION_ITEM_CREATE;
      switchToDefectEdit();
   }

   protected function switchToDefectDisplay() : void {
      view.defectForm.visible = false;
      view.defectForm.includeInLayout = false;
      view.assignToIterationList.visible = false;
      view.assignToIterationList.includeInLayout = false;
      view.defectDisplay.visible = true;
      view.drawer.visible = true;
   }

   protected function switchToDefectEdit() : void {
      view.defectDisplay.visible = false;
      view.defectDisplay.includeInLayout = false;
      view.assignToIterationList.visible = false;
      view.assignToIterationList.includeInLayout = false;
      view.defectForm.visible = true;
      view.drawer.visible = true;
   }

   protected function switchToAssignIteration() : void {
      view.defectForm.visible = false;
      view.defectForm.includeInLayout = false;
      view.defectDisplay.visible = false;
      view.defectDisplay.includeInLayout = false;
      view.assignToIterationList.visible = true;
      view.assignToIterationList.iterationAssignmentList.dataProvider = _projectProxy.selectedProject.iterations;
      view.assignToIterationList.iterationAssignmentList.selectedIndex = 0;
      view.assignToIterationList.backlogAssignmentList.dataProvider = _projectProxy.selectedProject.iterations.getItemAt(0).backLog;
      view.drawer.visible = true;
   }

   protected function bindDefectDisplay() : void {
      if (_projectProxy.selectedDefect != null) {
         var display : DefectDisplay = view.defectDisplay;
         display.itemTitle.text = _projectProxy.selectedDefect.title;
         display.description.text = _projectProxy.selectedDefect.description;
         display.reference.text = _projectProxy.selectedDefect.reference;
         display.itemStatus.text = _projectProxy.selectedDefect.status;
      }
   }

   protected function bindDefectForm() : void {
      if (_projectProxy.selectedDefect != null) {
         var form : DefectForm = view.defectForm;
         form.itemTitle.text = _projectProxy.selectedDefect.title;
         form.description.text = _projectProxy.selectedDefect.description;
         form.reference.text = _projectProxy.selectedDefect.reference;
         form.itemStatus.text = _projectProxy.selectedDefect.status;
      }
   }

   protected function clearDefectForm() : void {
      var form : DefectForm = view.defectForm;
      form.itemTitle.text = "";
      form.description.text = "";
      form.reference.text = "";
      form.itemStatus.text = "";
   }

   public function get view(): DefectView
   {
      return viewComponent as DefectView;
   }

}
}