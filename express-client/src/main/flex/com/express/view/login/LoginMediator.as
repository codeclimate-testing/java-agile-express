package com.express.view.login
{
import com.express.ApplicationFacade;
import com.express.controller.LoginCommand;
import com.express.model.PersistentLoginDetails;
import com.express.model.request.LoginRequest;
import com.express.navigation.MenuItem;
import com.express.view.ApplicationMediator;
import com.express.view.form.FormUtility;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.events.FlexEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.mediator.Mediator;

public class LoginMediator extends Mediator
{
   public static const NAME:String = "LoginViewMediator";

   private var _loginValidators : Array;

   public function LoginMediator(viewComp : LoginView) {
      super(NAME, viewComp);
      viewComp.btnLogin.addEventListener(MouseEvent.CLICK, handleLoginButton);
      viewComp.btnClear.addEventListener(MouseEvent.CLICK, handleClearButton);
      viewComp.btnEmailRequest.addEventListener(MouseEvent.CLICK, handleEmailRequest);
      viewComp.btnRegister.addEventListener(MouseEvent.CLICK, handleRegisterButton);
      viewComp.addEventListener(FlexEvent.SHOW, handleShowLogin);

      _loginValidators = [];
      _loginValidators.push(viewComp.userNameValidator);
      _loginValidators.push(viewComp.passwordValidator);
      handleShowLogin(null);
   }

   override public function listNotificationInterests():Array {
      return [LoginCommand.FAILURE, LoginCommand.EMAIL_SUCCESS];
   }

   override public function handleNotification(notification : INotification):void {
      switch (notification.getName()) {
         case LoginCommand.EMAIL_SUCCESS :
            handleEmailSuccess();
            break;
         case LoginCommand.FAILURE :
            handleLoginFailure();
            break;
      }
   }

   public function handleShowLogin(event : Event) : void {
      if (PersistentLoginDetails.hasDetails()) {
         view.userName.text = PersistentLoginDetails.getEmail();
         view.password.text = PersistentLoginDetails.getPassword();
         view.rememberMe.selected = true;
      }
      else {
         resetLoginForm();
      }
      view.focusManager.setFocus(view.userName);
   }

   public function handleLoginButton(event : Event) : void {
      FormUtility.doValidate(_loginValidators);
      if (FormUtility.validateAll(_loginValidators)) {
         var loginRequest : LoginRequest = new LoginRequest();
         loginRequest.username = view.userName.text;
         loginRequest.password = view.password.text;
         loginRequest.passwordReminderRequest = false;
         sendNotification(ApplicationFacade.NOTE_LOGIN, loginRequest);

         if (view.rememberMe.selected) {
            PersistentLoginDetails.clear();
            PersistentLoginDetails.store(view.userName.text, view.password.text);
         }
         else {
            PersistentLoginDetails.clear();
         }
      }
      else {
         sendNotification(ApplicationFacade.NOTE_SHOW_ERROR_MSG, "Missing or invalid data entered");
      }
   }

   public function handleClearButton(event:MouseEvent) : void {
      resetLoginForm();
   }

   public function handleRegisterButton(event:MouseEvent) : void {
      sendNotification(ApplicationFacade.NOTE_NAVIGATE,
            new MenuItem(ApplicationMediator.REGISTER_HEAD, ApplicationMediator.REGISTER_VIEW, null));
   }

   public function handleEmailRequest(event:MouseEvent) : void {
      view.userNameValidator.validate();
      if (view.userName.errorString == null || view.userName.errorString.length == 0) {
         var loginRequest : LoginRequest = new LoginRequest();
         loginRequest.username = view.userName.text;
         loginRequest.passwordReminderRequest = true;
         sendNotification(ApplicationFacade.NOTE_LOGIN, loginRequest);
      }
   }


   public function handleLoginFailure() : void {
      sendNotification(ApplicationFacade.NOTE_SHOW_ERROR_MSG,
            "The user name and/or password you entered are not correct. " +
                                      "Please re-enter to log in again.");
   }

   public function handleEmailSuccess() : void {
      sendNotification(ApplicationFacade.NOTE_SHOW_SUCCESS_MSG,
            "An email with your password has been sent to your account.");
   }

   public function resetLoginForm() : void {
      view.userName.text = "";
      view.password.text = "";
      view.rememberMe.selected = false;
      FormUtility.clearValidationErrors(_loginValidators);
      sendNotification(ApplicationFacade.NOTE_CLEAR_MSG);
   }

   protected function get view() : LoginView
   {
      return viewComponent as LoginView;
   }
}
}