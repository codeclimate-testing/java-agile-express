package com.express.view.components {
import flash.events.Event;
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.collections.ICollectionView;
import mx.collections.IList;
import mx.collections.ListCollectionView;
import mx.collections.XMLListCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Image;
import mx.controls.Label;
import mx.controls.List;
import mx.core.Application;
import mx.events.FlexEvent;
import mx.events.ListEvent;

public class PopUpLabel extends HBox{

   public var _dataProvider : ICollectionView;

   private var _labelText : String;

   [Bindable]
   [Embed(source="/images/icons/down.png")]
   public var popUpIcon : Class;

   [Bindable]
   public var labelStyleName : String;

   [Bindable]
   public var labelFunction : Function ;

   [Bindable]
   public var selectedIndex : int;

   [Bindable]
   public var leftIcon : Class;

   private var _popup : VBox;
   private var _rowHeight : int = 18;
   private var _img : Image;
   private var _label : Label;
   private var _list : List;
   private var _leftImg : Image;

   public function PopUpLabel() {
      _leftImg = new Image();
      this.addChild(_leftImg);
      _label = new Label();
      this.addChild(_label);
      _img = new Image();
      this.addChild(_img);
      _img.addEventListener(MouseEvent.CLICK, popup);
      this.addEventListener(FlexEvent.CREATION_COMPLETE, handleCreationComplete);
   }

   private function handleCreationComplete(event : Event) : void {
      _label.text = _labelText;
      _label.styleName = labelStyleName;
      if(leftIcon) {
         _leftImg.source = leftIcon;
      }
      _img.source = popUpIcon;
      _img.buttonMode = true;
      _img.useHandCursor = true;

      _popup = new VBox();
      createPopUpList();
      _popup.addChild(_list);
      _popup.verticalScrollPolicy = "off";
      _popup.horizontalScrollPolicy = "off";
      _popup.height = (_dataProvider.length + 1) * _rowHeight;
      _popup.includeInLayout = false;
      _popup.visible = false;
      Application.application.addChild(_popup);

   }

   private function createPopUpList() : void {
      _list = new List();
      _list.dataProvider = _dataProvider;
      if(labelFunction != null) {
         _list.labelFunction = labelFunction;
      }
      _list.setStyle("textAlign", "left");
      _list.addEventListener(ListEvent.ITEM_CLICK, handleEventListItemClick);
      _list.setStyle("fontSize", 14);
      _list.setStyle("fontWeight", "normal");
      _list.variableRowHeight = true;
      _list.height = (_dataProvider.length + 1) * _rowHeight;
      selectedIndex = -1;
   }

   private function handleEventListItemClick(event : ListEvent) : void {
      selectedIndex = event.rowIndex;
      this.dispatchEvent(event);
      _popup.visible = false;
   }

   private function popup(event : MouseEvent) : void {
      if (!_popup.visible) {
         var newX :int = event.stageX;
         if(newX + _popup.width > Application.application.width) {
            newX = newX - _popup.width;
         }
         _popup.x = newX;
         _popup.y = event.stageY;
         _popup.visible = true;
         _list.selectedIndex = -1;
         event.stopImmediatePropagation();
         Application.application.stage.addEventListener(MouseEvent.CLICK, handleMouseClick);
      }
   }

   private function handleMouseClick(event : MouseEvent) : void {
      if (_popup.visible) {
         _popup.visible = false;
         Application.application.stage.removeEventListener(MouseEvent.CLICK, handleMouseClick);
      }
   }

   public function set dataProvider(value : Object) : void {
      if (value is Array) {
         _dataProvider = new ArrayCollection(value as Array);
      }
      else if (value is ICollectionView) {
         _dataProvider = ICollectionView(value);
      }
      else if (value is IList) {
            _dataProvider = new ListCollectionView(IList(value));
         }
         else if (value is XMLList) {
               _dataProvider = new XMLListCollection(value as XMLList);
            }
            else {
               // convert it to an array containing this one item
               var tmp : Array = [value];
               _dataProvider = new ArrayCollection(tmp);
            }
   }

   [Bindable]
   public function get labelText():String {
      return _labelText;
   }

   public function set labelText(value:String):void {
      _labelText = value;
      if(_label) {
         _label.text = _labelText;
      }
   }
}
}