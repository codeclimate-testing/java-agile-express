package com.express.model.domain
{
import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

[RemoteClass(alias="com.express.service.dto.IterationDto")]
public class Iteration
{
   public static const MILLIS_IN_A_DAY : int = 86400000;
   public function Iteration(id : Number = 0, title : String = null) {
      backlog = new ArrayCollection();
      burndown = new ArrayCollection();
      this.id = id;
      this.title = title;
   }

   [Bindable]
   public var id : Number;

   public var version : Number;

   public var title : String;

   public var description : String;

   [Bindable]
   public var startDate : Date;

   [Bindable]
   public var endDate : Date;

   public var finalVelocity : Number;

   public var project : Project;

   public var backlog : ArrayCollection;

   public var burndown : ArrayCollection;

   public function getPoints() : int {
      var total : Number = 0;
      for each(var item : BacklogItem in backlog) {
         total += item.effort;
      }
      return total;
   }

   public function getDaysRemaining() : int {
      var today: Date = new Date();
      if(today.getTime() >= endDate.getTime()) {
         return 0;
      }
      var millis : Number = today.getTime() < startDate.getTime() ?
                   startDate.getTime() : today.getTime();
      millis = endDate.getTime() - millis;
      var result : Number = millis / MILLIS_IN_A_DAY;
      if(Math.floor(result) < result) {
         result ++;
      }
      return result;
   }

   public function isOpen() : Boolean {
      var temp: Date = new Date();
      var today : Date = new Date(temp.fullYear, temp.month, temp.day);
      var start :Date = new Date(startDate.fullYear, startDate.month, startDate.day);
      var end : Date = new Date(endDate.fullYear, endDate.month, endDate.day);
      return today.getTime() <= end.getTime() && today.getTime() >= start.getTime();
   }

   public function getTaskHoursRemaining() : int {
      var total : Number = 0;
      for each(var item : BacklogItem in backlog) {
         for each (var task : BacklogItem in item.tasks) {
            total += task.effort;
         }
      }
      return total;
   }

   public function copyFrom(iteration : Iteration) : void {
      id = iteration.id;
      version = iteration.version;
      title = iteration.title;
      startDate = iteration.startDate;
      endDate = iteration.endDate;
      project = iteration.project;
      description = iteration.description;
      burndown = iteration.burndown;
      backlog = iteration.backlog;
      finalVelocity = iteration.finalVelocity;
   }
}
}