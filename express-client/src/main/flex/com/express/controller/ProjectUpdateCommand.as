package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Project;
import com.express.service.ServiceRegistry;

import mx.rpc.Fault;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class ProjectUpdateCommand extends SimpleCommand implements IResponder
{
   override public function execute(notification : INotification) : void {
      var project : Project = (facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy).selectedProject;
      var registry : ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var service : RemoteObject = registry.getRemoteObjectService(ApplicationFacade.PROJECT_SERVICE);
      var call : Object = service.updateProject(project);
      call.addResponder(this);
   }

   public function result(data : Object) : void {
      var proxy : ProjectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      proxy.selectedProject = data.result as Project;
      sendNotification(ProjectLoadCommand.SUCCESS);
   }

   public function fault(info : Object) : void {
      var fault : Fault = (info as FaultEvent).fault;
      trace(fault.message);
   }
}
}