package com.express.model
{
import com.express.model.domain.BacklogItem;

import flash.utils.Dictionary;

import mx.collections.ArrayCollection;

import org.puremvc.as3.patterns.proxy.Proxy;

public class WallProxy extends Proxy
{
   public static const NAME : String = "WallProxy";

   public var inProgresstem : BacklogItem;
   private var _currentBacklog : ArrayCollection;
   private var _allTasks : ArrayCollection;
   private var _taskCollections : Dictionary;

   public function WallProxy() {
      super(NAME, null);
      _currentBacklog = new ArrayCollection();
      _allTasks = new ArrayCollection();
      _taskCollections = new Dictionary();
      _taskCollections[BacklogItem.STATUS_OPEN] = new ArrayCollection();
      _taskCollections[BacklogItem.STATUS_PROGRESS] = new ArrayCollection();
      _taskCollections[BacklogItem.STATUS_TEST] = new ArrayCollection();
      _taskCollections[BacklogItem.STATUS_DONE] = new ArrayCollection();
   }

   public function get currentBacklog() : ArrayCollection {
      return _currentBacklog;
   }

   public function set currentBacklog(backlog : ArrayCollection) : void {
      _currentBacklog.source = backlog.source.concat();
      setItemCollection(backlog);
   }

   public function refreshCurrentBacklog(backlog : ArrayCollection) : void {
      for each(var item : BacklogItem in backlog) {
         for each(var task : BacklogItem in item.tasks) {
            var existingTask : BacklogItem = findInTasks(task.id);
            if(existingTask != null) {
               if(existingTask.status != task.status) {
                  removeItemFromCollection(task.id, _taskCollections[existingTask.status]);
                  _taskCollections[task.status].addItem(existingTask);
               }
               existingTask.copyFrom(task);
            }
         }
         var existingStory : BacklogItem = findInBacklog(item.id);
         if(existingStory) {
            existingStory.copyFrom(item);
         }
      }
      addNewBacklogItem(backlog);
   }

   public function removeBacklogItem(item : BacklogItem) : void {
      if(item.parent == null) {
         removeItemFromCollection(item.id, _currentBacklog);
         for each(var task : BacklogItem in item.tasks) {
            removeItemFromCollection(task.id, _taskCollections[task.status]);
         }
      }
      else {
         removeItemFromCollection(item.id, _taskCollections[item.status]);
      }
   }

   public function get openItems() : ArrayCollection {
      return _taskCollections[BacklogItem.STATUS_OPEN];
   }

   public function get inProgressItems() : ArrayCollection {
      return _taskCollections[BacklogItem.STATUS_PROGRESS];
   }

   public function get testItems() : ArrayCollection {
      return _taskCollections[BacklogItem.STATUS_TEST];
   }

   public function get doneItems() : ArrayCollection {
      return _taskCollections[BacklogItem.STATUS_DONE];
   }

   private function setItemCollection(source : ArrayCollection) : void {
      _allTasks.source = [];
      for each(var array : ArrayCollection in _taskCollections) {
         array.source = [];
      }
      for each(var item : BacklogItem in source) {
         for each(var task : BacklogItem in item.tasks) {
            (_taskCollections[task.status] as ArrayCollection).addItem(task);
            _allTasks.addItem(task);
         }
      }
   }

   private function findInTasks(id : Number) : BacklogItem {
      for each( var task : BacklogItem in _allTasks) {
         if(task.id == id) {
            return task;
         }
      }
      return null;
   }

   private function findInBacklog(id : Number) : BacklogItem {
      for each( var item : BacklogItem in _currentBacklog) {
         if(item.id == id) {
            return item;
         }
      }
      return null;
   }

   private function removeItemFromCollection(id : Number, collection : ArrayCollection) : void {
      var index : int = 0;
      for each(var item : BacklogItem in collection) {
         if(id == item.id) {
            collection.removeItemAt(index);
            return;
         }
         index++;
      }
   }

   private function addNewBacklogItem(items : ArrayCollection) : void {
      if(items.length > _currentBacklog.length) {
         for each(var item : BacklogItem in items) {
            if(findInBacklog(item.id) == null) {
               _currentBacklog.addItem(item);
            }
         }
         return;
      }
      var tasks : Array = [];
      for each(item  in items) {
         for each(var task : BacklogItem in item.tasks) {
            tasks.push(task);
         }
      }
      if(tasks.length > _allTasks.length) {
         for each(task in tasks) {
            if(findInTasks(task.id) == null) {
               _allTasks.addItem(task);
               _taskCollections[task.status].addItem(task);
            }
         }
      }
   }

}
}