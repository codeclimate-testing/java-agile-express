package com.express.model
{
import com.express.model.domain.AccessRequest;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.EffortRecord;
import com.express.model.domain.Issue;
import com.express.model.domain.Iteration;
import com.express.model.domain.Project;

import com.express.model.domain.ProjectWorker;
import com.express.model.domain.User;

import mx.collections.ArrayCollection;
import mx.collections.HierarchicalData;

import org.puremvc.as3.patterns.proxy.Proxy;

public class ProjectProxy extends Proxy
{
   // Cannonical name of the Proxy
   public static const NAME:String = "ProjectProxy";

   public static const UNCOMMITED_TITLE : String = "Uncommited Backlog";
   public static const UNCOMMITED_ID :  Number = -1;
   public static const DEVELOPER : String = "Developer";
   public static const STORY : String = "Story";

   private var _projectList : ArrayCollection;
   private var _selectedProject : Project;

   private var _iterationList : ArrayCollection;
   private var _selectedIteration : Iteration;

   private var _selectedBacklog : HierarchicalData;
   private var _productBacklog : HierarchicalData;

   private var _developers : ArrayCollection;
   private var _accessRequests: ArrayCollection;
   private var _defectList : ArrayCollection;
   private var _burndown : ArrayCollection;
   private var _themes : ArrayCollection;

   public var selectedDefect : Issue;

   public var selectedBacklogItem : BacklogItem;
   public var selectedAccessRequest : AccessRequest;
   public var newIteration : Iteration;
   public var colourGroupings : ArrayCollection;
   public var productBacklogRequest : Boolean = false;

   public function ProjectProxy()
   {
      super(NAME, null);
      _projectList = new ArrayCollection();
      _iterationList = new ArrayCollection();
      _accessRequests = new ArrayCollection();
      _developers = new ArrayCollection();
      _burndown = new ArrayCollection();

      _selectedBacklog = new HierarchicalData();
      _selectedBacklog.childrenField = "tasks";

      _productBacklog = new HierarchicalData();
      _productBacklog.childrenField = "tasks";

      _defectList = new ArrayCollection();
      _themes = new ArrayCollection();

      colourGroupings = new ArrayCollection();
      colourGroupings.addItem(DEVELOPER);
      colourGroupings.addItem(STORY);
   }

   public function get projectList() : ArrayCollection {
      return _projectList;
   }

   public function set projectList(projectList : ArrayCollection) : void {
      _projectList.source = projectList.source;
   }

   public function addToProjectList(project : Project) : void {
      for each(var lstProject : Project in _projectList) {
         if(lstProject.id == project.id) {
            lstProject.copyFrom(project);
            return;
         }
      }
      _projectList.addItem(project);
   }

   public function get iterationList() : ArrayCollection {
      return _iterationList;
   }

   public function get defectList() : ArrayCollection {
      if (selectedProject == null) {
         return new ArrayCollection();
      }

      return selectedProject.defects;
   }

   public function set selectedIteration(iteration : Iteration) : void {
      _selectedIteration = iteration;
      if (iteration != null) {
         _selectedBacklog.source = iteration.backlog;
         setBurndown(iteration);
      }
      else {
         _selectedBacklog.source = [];
         _burndown.source = [];
      }
   }

   public function updateIterationList() : void {
      for each(var iteration : Iteration in _selectedProject.iterations) {
         var oldVersion : Iteration = getIteration(iteration.id);
         if (oldVersion) {
            oldVersion.copyFrom(iteration);
         }
         else {
            _iterationList.addItem(iteration);
         }
      }
   }

   private function getIteration(id : Number) : Iteration {
      for each(var iteration : Iteration in _iterationList) {
         if (iteration.id == id) {
            return iteration;
         }
      }
      return null;
   }

   public function get selectedIteration() : Iteration {
      return _selectedIteration;
   }

   [Bindable]
   public function get selectedProject() : Project {
      return _selectedProject;
   }

   public function set selectedProject(project : Project) : void {
      if(_selectedProject == null || project.id != _selectedProject.id) {
         _selectedProject = project;
         _iterationList.source = project.iterations == null ? [] : project.iterations.source;
      }
      else {
         _selectedProject.copyFrom(project);
         updateIterationList();

      }
      addToProjectList(project);
      accessRequests = project.accessRequests;
      setDevelopers(project.projectWorkers);
      setProductBacklogSource(project.productBacklog);
      themes = project.themes;
   }

   public function get selectedBacklog() : HierarchicalData {
      return _selectedBacklog;
   }

   private function setSelectedBacklogSource(backlog : ArrayCollection) : void {
      _selectedBacklog.source = backlog;
   }

   public function get productBacklog() : HierarchicalData {
      return _productBacklog;
   }

   public function setProductBacklogSource(backlog : ArrayCollection) : void {
      _productBacklog.source = backlog;
   }

   public function get accessRequests() : ArrayCollection {
      return _accessRequests;
   }

   public function set accessRequests(accessRequests : ArrayCollection) : void {
      _accessRequests.source = [];
      for each(var accessRequest : AccessRequest in accessRequests) {
         if(accessRequest.status == AccessRequest.UNRESOLVED) {
            _accessRequests.addItem(accessRequest);
         }
      }
   }

   public function get developers() : ArrayCollection {
      return _developers;
   }

   public function setDevelopers(projectWorkers : ArrayCollection) : void {
      var developers : Array = [];
      for each(var worker : ProjectWorker in projectWorkers) {
         developers.push(worker.worker);
      }
      _developers.source = developers;
   }

   public function getDeveloperIndex(developer : User) : int {
      if(developer == null) {
         return -1;
      }
      var index : int;
      for each(var dev : User in _developers) {
         if(dev.id == developer.id) {
            return index;
         }
         index++;
      }
      return -1;
   }

   public function get burndown() : ArrayCollection {
      return _burndown;
   }

   public function setBurndown(iteration : Iteration) : void {
      if(iteration) {
      _burndown.source = iteration.burndown.source.concat();
         if(iteration.getDaysRemaining() > 0) {
            var effort : EffortRecord = new EffortRecord();
            effort.date = new Date();
            effort.effort = iteration.getTaskHoursRemaining();
            _burndown.addItem(effort);
         }
      }
      else {
         _burndown.source = [];
      }
   }

   public function set themes(themes : ArrayCollection) : void {
      if(themes) {
         _themes.source = themes.source;
      }
   }

   public function get themes() : ArrayCollection {
      return _themes;
   }

   public function updateCurrentUser(user : User) : void {
      for each(var developer : User in _developers) {
         if(developer.id == user.id) {
            developer.copyFrom(user);
            return;
         }
      }
   }

}
}