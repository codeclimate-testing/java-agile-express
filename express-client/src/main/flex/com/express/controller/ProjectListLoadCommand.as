package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Project;
import com.express.service.ServiceRegistry;
import com.express.view.projectSummary.ProjectSummaryMediator;

import mx.collections.ArrayCollection;
import mx.rpc.Fault;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class ProjectListLoadCommand extends SimpleCommand implements IResponder {

   override public function execute(notification:INotification):void {
      var registry:ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var service:RemoteObject = registry.getRemoteObjectService(ApplicationFacade.PROJECT_SERVICE);
      var call:Object = service.findAllProjects();
      call.addResponder(this);
   }

   public function result(data:Object):void {
      var proxy:ProjectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      var mediator:ProjectSummaryMediator = facade.retrieveMediator(ProjectSummaryMediator.NAME) as ProjectSummaryMediator;
      proxy.projectList = data.result as ArrayCollection;
      if (mediator != null && proxy.selectedProject != null) {
         mediator.view.cboProjects.selectedIndex = getIndex(proxy.projectList, proxy.selectedProject);
      }
   }

   private function getIndex(list:ArrayCollection, project:Project):int {
      var index:int = 0;
      for each(var listProject:Project in list) {
         if (listProject.title == project.title) {
            return index;
         }
         index++;
      }
      return -1;
   }

   public function fault(info:Object):void {
      var fault:Fault = (info as FaultEvent).fault;
      trace(fault.message);
   }
}
}