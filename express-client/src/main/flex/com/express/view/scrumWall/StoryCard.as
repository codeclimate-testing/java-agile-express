package com.express.view.scrumWall {
import com.express.ApplicationFacade;
import com.express.model.domain.BacklogItem;
import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.components.PopUpLabel;

import flash.events.MouseEvent;
import flash.filters.DropShadowFilter;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.Text;
import mx.events.ListEvent;

public class StoryCard extends VBox {

   public static const NOTE_ADD_TASK:String = "Note.AddTAsk";
   public static const NOTE_MARK_DONE:String = "Note.MarkDone";

   private static const _ADD_TASK:String = "Add Task";
   private static const _MARK_DONE:String = "Mark as Done";

   private var _facade:ApplicationFacade;
   private var _quickMenu:ArrayCollection = new ArrayCollection();

   //Child visual components
   private var _refHeading:Label;
   private var _statusHeading:Label;
   private var _text:Text;
   private var _actionPopUp:PopUpLabel;

   [Bindable]
   public var _story:BacklogItem;

   public function StoryCard() {
      super();
      var headerBox:HBox = new HBox();
      headerBox.styleName = "storyCardHeaderBox";
      headerBox.percentWidth = 100;
      _refHeading = new Label();
      _refHeading.styleName = "h3";
      _refHeading.width = 75;
      headerBox.addChild(_refHeading);
      _statusHeading = new Label();
      _statusHeading.percentWidth = 100;
      headerBox.addChild(_statusHeading);
      _actionPopUp = new PopUpLabel();
      _actionPopUp.dataProvider = _quickMenu;
      _actionPopUp.styleName = "storyQuickMenu";
      _actionPopUp.addEventListener(ListEvent.ITEM_CLICK, handleQuickMenuSelection);
      headerBox.addChild(_actionPopUp);

      _text = new Text();
      _text.width = 205;
      _text.height = 80;
      _text.styleName = "storyText";

      this.addChild(headerBox);
      this.addChild(_text);
      this.width = 220;
      _facade = ApplicationFacade.getInstance();
      this.doubleClickEnabled = true;
      this.addEventListener(MouseEvent.DOUBLE_CLICK, handleDoubleClick);
      buildQuickMenu();
      addDropShadow();
   }

   private function handleDoubleClick(event:MouseEvent):void {
      _facade.sendNotification(BacklogItemMediator.EDIT, _story);
   }

   private function buildQuickMenu() : void {
      _quickMenu.source = [];
      _quickMenu.addItem(_ADD_TASK);
      _quickMenu.addItem(_MARK_DONE);
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

   private function handleQuickMenuSelection(event:ListEvent):void {
      switch (_quickMenu[event.rowIndex]) {
         case _ADD_TASK :
            _facade.sendNotification(NOTE_ADD_TASK, _story);
            break;
         case _MARK_DONE :
            _facade.sendNotification(NOTE_MARK_DONE, _story);
            break;
      }
   }

   public function set story(value:BacklogItem):void {
      if(_story && _story.id == value.id) {
         _story.copyFrom(value);
      }
      else {
         _story = value;
         BindingUtils.bindProperty(_refHeading, "text", value, "reference");
         BindingUtils.bindProperty(_statusHeading, "text", value, "status");
         BindingUtils.bindProperty(_text, "text", value, "summary");
         BindingUtils.bindProperty(_text, "toolTip", value, "summary");
      }
      if(_story.impediment) {
         this.styleName = "storyImpeded";
      }
      else {
         this.styleName = "";
      }

   }
}
}