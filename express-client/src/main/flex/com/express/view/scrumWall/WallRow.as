package com.express.view.scrumWall {
import com.express.model.domain.BacklogItem;

import mx.containers.HBox;
import mx.controls.Spacer;
import mx.core.Container;
import mx.events.FlexEvent;

public class WallRow extends HBox {

   public static const CARD_WIDTH :int = 120;
   public static const CARD_HEIGHT :int = 108;

   public var header : Container;

   private var _story : BacklogItem;

   private var _storyCard : StoryCard;
   private var _openGrid : CardGrid;
   private var _progressGrid : CardGrid;
   private var _testGrid : CardGrid;
   private var _doneGrid : CardGrid;

   public function WallRow() {
      super();
      _storyCard = new StoryCard();
      this.addChild(_storyCard);
      var spacer : Spacer = new Spacer();
      spacer.width = 4;
      this.addChild(spacer);
      _openGrid = new CardGrid();
      _openGrid.gridStatus = BacklogItem.STATUS_OPEN;
      _openGrid.percentHeight = 100;
//      _openGrid.setStyle("backgroundColor", "#990000");
      this.addChild(_openGrid);

      spacer = new Spacer();
      spacer.width = 8;
      this.addChild(spacer);

      _progressGrid = new CardGrid();
      _progressGrid.gridStatus = BacklogItem.STATUS_PROGRESS;
      _progressGrid.percentHeight = 100;
//      _progressGrid.setStyle("backgroundColor", "#009900");
      this.addChild(_progressGrid);

      spacer = new Spacer();
      spacer.width = 8;
      this.addChild(spacer);

      _testGrid = new CardGrid();
      _testGrid.gridStatus = BacklogItem.STATUS_TEST;
      _testGrid.percentHeight = 100;
//      _testGrid.setStyle("backgroundColor", "#000099");
      this.addChild(_testGrid);

      spacer = new Spacer();
      spacer.width = 8;
      this.addChild(spacer);

      _doneGrid = new CardGrid();
      _doneGrid.gridStatus = BacklogItem.STATUS_DONE;
      _doneGrid.percentHeight = 100;
//      _doneGrid.setStyle("backgroundColor", "#009999");
      this.addChild(_doneGrid);

      this.addEventListener(FlexEvent.CREATION_COMPLETE, handleCreationComplete);
      this.minHeight = 100;
   }

   /**
    * When the header is resized all CardGrids need to lay themselves out again. We first set
    * the row height back to the minimum. It will then end up the height of the tallest CardGrid.
    * @param event
    */
   private function handleHeaderResize(event : FlexEvent):void {
      this.height = CARD_HEIGHT + 3;
      _openGrid.layoutCards();
      _progressGrid.layoutCards();
      _testGrid.layoutCards();
      _doneGrid.layoutCards();
   }

   private function handleCreationComplete(event : FlexEvent) : void {
      header.parent.addEventListener(FlexEvent.UPDATE_COMPLETE, handleHeaderResize);
   }

   public function setRowHeight(newHeight : int) : void {
      if(newHeight > this.height) {
         this.height = newHeight + 3;
      }
   }

   public function get story():BacklogItem {
      return _story;
   }

   public function set story(value:BacklogItem):void {
      _story = value;
      _storyCard.story = _story;
      _openGrid.story = _story;
      _progressGrid.story = _story;
      _testGrid.story = _story;
      _doneGrid.story = _story;
      sortTasks();
   }

   private function sortTasks() : void {
      for each (var task : BacklogItem in _story.tasks) {
         switch(task.status) {
            case BacklogItem.STATUS_OPEN :
               _openGrid.addTask(task);
               break;
            case BacklogItem.STATUS_PROGRESS :
               _progressGrid.addTask(task);
               break;
            case BacklogItem.STATUS_TEST :
               _testGrid.addTask(task);
               break;
            case BacklogItem.STATUS_DONE :
               _doneGrid.addTask(task);
               break;
         }
      }
   }

   public function set openWidth(value:int):void {
      _openGrid.width = value;
   }

   public function set progressWidth(value:int):void {
      _progressGrid.width = value;
   }

   public function set testWidth(value:int):void {
      _testGrid.width = value;
   }

   public function set doneWidth(value:int):void {
      _doneGrid.width = value;
   }
}
}