package com.express.view.wall
{
import com.express.ApplicationFacade;
import com.express.controller.ImpedimentCreateCommand;
import com.express.controller.IterationLoadCommand;
import com.express.controller.ProjectLoadCommand;
import com.express.controller.event.StoryClickEvent;
import com.express.model.ProjectProxy;
import com.express.model.SecureContextProxy;
import com.express.model.WallProxy;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.Issue;
import com.express.model.domain.User;
import com.express.model.request.AddImpedimentRequest;
import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.components.AssignmentPopup;
import com.express.view.renderer.CardView;
import com.express.view.renderer.StoryView;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.ViewStack;
import mx.events.DragEvent;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class WallMediator extends Mediator
{
   public static const NAME:String = "com.express.view.wall.WallMediator";

   private var _minHeight : Number;
   private var _wallProxy : WallProxy;
   private var _projectProxy : ProjectProxy;
   private var _secureContext : SecureContextProxy;
   private var _visible : Boolean = true;
   private var _assignmentPopup : AssignmentPopup;

   public function WallMediator(viewComp : WallView) {
      super(NAME, viewComp);
      _wallProxy = facade.retrieveProxy(WallProxy.NAME) as WallProxy;
      _projectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      _secureContext = facade.retrieveProxy(SecureContextProxy.NAME) as SecureContextProxy;
      _minHeight = ViewStack(view.parent).minHeight;

      viewComp.lstStories.dataProvider = _wallProxy.currentBacklog;
      viewComp.lstOpenItems.dataProvider = _wallProxy.openItems;
      viewComp.lstProgressItems.dataProvider = _wallProxy.inProgressItems;
      viewComp.lstTestItems.dataProvider = _wallProxy.testItems;
      viewComp.lstDoneItems.dataProvider = _wallProxy.doneItems;

      viewComp.lstOpenItems.addEventListener(DragEvent.DRAG_DROP, droppedInOpen);
      viewComp.lstProgressItems.addEventListener(DragEvent.DRAG_DROP, droppedInProgress);
      viewComp.lstTestItems.addEventListener(DragEvent.DRAG_DROP, droppedInTest);
      viewComp.lstDoneItems.addEventListener(DragEvent.DRAG_DROP, droppedInDone);

      viewComp.storyListHolder.addEventListener(StoryClickEvent.STORY_EDIT, handleEditStory);
      viewComp.storyListHolder.addEventListener(StoryClickEvent.TASK_ADD, handleAddTask);
      viewComp.lstOpenItems.addEventListener(StoryClickEvent.STORY_EDIT, handleEditTask);
      viewComp.lstProgressItems.addEventListener(StoryClickEvent.STORY_EDIT, handleEditTask);
      viewComp.lstTestItems.addEventListener(StoryClickEvent.STORY_EDIT, handleEditTask);
      viewComp.lstDoneItems.addEventListener(StoryClickEvent.STORY_EDIT, handleEditTask);
      loadIterationBacklog();
      viewComp.addEventListener(FlexEvent.SHOW, handleShowView);
      viewComp.addEventListener(FlexEvent.HIDE, handleHideView);
   }

   public function handleShowView(event : Event) : void {
      calculateNewHeight();
      _visible = true;
   }

   public function handleHideView(event : Event) : void {
      view.parent.height = _minHeight;
      ViewStack(view.parent).minHeight = _minHeight;
      _visible = false;
   }

   private function calculateNewHeight() : void {
      var newHeight : Number;
      if (_projectProxy.selectedIteration != null) {
         var requiredHeight : Number = 115 * _wallProxy.currentBacklog.length;
         newHeight = requiredHeight > _minHeight ? requiredHeight : _minHeight;
      }
      else {
         newHeight = _minHeight;
      }
      view.storyListHolder.height = newHeight - 50;
      view.swimLanes.height = newHeight - 40;
      view.height = newHeight;
   }

   private function getAssignmentPopup() : AssignmentPopup {
      if(! _assignmentPopup) {
         _assignmentPopup = new AssignmentPopup();
         _assignmentPopup.addEventListener(FlexEvent.CREATION_COMPLETE, handlePopupCreationComplete);
      }
      else {
         _assignmentPopup.cboAssignedTo.selectedItem = getAssigneeIndex(_secureContext.currentUser.id);
      }
      return _assignmentPopup;
   }

   private function handlePopupCreationComplete(event : Event) : void {
      _assignmentPopup.cboAssignedTo.dataProvider = _projectProxy.developers;
      _assignmentPopup.btnAssign.addEventListener(MouseEvent.CLICK, handleAssigned);
      _assignmentPopup.cboAssignedTo.selectedIndex = getAssigneeIndex(_secureContext.currentUser.id);
   }

   private function getAssigneeIndex(id :int) : int {
      for(var index : int ; index < _projectProxy.developers.length; index++) {
         if(_projectProxy.developers[index].id == id) {
            return index;
         }
      }
      return -1;
   }

   private function handleAssigned(event : Event) : void {
      var item : BacklogItem = _wallProxy.inProgresstem;
      item.assignedTo = _assignmentPopup.cboAssignedTo.selectedItem as User;
      buildAndSendUpdateNotification(BacklogItem.STATUS_PROGRESS, item);
      PopUpManager.removePopUp(_assignmentPopup);
   }

//   private function handleStorySelected(event : Event) : void {
      //      var story : BacklogItem = event.currentTarget.selectedItem as BacklogItem;
      //      var tiles : Array = view.tiles;
      //      for each(var cardView : CardView in tiles) {
      //         if(cardView.storyId == story.id) {
      //            cardView.setStyle("backgroundColor", "#eaa55b");
      //         }
      //         else {
      //            cardView.setStyle("backgroundColor", "#FFFFFF");
      //         }
      //      }
//   }

   public function handleAddTask(event : StoryClickEvent) : void {
      var story : BacklogItem = StoryView(event.target).story;
      var item : BacklogItem = new BacklogItem();
      item.parent = story;
      sendNotification(BacklogItemMediator.CREATE, item);
   }

   public function handleEditTask(event : StoryClickEvent) : void {
      var item : BacklogItem = CardView(event.target).story;
      sendNotification(BacklogItemMediator.EDIT, item);
   }

   public function handleEditStory(event : StoryClickEvent) : void {
      var item : BacklogItem = StoryView(event.target).story;
      sendNotification(BacklogItemMediator.EDIT, item);
   }

   public function droppedInOpen(event : DragEvent) : void {
      var item : BacklogItem = getDragItem(event);
      buildAndSendUpdateNotification(BacklogItem.STATUS_OPEN, item);
   }

   public function droppedInProgress(event : DragEvent) : void {
      var item : BacklogItem = getDragItem(event);
      if(!item.assignedTo) {
         _wallProxy.inProgresstem = item;
         PopUpManager.addPopUp(getAssignmentPopup(), view, true);
         PopUpManager.centerPopUp(_assignmentPopup);
      }
      else {
         buildAndSendUpdateNotification(BacklogItem.STATUS_PROGRESS, item);
      }
   }

   public function droppedInTest(event : DragEvent) : void {
      var item : BacklogItem = getDragItem(event);
      buildAndSendUpdateNotification(BacklogItem.STATUS_TEST, item);
   }

   public function droppedInDone(event : DragEvent) : void {
      var item : BacklogItem = getDragItem(event);
      buildAndSendUpdateNotification(BacklogItem.STATUS_DONE, item);
   }

   private function getDragItem(event : DragEvent) : BacklogItem {
      var items : Array = event.dragSource.dataForFormat("items") as Array;
      return items[0] as BacklogItem;
   }

   private function buildAndSendUpdateNotification(status : String, source : BacklogItem) : void {
      if (source.status != status) {
         var newItem : BacklogItem = new BacklogItem();
         newItem.copyFrom(source);
         newItem.status = status;
         source.status = status;
         if(status == BacklogItem.STATUS_DONE) {
            newItem.effort = 0;
         }
         sendNotification(ApplicationFacade.NOTE_UPDATE_BACKLOG_ITEM, newItem);
      }
   }

   override public function listNotificationInterests():Array {
      return [IterationLoadCommand.SUCCESS,
              ApplicationFacade.NOTE_LOAD_BACKLOG_COMPLETE,
              ApplicationFacade.NOTE_REMOVE_BACKLOG_ITEM,
              ProjectLoadCommand.SUCCESS,
              CardView.NOTE_IMPEDED,
              CardView.NOTE_UNIMPEDED,
              CardView.NOTE_UNASSIGN_TASK,
              CardView.NOTE_TAKE_TASK];
   }

   override public function handleNotification(notification:INotification):void {
      switch (notification.getName()) {
         case IterationLoadCommand.SUCCESS :
            loadIterationBacklog();
            break;
         case ApplicationFacade.NOTE_LOAD_BACKLOG_COMPLETE :
               if(_projectProxy.selectedIteration) {
                  _wallProxy.refreshCurrentBacklog(_projectProxy.selectedIteration.backlog);
               }
            break;
         case ApplicationFacade.NOTE_REMOVE_BACKLOG_ITEM :
            _wallProxy.removeBacklogItem(notification.getBody() as BacklogItem);
            break;
         case ProjectLoadCommand.SUCCESS :
            loadIterationBacklog();
            break;
         case CardView.NOTE_IMPEDED :
            var impeded : BacklogItem = BacklogItem(notification.getBody());
            var impediment : Issue = new Issue();
            var request : AddImpedimentRequest = new AddImpedimentRequest();
            request.impediment = impediment;
            request.backlogItemId = impeded.id;
            request.iterationId = _projectProxy.selectedIteration.id;
            sendNotification(ApplicationFacade.NOTE_CREATE_IMPEDIMENT, request);
            break;
         case CardView.NOTE_UNIMPEDED :
            var unimpeded : BacklogItem = BacklogItem(notification.getBody());
            sendNotification(ApplicationFacade.NOTE_REMOVE_IMPEDIMENT, unimpeded);
            break;
         case CardView.NOTE_TAKE_TASK :
            var taken : BacklogItem = BacklogItem(notification.getBody());
            taken.assignedTo = _secureContext.currentUser;
            sendNotification(ApplicationFacade.NOTE_UPDATE_BACKLOG_ITEM, taken);
            break;
         case CardView.NOTE_UNASSIGN_TASK :
            var unassigned : BacklogItem = BacklogItem(notification.getBody());
            unassigned.assignedTo = null;
            sendNotification(ApplicationFacade.NOTE_UPDATE_BACKLOG_ITEM, unassigned);
            break;
      }
   }

   private function loadIterationBacklog() : void {
      if (_projectProxy.selectedIteration != null && _projectProxy.selectedIteration.id != -1) {
         _wallProxy.currentBacklog = _projectProxy.selectedIteration.backlog;
      }
      else {
         _wallProxy.currentBacklog = new ArrayCollection();
      }
      if(_visible) {
         calculateNewHeight();
      }
   }

   public function get view() : WallView {
      return viewComponent as WallView;
   }

}
}