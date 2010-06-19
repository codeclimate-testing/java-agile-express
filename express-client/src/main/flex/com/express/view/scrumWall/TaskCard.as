package com.express.view.scrumWall {
import com.express.ApplicationFacade;
import com.express.model.SecureContextProxy;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.User;

import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.components.PopUpLabel;

import flash.events.MouseEvent;
import flash.filters.DropShadowFilter;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.Text;
import mx.core.DragSource;
import mx.events.ListEvent;
import mx.managers.DragManager;

public class TaskCard extends VBox {

   public static const NOTE_IMPEDED:String = "Note.Impeded";
   public static const NOTE_UNIMPEDED:String = "Note.Unimpeded";
   public static const NOTE_VIEW_IMPEDIMENT:String = "Note.ViewImpdeiment";
   public static const NOTE_TAKE_TASK:String = "Note.TakeTask";
   public static const NOTE_UNASSIGN_TASK:String = "Note.UnassignTask";

   private static const _IMPEDED:String = "Impeded";
   private static const _UNIMPEDED:String = "Unimpeded";
   private static const _VIEW_IMPEDIMENT:String = "View Impediment";
   private static const _TAKE_TASK:String = "Take Task";
   private static const _UNASSIGN:String = "Unassign";

   private static const _EFFORT_PREFIX : String = "Remaining hrs:";

   private var _facade:ApplicationFacade;
   private var _quickMenu:ArrayCollection = new ArrayCollection();
   private var _task:BacklogItem;
   private var _colour : int;

   //Child visual components
   private var _refHeading : Label;
   private var _actionPopUp:PopUpLabel;
   private var _text:Text;
   private var _dot : Box;
   private var _assignedToLabel : Label;
   private var _effortLabel : Label;

   public function TaskCard() {
      super();
      var headerBox:HBox = new HBox();
      headerBox.styleName = "storyCardHeaderBox";
      headerBox.percentWidth = 100;
      _refHeading = new Label();
      _refHeading.styleName = "cardHeading";
      _refHeading.width = 95;
      _actionPopUp = new PopUpLabel();
      _actionPopUp.dataProvider = _quickMenu;
      _actionPopUp.styleName = "storyQuickMenu";
      _actionPopUp.addEventListener(ListEvent.ITEM_CLICK, handleQuickMenuSelection);
      headerBox.addChild(_refHeading);
      headerBox.addChild(_actionPopUp);

      _text = new Text();
      _text.percentWidth = 100;
      _text.height = 48;
      _text.styleName = "cardSummaryText";

      var assignedBox : HBox = new HBox();
      assignedBox.percentWidth = 100;
      assignedBox.setStyle("horizontalGap",0);
      _dot = new Box();
      _dot.height = 13;
      _dot.width = 13;
      _dot.styleName = "cardColourDot";
      assignedBox.addChild(_dot);
      _assignedToLabel = new Label();
      _assignedToLabel.styleName = "cardText";
      assignedBox.addChild(_assignedToLabel);

      _effortLabel = new Label();
      _effortLabel.styleName = "cardText";

      this.addChild(headerBox);
      this.addChild(_text);
      this.addChild(assignedBox);
      this.addChild(_effortLabel);
      this.width = WallRow.CARD_WIDTH;
      this.height = WallRow.CARD_HEIGHT;
      this.horizontalScrollPolicy = "off";
      this.verticalScrollPolicy = "off";
      addDropShadow();
      this.addEventListener(MouseEvent.MOUSE_MOVE, handleDragStart);
      this.doubleClickEnabled = true;
      this.addEventListener(MouseEvent.DOUBLE_CLICK, handleDoubleClick);
   }

   private function handleDragStart(event:MouseEvent):void {
      var source : DragSource = new DragSource();
      source.addData(this, "taskCard");
      DragManager.doDrag(this,source,event);
   }

   private function handleDoubleClick(event:MouseEvent):void {
      _facade.sendNotification(BacklogItemMediator.EDIT, _task);
   }

   private function addDropShadow() : void {
      var newFilters : Array = [];
      var shadowFilter : DropShadowFilter = new DropShadowFilter();
      shadowFilter.angle = 45;
      shadowFilter.hideObject = false;
      shadowFilter.alpha = 0.4;
      newFilters.push(shadowFilter);
      this.filters = newFilters;
   }
   
   private function buildQuickMenu(obj:Object = null) : void {
      _quickMenu.source = [];
      if (!_facade) {
         _facade = ApplicationFacade.getInstance();
      }
      if (_task.impediment) {
         this.styleName = "taskImpeded";
         _quickMenu.addItem(_VIEW_IMPEDIMENT);
         _quickMenu.addItem(_UNIMPEDED);
      }
      else {
         this.styleName = "";
         _quickMenu.addItem(_IMPEDED);
      }
      if (_task.assignedTo) {
         _quickMenu.addItem(_UNASSIGN);
      }
      var currentUser:User = SecureContextProxy(_facade.retrieveProxy(SecureContextProxy.NAME)).currentUser;
      if (!(_task.assignedTo) || _task.assignedTo.id != currentUser.id) {
         _quickMenu.addItem(_TAKE_TASK);
      }
   }

   private function handleQuickMenuSelection(event:ListEvent):void {
         switch (_quickMenu[event.rowIndex]) {
            case _IMPEDED :
               _facade.sendNotification(NOTE_IMPEDED, _task);
               break;
            case _UNIMPEDED :
               _facade.sendNotification(NOTE_UNIMPEDED, _task);
               break;
            case _VIEW_IMPEDIMENT :
               _facade.sendNotification(NOTE_VIEW_IMPEDIMENT, _task);
               break;
            case _TAKE_TASK :
               _facade.sendNotification(NOTE_TAKE_TASK, _task);
               break;
            case _UNASSIGN :
               _facade.sendNotification(NOTE_UNASSIGN_TASK, _task);
               break;
         }
      }

   public function set task(value:BacklogItem):void {
      _task = value;
      BindingUtils.bindProperty(_refHeading, "text", value, "reference");
      BindingUtils.bindProperty(_text, "text", value, "summary");
      BindingUtils.bindProperty(_text, "toolTip", value, "summary");
      BindingUtils.bindProperty(_assignedToLabel, "text", value, "assignedToLabel");
      BindingUtils.bindProperty(_assignedToLabel, "text", value, "assignedToLabel");
      BindingUtils.bindSetter(buildQuickMenu, value, "impediment");
      BindingUtils.bindSetter(setColour, value, "colour");
      _dot.setStyle("borderColor", _colour);
      _dot.setStyle("backgroundColor", _colour);
      _effortLabel.text = _EFFORT_PREFIX + " " + _task.effort;
      buildQuickMenu();
   }

   private function setColour(value : Object = null) : void {
      _colour = _task.colour;
      _dot.setStyle("borderColor", _colour);
      _dot.setStyle("backgroundColor", _colour);
   }

   public function get task() : BacklogItem {
      return _task;
   }
}
}