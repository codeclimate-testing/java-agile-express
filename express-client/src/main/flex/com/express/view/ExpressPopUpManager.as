package com.express.view {
import com.express.model.SecureContextProxy;
import com.express.model.domain.WindowMetrics;
import com.express.print.BacklogPrintView;
import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.backlogItem.BacklogItemView;
import com.express.view.components.BurndownChart;
import com.express.view.components.VelocityChart;
import com.express.view.iteration.IterationForm;
import com.express.view.iteration.IterationMediator;
import com.express.view.projectDetails.ProjectDetailsForm;
import com.express.view.projectDetails.ProjectDetailsMediator;
import com.express.view.projectWorkers.ProjectAdmin;
import com.express.view.projectWorkers.ProjectAdminMediator;
import com.express.view.themes.ThemesForm;
import com.express.view.themes.ThemesMediator;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.core.UIComponent;
import mx.events.CloseEvent;
import mx.events.FlexEvent;

import org.puremvc.as3.interfaces.IFacade;
import org.puremvc.as3.interfaces.INotification;

public class ExpressPopUpManager {
   
   private var _backlogItemView : BacklogItemView;
   private var _iterationForm : IterationForm;
   private var _projectForm : ProjectDetailsForm;
   private var _projectAdminForm : ProjectAdmin;
   private var _themesForm : ThemesForm;
   private var _burndownChart : BurndownChart;
   private var _velocityChart : VelocityChart;
   private var _lastWindowNotification : INotification;
   
   private var _facade : IFacade;
   private var _application : Express;
   private var _secureContext : SecureContextProxy;
   
   public function ExpressPopUpManager(facade : IFacade, application : Express) {
      _facade = facade;
      _application = application;
      _secureContext = SecureContextProxy(_facade.retrieveProxy(SecureContextProxy.NAME));
   }
   
   private function createBacklogItemForm() : void {
      _backlogItemView = new BacklogItemView();
      _backlogItemView.addEventListener(FlexEvent.CREATION_COMPLETE, handleBacklogItemFormCreated);
   }

   private function createProjectAdmin() : void {
      _projectAdminForm = new ProjectAdmin();
      _projectAdminForm.addEventListener(FlexEvent.CREATION_COMPLETE, handleProjectAdminCreated);
   }

   private function createThemesForm() : void {
      _themesForm = new ThemesForm();
      _themesForm.addEventListener(FlexEvent.CREATION_COMPLETE, handleThemesFormCreated);
   }

   private function createIterationForm() : void {
      _iterationForm = new IterationForm();
      _iterationForm.addEventListener(FlexEvent.CREATION_COMPLETE, handleIterationFormCreated);
   }

   private function createProjectForm() : void {
      _projectForm = new ProjectDetailsForm();
      _projectForm.addEventListener(FlexEvent.CREATION_COMPLETE, handleProjectdetailsFormCreated);
   }

   private function createBurndownChart() : void {
      _burndownChart = new BurndownChart();
   }
   
   private function createVelocityChart() : void {
      _velocityChart = new VelocityChart();
   }
   
   public function handleThemesFormCreated(event : Event) : void {
      _facade.registerMediator(new ThemesMediator(_themesForm));
   }

   private function handleBacklogItemFormCreated(event : FlexEvent) : void {
      var mediator : BacklogItemMediator = new BacklogItemMediator(_backlogItemView);
      _facade.registerMediator(mediator);
      mediator.handleNotification(_lastWindowNotification);
   }

   private function handleProjectAdminCreated(event : FlexEvent) : void {
      _facade.registerMediator(new ProjectAdminMediator(_projectAdminForm));
   }

   private function handleIterationFormCreated(event : FlexEvent) : void {
      var mediator : IterationMediator = new IterationMediator(_iterationForm);
      _facade.registerMediator(mediator);
      mediator.handleNotification(_lastWindowNotification);
   }

   private function handleProjectdetailsFormCreated(event : FlexEvent) : void {
      var mediator : ProjectDetailsMediator = new ProjectDetailsMediator(_projectForm, "MainProjectDetailsMediator");
      _facade.registerMediator(mediator);
      mediator.handleNotification(_lastWindowNotification);
   }

   public function showPrintPreview(previewPanel : BacklogPrintView) : void {
      replaceTitleWindowChildWith(previewPanel);
      _application.mainPopup.title = "Print Preview - Print in landscape on A4";
      _application.mainPopup.width = 825;
      _application.mainPopup.height = 500;
      _application.mainPopup.x = (_application.width / 2) - 405;
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }
   
   public function showProjectAdminWindow(notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_projectAdminForm) {
         createProjectAdmin();
      }
      replaceTitleWindowChildWith(_projectAdminForm);
      _application.mainPopup.title = "Project Access Requests";
      _application.mainPopup.width = 550;
      _application.mainPopup.height = 450;
      _application.mainPopup.x = (_application.width / 2) - 225;
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }

   public function showThemesWindow(notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_themesForm) {
         createThemesForm();
      }
      replaceTitleWindowChildWith(_themesForm);
      _application.mainPopup.title = "Project Themes";
      _application.mainPopup.width = 450;
      _application.mainPopup.height = 410;
      _application.mainPopup.x = (_application.width / 2) - 225;
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }

   public function showBurndownWindow(title : String, notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_burndownChart) {
         createBurndownChart();
      }
      _burndownChart.dataProvider = notification.getBody();
      replaceTitleWindowChildWith(_burndownChart);
      _application.mainPopup.title = title;
      _application.mainPopup.width = 600;
      _application.mainPopup.height = 450;
      _application.mainPopup.x = (_application.width / 2) - (_application.mainPopup.width / 2);
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }

   public function showVelocityWindow(title : String, notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_velocityChart) {
         createVelocityChart();
      }
      _velocityChart.dataProvider = notification.getBody();
      replaceTitleWindowChildWith(_velocityChart);
      _application.mainPopup.title = title;
      _application.mainPopup.width = 600;
      _application.mainPopup.height = 450;
      _application.mainPopup.x = (_application.width / 2) - (_application.mainPopup.width / 2);
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }
   
   public function showBacklogWindow(notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_backlogItemView) {
         createBacklogItemForm();
      }
      replaceTitleWindowChildWith(_backlogItemView);
      var metrics : WindowMetrics = _secureContext.currentUser.storyWindowPreference;
      if(!metrics) {
         metrics = new WindowMetrics();
         metrics.x = (_application.width / 2) - 450;
         metrics.y = 80;
         metrics.width = 900;
         metrics.height = 560;
         _secureContext.currentUser.storyWindowPreference = metrics;
      }
      _application.mainPopup.width = metrics.width;
      _application.mainPopup.height = metrics.height;
      _application.mainPopup.x = metrics.x;
      _application.mainPopup.y = metrics.y;
      _application.mainPopup.visible = true;
   }

   public function showIterationWindow(title : String, notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_iterationForm) {
         createIterationForm();
      }
      replaceTitleWindowChildWith(_iterationForm);
      _application.mainPopup.title = title;
      _application.mainPopup.width = 450;
      _application.mainPopup.height = 410;
      _application.mainPopup.x = (_application.width / 2) - 225;
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }

   public function showProjectWindow(title : String, notification : INotification) : void {
      _lastWindowNotification = notification;
      if(!_projectForm) {
         createProjectForm();
      }
      replaceTitleWindowChildWith(_projectForm);
      _application.mainPopup.title = title;
      _application.mainPopup.width = 450;
      _application.mainPopup.height = 410;
      _application.mainPopup.x = (_application.width / 2) - 225;
      _application.mainPopup.y = 80;
      _application.mainPopup.visible = true;
   }

   public function showConfirm(title : String, message : String, callback:Function = null) : void {
      _application.confirmBox.title = title;
      _application.confirmBox.message.text = message;
      _application.confirmBox.x = (_application.width / 2) - 225;
      _application.confirmBox.y = 80;
      _application.confirmBox.visible = true;
      if(callback != null) {
         _application.confirmBox.btnAccept.addEventListener(CloseEvent.CLOSE, callback);
      }
   }

   private function replaceTitleWindowChildWith(component : UIComponent) : void {
      if(_application.mainPopup.getChildren().length > 0) {
         _application.mainPopup.removeChildAt(0);
      }
      _application.mainPopup.addChild(component);
   }
}
}