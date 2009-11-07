package com.express.view.backlog
{
import com.express.model.SecureContextProxy;
import com.express.view.*;
import com.express.ApplicationFacade;
import com.express.controller.IterationCreateCommand;
import com.express.controller.ProjectLoadCommand;
import com.express.controller.event.GridButtonEvent;
import com.express.model.ProjectProxy;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.Iteration;
import com.express.model.request.BacklogItemAssignRequest;
import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.iteration.IterationMediator;
import com.express.view.projectPanel.ProjectPanelMediator;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.controls.ComboBox;
import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
import mx.events.CloseEvent;
import mx.events.DragEvent;
import mx.managers.DragManager;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class BacklogMediator extends Mediator
{
   public static const NAME : String = "BacklogViewMediator";

   private var _proxy : ProjectProxy;
   private var _secureContext : SecureContextProxy;
   private var _viewOnClick : Boolean;

   public function BacklogMediator(viewComp : BacklogView)
   {
      super(NAME, viewComp);
      _proxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      _secureContext = facade.retrieveProxy(SecureContextProxy.NAME) as SecureContextProxy;
      facade.registerMediator(new ProjectPanelMediator(viewComp.projectPanel));

      viewComp.cboIterations.dataProvider = _proxy.iterationList;
      viewComp.grdIterationBacklog.dataProvider = _proxy.selectedBacklog;
      viewComp.grdProductBacklog.dataProvider = _proxy.productBacklog;
      viewComp.grdIterationBacklog.dataTipFunction = buildToolTip;
      viewComp.grdProductBacklog.dataTipFunction = buildToolTip;
      viewComp.assignedToColumn.labelFunction = formatAssignedTo;
      viewComp.auth.userRoles = _secureContext.availableRoles;

      viewComp.cboIterations.addEventListener(Event.CHANGE, handleIterationSelected);
      viewComp.lnkCreateIteration.addEventListener(MouseEvent.CLICK, handleCreateIteration);
      viewComp.grdIterationBacklog.addEventListener(GridButtonEvent.CLICK, handleGridButton);
      viewComp.grdProductBacklog.addEventListener(GridButtonEvent.CLICK, handleGridButton);
      viewComp.grdIterationBacklog.addEventListener(MouseEvent.DOUBLE_CLICK, handleGridDoubleClick);
      viewComp.grdProductBacklog.addEventListener(MouseEvent.DOUBLE_CLICK, handleGridDoubleClick);
      viewComp.btnCreateItem.addEventListener(MouseEvent.CLICK, handleCreateBacklogItem);
      viewComp.btnProductCreateItem.addEventListener(MouseEvent.CLICK, handleCreateProductBacklogItem);
      viewComp.grdIterationBacklog.addEventListener(DragEvent.DRAG_DROP, dropInIteration);
      viewComp.grdProductBacklog.addEventListener(DragEvent.DRAG_DROP, dropInProductBacklog);

      viewComp.grdProductBacklog.addEventListener(DragEvent.DRAG_ENTER, handleDragEnter);
      viewComp.grdIterationBacklog.addEventListener(DragEvent.DRAG_ENTER, handleDragEnter);
   }

   /**
    * Disallow draging of tasks
    * @param event
    * @return
    */
   private function handleDragEnter(event : DragEvent) : void {
      var items : Array = event.dragSource.dataForFormat('treeDataGridItems') as Array;
      for each(var item : BacklogItem in items) {
         if(item.parent) {
            event.preventDefault();
            DragManager.showFeedback(DragManager.NONE);
            return;
         }
      }
   }

   private function dropInIteration(event : DragEvent) : void {

      var assignmentRequest : BacklogItemAssignRequest =
            createAssignmentrequest(event.dragSource.dataForFormat('treeDataGridItems') as Array);
      assignmentRequest.iterationToId = _proxy.selectedIteration.id;
      assignmentRequest.iterationFromId = 0;
      sendNotification(ApplicationFacade.NOTE_ASSIGN_BACKLOG_ITEM, assignmentRequest);
   }

   private function dropInProductBacklog(event : DragEvent) : void {
      var assignmentRequest : BacklogItemAssignRequest =
            createAssignmentrequest(event.dragSource.dataForFormat('treeDataGridItems') as Array);
      assignmentRequest.iterationToId = 0;
      assignmentRequest.iterationFromId = _proxy.selectedIteration.id;
      sendNotification(ApplicationFacade.NOTE_ASSIGN_BACKLOG_ITEM, assignmentRequest);
   }

   private function createAssignmentrequest(items : Array) : BacklogItemAssignRequest {
      var assignmentRequest : BacklogItemAssignRequest = new BacklogItemAssignRequest();
      var length : int = items.length;
      for (var index : int; index < length; index++) {
         assignmentRequest.itemIds.push(items[index].id);
      }
      return assignmentRequest;
   }

   override public function listNotificationInterests():Array {
      return [ProjectLoadCommand.SUCCESS,IterationCreateCommand.SUCCESS];
   }

   override public function handleNotification(notification:INotification):void {
      switch (notification.getName()) {
         case ProjectLoadCommand.SUCCESS :
            handleProjectSelected();
            break;
         case IterationCreateCommand.SUCCESS :
            var iteration : Iteration = notification.getBody() as Iteration;
            _proxy.selectedProject.iterations.removeItemAt(_proxy.selectedProject.iterations.length - 1);
            _proxy.selectedProject.iterations.addItem(iteration);
            _proxy.newIteration = null;
            _proxy.updateIterationList();
            view.cboIterations.selectedItem = iteration;
            selectIteration(iteration);
            break;
      }
   }

   private function handleProjectSelected() : void {
      view.lnkCreateIteration.enabled = true;
      _proxy.selectedIteration = _proxy.selectedProject.currentIteration;
      view.btnProductCreateItem.enabled = _proxy.selectedProject != null;
      if (!_proxy.selectedIteration) {
         view.cboIterations.selectedIndex = -1;
         view.btnCreateItem.enabled = false;
      }
      else {
         view.cboIterations.selectedIndex = getSelectionIndex(view.cboIterations.dataProvider as ArrayCollection, _proxy.selectedIteration);
         view.btnCreateItem.enabled = true;
      }
      ProjectPanelMediator(facade.retrieveMediator(ProjectPanelMediator.NAME)).bindIterationDisplay();
   }

   private function getSelectionIndex(list : ArrayCollection, iteration :Iteration) : int{
      for(var index : int = 0; index < list.length; index++) {
         if(list.getItemAt(index).id == iteration.id) {
            return index;
         }
      }
      return -1;
   }

   private function handleIterationSelected(event : Event) : void {
       selectIteration((event.target as ComboBox).selectedItem as Iteration);

   }

   private function selectIteration(iteration : Iteration) : void {
      _proxy.selectedIteration = iteration;
      view.btnCreateItem.enabled = _proxy.selectedBacklog.source != null;
      sendNotification(ApplicationFacade.NOTE_ITERATION_SELECTED);
   }
   
   public function handleCreateIteration(event : MouseEvent) : void {
      _proxy.newIteration = new Iteration();
      _proxy.newIteration.project = _proxy.selectedProject;
      _proxy.newIteration.title = "Iteration " +
                                  (_proxy.selectedProject.iterations.length + 1);
      sendNotification(IterationMediator.CREATE);
   }

   private function handleGridButton(event : GridButtonEvent) : void {
      switch (event.action) {
         case GridButtonEvent.ACTION_ADD_CHILD :
            var task : BacklogItem = new BacklogItem();
            var parent : BacklogItem = event.data as BacklogItem;
            task.parent = parent;
            sendNotification(BacklogItemMediator.CREATE, task);
            break;
         case GridButtonEvent.ACTION_REMOVE :
            _proxy.selectedBacklogItem = event.data as BacklogItem;
            Alert.show("Are you sure you want to delete this item?", "Confirm Removal",
                  Alert.YES | Alert.NO, null, removeConfirmed, null, Alert.YES);
            break;
      }
   }

   private function handleGridDoubleClick(event : MouseEvent) : void {
      var item : BacklogItem = event.currentTarget.selectedItem as BacklogItem;
      sendNotification(BacklogItemMediator.EDIT, item);
   }

   private function removeConfirmed(event : CloseEvent) : void {
      if (event.detail == Alert.YES) {
         if(_proxy.selectedBacklogItem.inProductBacklog()) {
            _proxy.productBacklogRequest = true;
         }
         sendNotification(ApplicationFacade.NOTE_REMOVE_BACKLOG_ITEM, _proxy.selectedBacklogItem);
      }
   }

   private function handleCreateBacklogItem(event : MouseEvent) : void {
      var story : BacklogItem = new BacklogItem();
      story.iteration = _proxy.selectedIteration;
      _proxy.productBacklogRequest = false;
      sendNotification(BacklogItemMediator.CREATE, story);
   }

   private function handleCreateProductBacklogItem(event : MouseEvent) : void {
      var story : BacklogItem = new BacklogItem();
      story.project = _proxy.selectedProject;
      _proxy.productBacklogRequest = true;
      sendNotification(BacklogItemMediator.CREATE, story);
   }

   private function formatAssignedTo(row : Object, col : AdvancedDataGridColumn) : String {
      var item : BacklogItem = row as BacklogItem;
      if (item.assignedTo == null) {
         return "Unassigned";
      }
      return item.assignedTo.fullName;
   }

   private function buildToolTip(row : Object) : String {
      var item : BacklogItem = row as BacklogItem;
      return item ? item.summary : "";
   }

   public function get view():BacklogView {
      return viewComponent as BacklogView;
   }

}
}