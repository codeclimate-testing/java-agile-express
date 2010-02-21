package com.express.view
{
import com.express.ApplicationFacade;
import com.express.controller.LoginCommand;
import com.express.controller.ProjectLoadCommand;
import com.express.controller.RegisterConfirmCommand;
import com.express.controller.UpdateUserCommand;
import com.express.model.ProjectProxy;
import com.express.model.SecureContextProxy;
import com.express.model.WallProxy;
import com.express.model.domain.User;
import com.express.navigation.MenuItem;
import com.express.print.BacklogPrintView;
import com.express.service.ServiceRegistry;
import com.express.view.backlog.BacklogMediator;
import com.express.view.backlogItem.BacklogItemMediator;
import com.express.view.iteration.IterationMediator;
import com.express.view.login.LoginMediator;
import com.express.view.login.LoginView;
import com.express.view.profile.ProfileMediator;
import com.express.view.projectAccess.ProjectAccessMediator;
import com.express.view.projectDetails.ProjectDetailsMediator;
import com.express.view.projectPanel.ProjectPanelMediator;
import com.express.view.register.RegisterMediator;
import com.express.view.scrumWall.ScrumWallMediator;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.LinkButton;
import mx.events.FlexEvent;
import mx.managers.PopUpManager;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class ApplicationMediator extends Mediator {
   // Canonical name of the Mediator
   public static const NAME:String = "ApplicationMediator";

   public static const LOGIN_VIEW:uint = 0;
   public static const BACKLOG_VIEW:int = 1;
   public static const WALL_VIEW:int = 2;
   public static const PROFILE_VIEW:uint = 3;
   public static const ACCESS_VIEW:uint = 4;
   public static const REGISTER_VIEW:uint = 5;

   public static const LOGIN_HEAD:String = "Login";
   public static const PROJECT_HEAD:String = "Project";
   public static const ACCESS_HEAD:String = "Project Access";
   public static const PROFILE_HEAD:String = "User Profile";
   public static const REGISTER_HEAD:String = "User Registration";
   public static const REGISTER_CONFIRM_HEAD:String = "Registration Confirmation";

   private var _loginView:LoginView;
   private var _secureContext:SecureContextProxy;
   private var _projectProxy:ProjectProxy;
   private var _popupManager:ExpressPopUpManager;


   public function ApplicationMediator(viewComp:Express) {
      super(NAME, viewComp);

      _secureContext = SecureContextProxy(facade.retrieveProxy(SecureContextProxy.NAME));
      _projectProxy = ProjectProxy(facade.retrieveProxy(ProjectProxy.NAME));
      _popupManager = new ExpressPopUpManager(facade, viewComp);
      facade.registerMediator(new LoginMediator(viewComp.loginView));

      viewComp.rptMenu.dataProvider = _secureContext.menu;

      // Add listeners for other components on the viewStack with delayed instantiation.
      viewComp.backlogView.addEventListener(FlexEvent.CREATION_COMPLETE, handleBacklogViewCreated);
      viewComp.wallView.addEventListener(FlexEvent.CREATION_COMPLETE, handleWallViewCreated);
      viewComp.projectAccessForm.addEventListener(FlexEvent.CREATION_COMPLETE, handleProjectAccessFormCreated);
      viewComp.profileView.addEventListener(FlexEvent.CREATION_COMPLETE, handleProfileViewCreated);
      viewComp.registerView.addEventListener(FlexEvent.CREATION_COMPLETE, handleRegisterViewCreated);
      viewComp.menu.addEventListener(MouseEvent.CLICK, handleNavigateRequest);

      viewComp.btnLogout.addEventListener(MouseEvent.CLICK, handleLogout);
   }

   private function handleLogout(event:Event):void {
      logout();
   }


   override public function listNotificationInterests():Array {
      return [ApplicationFacade.NOTE_DISPLAY_BURNDOWN,
         ApplicationFacade.NOTE_DISPLAY_VELOCITY,
         ApplicationFacade.NOTE_NAVIGATE,
         ApplicationFacade.NOTE_SHOW_ERROR_MSG,
         ApplicationFacade.NOTE_SHOW_SUCCESS_MSG,
         ApplicationFacade.NOTE_CLEAR_MSG,
         ApplicationFacade.NOTE_PROJECT_ACCESS_MANAGE,
         ApplicationFacade.NOTE_THEMES_MANAGE,
         ApplicationFacade.NOTE_CREATE_IMPEDIMENT,
         ApplicationFacade.NOTE_EDIT_IMPEDIMENT,
         LoginCommand.SUCCESS,
         BacklogItemMediator.CREATE,
         BacklogItemMediator.EDIT,
         ProjectPanelMediator.SHOW_PRINT_PREVIEW,
         UpdateUserCommand.SUCCESS,
         RegisterConfirmCommand.SUCCESS,
         RegisterConfirmCommand.FAILURE,
         UpdateUserCommand.SUCCESS,
         IterationMediator.CREATE,
         IterationMediator.EDIT,
         ProjectDetailsMediator.CANCEL_EDIT,
         ProjectDetailsMediator.EDIT,
         ProjectLoadCommand.SUCCESS];
   }

   override public function handleNotification(notification:INotification):void {
      switch (notification.getName()) {
         case BacklogItemMediator.CREATE :
         case BacklogItemMediator.EDIT :
            _popupManager.showBacklogWindow(notification);
            break;
         case IterationMediator.CREATE :
            _popupManager.showIterationWindow("Create Iteration", notification);
            break;
         case IterationMediator.EDIT :
            _popupManager.showIterationWindow("Edit " + _projectProxy.selectedIteration.title, notification);
            break;
         case ProjectDetailsMediator.EDIT :
            _popupManager.showProjectWindow("Edit " + _projectProxy.selectedProject.title, notification);
            break;
         case LoginCommand.SUCCESS :
            login();
            break;
         case ProjectPanelMediator.SHOW_PRINT_PREVIEW :
            _popupManager.showPrintPreview(notification.getBody() as BacklogPrintView);
            break;
         case ApplicationFacade.NOTE_CREATE_IMPEDIMENT :
            _popupManager.showIssueWindow("Add Impediment", notification);
            break;
         case ApplicationFacade.NOTE_EDIT_IMPEDIMENT :
            _popupManager.showIssueWindow("Edit Impediment", notification);
            break;
         case ApplicationFacade.NOTE_PROJECT_ACCESS_MANAGE :
            _popupManager.showProjectAdminWindow(notification);
            break;
         case ApplicationFacade.NOTE_THEMES_MANAGE :
            _popupManager.showThemesWindow(notification);
            break;
         case ApplicationFacade.NOTE_DISPLAY_BURNDOWN :
            _popupManager.showBurndownWindow(_projectProxy.selectedIteration.title + " Burndown", notification);
            break;
         case ApplicationFacade.NOTE_DISPLAY_VELOCITY :
            _popupManager.showVelocityWindow(_projectProxy.selectedProject.title + " Velocity comparison", notification);
            break;
         case ApplicationFacade.NOTE_NAVIGATE :
            navigate(MenuItem(notification.getBody()));
            break;
         case ProjectDetailsMediator.CANCEL_EDIT :
         case ProjectLoadCommand.SUCCESS :
            _secureContext.setAvailableRoles(_projectProxy.selectedProject);
            break;
         case ApplicationFacade.NOTE_SHOW_ERROR_MSG :
            app.messageBox.showFailureMessage(notification.getBody() as String);
            break;
         case ApplicationFacade.NOTE_SHOW_SUCCESS_MSG :
            app.messageBox.showSuccessMessage(notification.getBody() as String);
            break;
         case ApplicationFacade.NOTE_CLEAR_MSG :
            app.messageBox.clearAndHide();
            break;
         case UpdateUserCommand.SUCCESS :
            app.lblUser.text = _secureContext.currentUser.fullName;
            break;
         case RegisterConfirmCommand.SUCCESS :
            sendNotification(ApplicationFacade.NOTE_SHOW_SUCCESS_MSG,
                             "Your registration has now been confirmed and you can access express using the " +
                             "email address and password you provided.");
            app.loginView.reponseText.visible = true;
            app.loginView.reponseText.includeInLayout = true;
            break;
         case RegisterConfirmCommand.FAILURE :
            sendNotification(ApplicationFacade.NOTE_SHOW_ERROR_MSG,
                             "Unfortunately we were unable to comfirm your  registration. If you have used the " +
                             "link on the email we sent you please contact the administrator and report this problem");
            app.loginView.reponseText.visible = false;
            app.loginView.reponseText.includeInLayout = false;
            break;
      }
   }

   public function handleBacklogViewCreated(event:Event):void {
      facade.registerMediator(new BacklogMediator(app.backlogView));
   }

   public function handleProjectAccessFormCreated(event:FlexEvent):void {
      facade.registerMediator(new ProjectAccessMediator(app.projectAccessForm));
   }

   public function handleRiskViewCreated(event:FlexEvent):void {
      //facade.registerMediator(new RiskMediator(view.riskView));
   }

   public function handleWallViewCreated(event:FlexEvent):void {
      facade.registerProxy(new WallProxy());
      facade.registerMediator(new ScrumWallMediator(app.wallView));
      //      facade.registerMediator(new WallMediator(app.wallView));
   }

   public function handleLoginViewCreated(event:Event):void {
      facade.registerMediator(new LoginMediator(_loginView));
   }

   public function handleProfileViewCreated(event:Event):void {
      facade.registerMediator(new ProfileMediator(app.profileView));
   }

   public function handleRegisterViewCreated(event:Event):void {
      facade.registerMediator(new RegisterMediator(app.registerView));
   }


   public function handleNavigateRequest(event:MouseEvent):void {
      var item:MenuItem = (event.target as LinkButton).data as MenuItem;
      navigate(item);
   }

   private function navigate(item:MenuItem):void {
      sendNotification(ApplicationFacade.NOTE_CLEAR_MSG);
      app.views.selectedIndex = item.index;
   }

   public function handleLoginRequest(event:MouseEvent):void {
      if (_loginView == null) {
         _loginView = new LoginView();
         _loginView.addEventListener(FlexEvent.CREATION_COMPLETE, handleLoginViewCreated);
      }

      PopUpManager.addPopUp(_loginView, app, true);
      PopUpManager.centerPopUp(_loginView);
      _loginView.y = _loginView.y - 200;
      _loginView.visible = true;
   }

   public function login():void {
      var user:User = _secureContext.currentUser;
      sendNotification(ApplicationFacade.NOTE_LOAD_PROJECT_LIST);

      app.lblUser.text = user.fullName;
      app.topBox.visible = true;
      if (user.hasProjects) {
         navigate(new MenuItem(PROJECT_HEAD, BACKLOG_VIEW, null));
      }
      else {
         navigate(new MenuItem(ACCESS_HEAD, ACCESS_VIEW, null));
      }
      app.menu.visible = true;
   }

   public function logout():void {
      SecureContextProxy(facade.retrieveProxy(SecureContextProxy.NAME)).logout();
      ServiceRegistry(facade.retrieveProxy(ServiceRegistry.NAME)).logout();
      app.views.selectedIndex = LOGIN_VIEW;
      app.topBox.visible = false;
      app.menu.visible = false;
   }

   public function get app():Express {
      return viewComponent as Express;
   }

   public function get loginView():LoginView {
      return _loginView;
   }

}
}