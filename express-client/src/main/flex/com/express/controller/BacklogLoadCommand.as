package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.request.LoadBacklogRequest;
import com.express.service.ServiceRegistry;

import mx.collections.ArrayCollection;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

   /**
    * Loads the backlog for the selected Iteration.
    */
public class BacklogLoadCommand extends SimpleCommand implements IResponder
{
   private var _proxy : ProjectProxy;

   public function BacklogLoadCommand() {
   }

   override public function execute(notification:INotification):void {
      var request : LoadBacklogRequest = new LoadBacklogRequest();
      _proxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      if (_proxy.productBacklogRequest) {
         request.type = LoadBacklogRequest.TYPE_PROJECT;
         request.parentId = _proxy.selectedProject.id;
      }
      else {
         request.type = LoadBacklogRequest.TYPE_ITERATION;
         request.parentId = _proxy.selectedIteration.id;
      }
      var registry : ServiceRegistry = facade.retrieveProxy(ServiceRegistry.NAME) as ServiceRegistry;
      var service : RemoteObject = registry.getRemoteObjectService(ApplicationFacade.PROJECT_SERVICE);
      var call : Object = service.loadBacklog(request);
      call.addResponder(this);
   }

   public function result(data : Object) :void {

      if(_proxy.productBacklogRequest) {
         _proxy.selectedProject.productBacklog = ArrayCollection(data.result);
         _proxy.setProductBacklogSource(ArrayCollection(data.result));
      }
      else {
         _proxy.selectedIteration.backlog.source = ArrayCollection(data.result).source;
      }
      _proxy.setIterationHistory(_proxy.selectedIteration);
      _proxy.productBacklogRequest = false;
      sendNotification(ApplicationFacade.NOTE_LOAD_BACKLOG_COMPLETE);
   }

   public function fault(info : Object) : void {
      trace((info as FaultEvent).fault.message);
   }
}
}