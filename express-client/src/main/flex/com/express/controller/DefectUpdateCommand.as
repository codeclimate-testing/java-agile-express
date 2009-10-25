package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Issue;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class DefectUpdateCommand extends SimpleCommand implements IResponder
{
   override public function execute(notification:INotification):void {
      var defect : Issue = notification.getBody() as Issue;
      //TODO: do remote call for create and take the next line(s) out.
      doDummyUpdate(defect);
      this.result(null);
   }

   public function result(data : Object) :void {
      sendNotification(ApplicationFacade.NOTE_LOAD_BACKLOG);
   }

   public function fault(info : Object) : void {
      Alert.show((info as FaultEvent).fault.message);
      trace((info as FaultEvent).fault.message);
   }

   protected function doDummyUpdate(updated : Issue) : void {
      var defects : ArrayCollection = (facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy).defectList;
      for each(var defect : Issue in defects) {
         if (defect.reference == updated.reference) {
            defect.title = updated.title;
            defect.description = updated.description;
            defect.status = updated.status;
            defect.effort = updated.effort;
            defect.assignedTo = updated.assignedTo;
            break;
         }
      }
      defects.refresh();
   }
}
}