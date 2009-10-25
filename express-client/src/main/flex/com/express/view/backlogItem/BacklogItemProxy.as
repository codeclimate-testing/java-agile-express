package com.express.view.backlogItem
{
import com.express.model.domain.AcceptanceCriteria;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.User;
import com.express.model.request.CreateBacklogItemRequest;

import mx.collections.ArrayCollection;

import org.puremvc.as3.patterns.proxy.Proxy;

public class BacklogItemProxy extends Proxy
{
   public static const NAME : String = "BacklogItemProxy";

   public static const ACTION_ITEM_CREATE : int = 1;
   public static const ACTION_ITEM_EDIT : int = 2;
   public static const ACTION_ITEM_CHILD_CREATE : int = 3;
   public static const ACTION_ITEM_CHILD_EDIT : int = 4;

   public var createBacklogItemRequest : CreateBacklogItemRequest;
   public var selectedBacklogItem : BacklogItem;
   public var viewAction :int = 0;

   private var _assignToList : ArrayCollection;
   private var _statusList : ArrayCollection;

   public function BacklogItemProxy(proxyName:String = NAME, data:Object = null) {
      super(proxyName, data);
      _statusList = new ArrayCollection();
      _statusList.addItem(BacklogItem.STATUS_OPEN);
      _statusList.addItem(BacklogItem.STATUS_PROGRESS);
      _statusList.addItem(BacklogItem.STATUS_TEST);
      _statusList.addItem(BacklogItem.STATUS_DONE);
   }

   [Bindable]
   public function get assignToList() : ArrayCollection {
      return _assignToList;
   }

   public function get statusList() : ArrayCollection {
      return _statusList;
   }

   public function set assignToList(assignToList : ArrayCollection) : void {
      _assignToList.removeAll();
      for each(var user : User in assignToList) {
         _assignToList.addItem(user);
      }
   }

}
}