package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Iteration;
import com.express.model.domain.Project;
import com.express.service.ServiceRegistry;

import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class ProjectLoadCommand extends SimpleCommand implements IResponder
{
   public static const SUCCESS : String = "ProjectLoadCommand.SUCCESS";
   private var _proxy : ProjectProxy;

   override public function execute(notification:INotification):void {
      var id : Number = notification.getBody() as Number;
      var registry : ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var service : RemoteObject = registry.getRemoteObjectService(ApplicationFacade.PROJECT_SERVICE);
      var call : Object = service.findProject(id);
      call.addResponder(this);
   }

   public function result(data:Object):void {
      _proxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      var project : Project = data.result as Project;
      _proxy.selectedProject = project;
      sendNotification(SUCCESS);
   }

   public function fault(info:Object):void {
      var fault : FaultEvent = info as FaultEvent;
      trace(fault.message);
   }
}
}