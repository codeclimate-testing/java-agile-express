package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.SecureContextProxy;
import com.express.model.domain.User;
import com.express.model.request.LoginRequest;
import com.express.service.ServiceRegistry;

import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class LoginCommand extends SimpleCommand implements IResponder
{
   public static const SUCCESS:String = "LoginCommand.SUCCESS";
   public static const FAILURE:String = "LoginCommand.FAILURE";
   public static const EMAIL_SUCCESS:String = "LoginCommand.EMAIL_SUCCESS";
   private var _password:String;

   public function LoginCommand() {
   }

   override public function execute(notification:INotification):void {
      var loginRequest:LoginRequest = notification.getBody() as LoginRequest;
      _password = loginRequest.password;
      var registry:ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var service:RemoteObject = registry.getRemoteObjectService(ApplicationFacade.USER_SERVICE);
      var call:Object = service.login(loginRequest);
      call.addResponder(this);
   }

   public function result(data:Object):void {
      if (data.result == null) {
         sendNotification(EMAIL_SUCCESS);
         return;
      }
      var secureContext:SecureContextProxy = facade.retrieveProxy(SecureContextProxy.NAME) as SecureContextProxy;
      var serviceRegistry:ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var user:User = data.result as User;
      serviceRegistry.setCredentials(user.email, _password);
      secureContext.currentUser = user;
      sendNotification(SUCCESS);
   }

   public function fault(info:Object):void
   {
      trace((info as FaultEvent).fault.message);
      sendNotification(FAILURE);
   }
}
}