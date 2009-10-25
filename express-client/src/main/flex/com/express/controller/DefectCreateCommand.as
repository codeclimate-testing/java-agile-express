package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Issue;

import mx.rpc.Fault;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class DefectCreateCommand extends SimpleCommand implements IResponder
{
   override public function execute(notification:INotification):void {
      var defect : Issue = notification.getBody() as Issue;
      //TODO: do remote call for create and take the next line(s) out.
      var proxy : ProjectProxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      //defect.reference = proxy.currentProject.reference + "-" + proxy.dummyIndex++;
      (facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy).defectList.addItem(defect);
      this.result(null);
   }

   public function result(data : Object) : void {
      sendNotification(ApplicationFacade.NOTE_LOAD_DEFECT);
   }

   public function fault(info : Object) : void {
      var fault : Fault = (info as FaultEvent).fault;
   }
}
}