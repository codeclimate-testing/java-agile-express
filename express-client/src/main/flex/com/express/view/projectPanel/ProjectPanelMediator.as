package com.express.view.projectPanel {
import com.express.ApplicationFacade;
import com.express.controller.IterationLoadCommand;
import com.express.controller.IterationUpdateCommand;
import com.express.controller.ProjectLoadCommand;
import com.express.model.ProjectProxy;
import com.express.model.SecureContextProxy;
import com.express.model.domain.BacklogItem;
import com.express.model.domain.Project;
import com.express.navigation.MenuItem;
import com.express.print.BacklogPrintView;
import com.express.view.ApplicationMediator;
import com.express.view.iteration.IterationMediator;
import com.express.view.projectDetails.ProjectDetailsMediator;
import com.express.view.renderer.CardPrintRenderer;

import flash.events.Event;
import flash.events.HTTPStatusEvent;
import flash.events.IOErrorEvent;
import flash.events.MouseEvent;

import flash.events.ProgressEvent;
import flash.events.SecurityErrorEvent;
import flash.net.FileReference;

import flash.net.URLRequest;

import mx.controls.ComboBox;
import mx.controls.DateField;
import mx.controls.TileList;
import mx.events.FlexEvent;
import mx.events.ListEvent;
import mx.messaging.config.ServerConfig;
import mx.printing.FlexPrintJob;
import mx.printing.FlexPrintJobScaleType;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class ProjectPanelMediator extends Mediator{
   public static const NAME : String = "ProjectPanelMediator";

   public static const SHOW_PRINT_PREVIEW : String = "Note.showPrintPreview";

   private var _proxy : ProjectProxy;
   private var _secureContext : SecureContextProxy;
   private var _printView : BacklogPrintView;
   private var _backlog : Array;
   private var _fileRef : FileReference;

   public function ProjectPanelMediator(viewComp : ProjectPanel, mediatorName : String = NAME) {
      super(mediatorName, viewComp);
      _proxy = ProjectProxy(facade.retrieveProxy(ProjectProxy.NAME));
      _secureContext = SecureContextProxy(facade.retrieveProxy(SecureContextProxy.NAME));
      view.iterationSummary.burndown.dataProvider = _proxy.burndown;
      bindDisplay();
      viewComp.cboProjects.dataProvider = _proxy.projectList;
      viewComp.cboProjects.addEventListener(Event.CHANGE, handleProjectSelected);
      viewComp.lnkProjectAccess.addEventListener(MouseEvent.CLICK, handleNavigateRequest);
      viewComp.lnkProjectAccess.data = new MenuItem(viewComp.lnkProjectAccess.label,ApplicationMediator.ACCESS_VIEW, null);
      viewComp.projectDisplay.auth.userRoles = _secureContext.availableRoles;
      viewComp.iterationSummary.auth.userRoles = _secureContext.availableRoles;
      viewComp.iterationSummary.printPopUp.addEventListener(ListEvent.ITEM_CLICK, handlePrintMenuSelection);
      viewComp.projectDisplay.managePopUp.addEventListener(ListEvent.ITEM_CLICK, handleManageMenuSelection);
      viewComp.projectDisplay.btnEdit.addEventListener(MouseEvent.CLICK, handleEditProject);
      viewComp.iterationSummary.btnEdit.addEventListener(MouseEvent.CLICK, handleEditIteration);
      viewComp.projectDisplay.lnkVelocity.addEventListener(MouseEvent.CLICK, handleDisplayVelocityChart);

      viewComp.iterationSummary.lnkExport.addEventListener(MouseEvent.CLICK, handleIterationBacklogExport);
      viewComp.projectDisplay.lnkExport.addEventListener(MouseEvent.CLICK, handleProductBacklogExport);
      _fileRef = new FileReference();
      _fileRef.addEventListener(Event.CANCEL, handleDownload);
      _fileRef.addEventListener(Event.COMPLETE, handleDownload);
      _fileRef.addEventListener(Event.OPEN, handleDownload);
      _fileRef.addEventListener(Event.SELECT, handleDownload);
      _fileRef.addEventListener(HTTPStatusEvent.HTTP_STATUS, handleDownload);
      _fileRef.addEventListener(IOErrorEvent.IO_ERROR, handleDownload);
      _fileRef.addEventListener(ProgressEvent.PROGRESS, handleDownload);
      _fileRef.addEventListener(SecurityErrorEvent.SECURITY_ERROR, handleDownload);
   }

   private function handleDownload(evt:Event):void {
      /* Create shortcut to the FileReference object. */
      var fr:FileReference = evt.currentTarget as FileReference;
   }

   private function handleIterationBacklogExport(event : Event) : void {
      var url : String = ServerConfig.getChannel("my-amf").endpoint;
      url = url.substr(0,url.length - 18);
      url += "/iteration/" + _proxy.selectedIteration.id + "/backlog";
      var request : URLRequest = new URLRequest(url);
      _fileRef.download(request);
   }

   private function handleProductBacklogExport(event : Event) : void {
      var url : String = ServerConfig.getChannel("my-amf").endpoint;
      url = url.substr(0,url.length - 18);
      url += "/project/" + _proxy.selectedProject.id + "/backlog";
      var request : URLRequest = new URLRequest(url);
      _fileRef.download(request);
   }

   public function handleProjectSelected(event : Event) : void {
      var project : Project = (event.target as ComboBox).selectedItem as Project;
      _proxy.selectedIteration = null;
      if (project != null) {
         _proxy.selectedProject = project;
         sendNotification(ApplicationFacade.NOTE_LOAD_PROJECT, project.id);
      }
   }

   public function handleNavigateRequest(event : MouseEvent) : void {
      facade.sendNotification(ApplicationFacade.NOTE_NAVIGATE,
            new MenuItem(ApplicationMediator.ACCESS_HEAD, ApplicationMediator.ACCESS_VIEW, null));
   }

   private function handlePrintMenuSelection(event : ListEvent) : void {
      switch(event.rowIndex) {
         case 0 :
            printPreviewRequest(_proxy.selectedIteration.backlog.source);
            break;
         case 1 :
            var tasks : Array = [];
            for each(var item : BacklogItem in _proxy.selectedIteration.backlog) {
               for each(var task : BacklogItem in item.tasks) {
                  tasks.push(task);
               }
            }
            printPreviewRequest(tasks);
      }
   }

   private function handleManageMenuSelection(event : ListEvent) : void {
      switch(event.rowIndex) {
         case 0 :
            event.stopImmediatePropagation();
            sendNotification(ApplicationFacade.NOTE_PROJECT_ACCESS_MANAGE);
            break;
         case 1 :
            event.stopImmediatePropagation();
            sendNotification(ApplicationFacade.NOTE_THEMES_MANAGE);
            break;
      }
   }

   override public function listNotificationInterests():Array {
      return [ProjectLoadCommand.SUCCESS,
              IterationUpdateCommand.SUCCESS,
              ApplicationFacade.NOTE_LOAD_BACKLOG_COMPLETE,
              IterationLoadCommand.SUCCESS];
   }

   override public function handleNotification(notification : INotification):void {
      switch(notification.getName()) {
         case ProjectLoadCommand.SUCCESS :
            view.cboProjects.dataProvider.refresh();
            bindDisplay();
            bindIterationDisplay();
            toggleBurndownAsLink();
            break;
         case IterationUpdateCommand.SUCCESS :
            bindIterationDisplay();
            break;
         case IterationLoadCommand.SUCCESS :
            bindIterationDisplay();
            toggleBurndownAsLink();
            break;
         case ApplicationFacade.NOTE_LOAD_BACKLOG_COMPLETE :
            bindIterationDisplay();
            break;
      }
   }

   private function toggleBurndownAsLink() : void {
      view.iterationSummary.burndown.chart.useHandCursor = _proxy.selectedIteration != null;
      view.iterationSummary.burndown.chart.buttonMode = _proxy.selectedIteration != null;
      view.iterationSummary.burndown.chart.mouseChildren = _proxy.selectedIteration == null;
      if(_proxy.selectedIteration) {
         view.iterationSummary.burndown.chart.addEventListener(MouseEvent.CLICK, handleDisplayBurndown);
      }
      else {
         view.iterationSummary.burndown.chart.removeEventListener(MouseEvent.CLICK, handleDisplayBurndown);
      }
   }

   private function handleEditProject(event : Event) : void {
      sendNotification(ProjectDetailsMediator.EDIT);
   }

   private function handleEditIteration(event : Event) : void {
      _proxy.newIteration = _proxy.selectedIteration;
      sendNotification(IterationMediator.EDIT);
   }

   private function handleDisplayBurndown(event : Event) : void {
      sendNotification(ApplicationFacade.NOTE_DISPLAY_BURNDOWN, view.iterationSummary.burndown.chkWeekends);
   }

   private function handleDisplayVelocityChart(event : Event) : void {
      sendNotification(ApplicationFacade.NOTE_DISPLAY_VELOCITY, _proxy.selectedProject.iterations);
   }

   public function get view() : ProjectPanel {
      return viewComponent as ProjectPanel;
   }

   private function bindDisplay() : void {
      if(_proxy.selectedProject) {
         view.projectDisplay.reference.text = _proxy.selectedProject.reference;
         view.projectDisplay.effortUnit.text = _proxy.selectedProject.effortUnit;
         view.projectDisplay.startDate.text = DateField.dateToString(_proxy.selectedProject.startDate, "DD/MM/YYYY");
         view.projectDisplay.description.text = _proxy.selectedProject.description;
         view.projectDisplay.rptAdmins.dataProvider = _proxy.selectedProject.admins;
         view.projectDisplay.lnkVelocity.visible = true;
         view.projectDisplay.btnEdit.enabled = true;
         view.projectDisplay.managePopUp.enabled = true;
         view.projectDisplay.lnkExport.enabled = true;
      }
   }

   public function bindIterationDisplay() : void {
      if(_proxy.selectedIteration) {
         view.iterationSummary.startDate.text = DateField.dateToString(_proxy.selectedIteration.startDate, "DD/MM/YYYY");
         view.iterationSummary.endDate.text = DateField.dateToString(_proxy.selectedIteration.endDate, "DD/MM/YYYY");
         view.iterationSummary.iterationStatus.text = _proxy.selectedIteration.isOpen() ? "open" : "closed";
         view.iterationSummary.totalPoints.text = "" + _proxy.selectedIteration.getPoints();
         view.iterationSummary.hrsRemaining.text = "" + _proxy.selectedIteration.getTaskHoursRemaining();
         view.iterationSummary.daysRemaining.text = "" + _proxy.selectedIteration.getDaysRemaining();
         view.iterationSummary.btnEdit.enabled = true;
         view.iterationSummary.printPopUp.enabled = true;
         view.iterationSummary.lnkExport.enabled = true;
         view.iterationSummary.burndown.chkWeekends.enabled = true;
         view.iterationSummary.burndown.xAxis.minimum = _proxy.selectedIteration.startDate;
         view.iterationSummary.burndown.xAxis.maximum = _proxy.selectedIteration.endDate;
      }
      else {
         view.iterationSummary.startDate.text = "";
         view.iterationSummary.endDate.text = "";
         view.iterationSummary.iterationStatus.text = "";
         view.iterationSummary.totalPoints.text = "";
         view.iterationSummary.hrsRemaining.text = "";
         view.iterationSummary.daysRemaining.text = "";
         view.iterationSummary.btnEdit.enabled = false;
         view.iterationSummary.printPopUp.enabled = false;
         view.iterationSummary.lnkExport.enabled = false;
         view.iterationSummary.burndown.chkWeekends.enabled = false;
         view.iterationSummary.burndown.xAxis.minimum = null;
         view.iterationSummary.burndown.xAxis.maximum = null;
      }
   }

   private function printPreviewRequest(backlog : Array) : void {
      _printView = new BacklogPrintView();
      _backlog = backlog;
      _printView.addEventListener(FlexEvent.CREATION_COMPLETE, handlePrintPreviewCreated);
      sendNotification(SHOW_PRINT_PREVIEW, _printView);
   }

   private function handlePrintPreviewCreated(event : FlexEvent) : void {
      _printView.lnkPrint.addEventListener(MouseEvent.CLICK, handlePrintRequest);
      var length : int = _backlog.length;
      var tileList : TileList = createTileList(0);
      var dataProvider : Array = [];
      for(var index : int = 4; index <= length; index += 4) {
         dataProvider = _backlog.slice(index - 4, index);
         tileList = createTileList(index / 4);
         tileList.dataProvider = dataProvider;
         _printView.pages.addChild(tileList);
      }
      if(length % 4 != 0) {
         dataProvider = _backlog.slice(index - 4, length );
         tileList = createTileList(index / 4);
         tileList.dataProvider = dataProvider;
         _printView.pages.addChild(tileList);
      }
   }

   private function createTileList(index : int) : TileList{
      var tileList : TileList = new TileList();
      tileList.id = "page_" + index;
      tileList.columnCount = 2;
      tileList.height = 550;
      tileList.width = 830;
      tileList.columnWidth = 380;
      tileList.styleName = "page";
      tileList.verticalScrollPolicy = "off";
      tileList.itemRenderer = new CardPrintRenderer();
      return tileList;
   }

   private function handlePrintRequest(event : Event) : void {
      var printJob:FlexPrintJob = new FlexPrintJob();
      if (printJob.start()) {
         for each(var tileList : TileList in _printView.pages.getChildren()) {
            printJob.addObject(tileList, FlexPrintJobScaleType.MATCH_WIDTH);
         }
         printJob.send();
      }
      _printView.parent.visible = false;
   }
}
}