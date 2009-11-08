package com.express.model.domain
{
import mx.collections.ArrayCollection;
import mx.collections.Sort;
import mx.collections.SortField;

[RemoteClass(alias="com.express.service.dto.IterationDto")]
public class Iteration
{
   private static const MILLIS_IN_A_DAY : int = 84400000;
   public function Iteration(id : Number = 0, title : String = null) {
      backlog = new ArrayCollection();
      _burndown = new ArrayCollection();
      var burndownSort : Sort = new Sort();
      burndownSort.fields = [new SortField("date")];
      _burndown.sort = burndownSort;
      this.id = id;
      this.title = title;
   }

   [Bindable]
   public var id : Number;

   public var version : Number;

   public var title : String;

   public var description : String;

   public var startDate : Date;

   public var endDate : Date;

   public var project : Project;

   public var backlog : ArrayCollection;

   public function get pointsCompleted() : int {
      return getPoints();
   }

   /**
    * A collection of EffortRecords for this iteration. Effort records should be naturally ordered
    * by date.
    */
   private var _burndown : ArrayCollection;

   public function get burndown() : ArrayCollection {
      _burndown.refresh();
      return _burndown;
   }

   public function set burndown(burndown : ArrayCollection) : void {
      _burndown.removeAll();
      for each(var effort : EffortRecord in burndown) {
         _burndown.addItem(effort);
      }
   }

   public function addEffortRecord(record : EffortRecord) : void {
      _burndown.addItem(record);
   }

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
      backlog.source = iteration.backlog.source;
   }
}
}