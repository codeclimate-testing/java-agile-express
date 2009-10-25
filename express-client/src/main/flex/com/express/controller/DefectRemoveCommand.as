package com.express.controller
{
import com.express.ApplicationFacade;
import com.express.model.ProjectProxy;
import com.express.model.domain.Issue;

import mx.collections.ArrayCollection;
import mx.rpc.Fault;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

public class DefectRemoveCommand extends SimpleCommand implements IResponder
{
   override public function execute(notification:INotification):void {
      var defect : Issue = notification.getBody() as Issue;
      //TODO: do remote call for create and take the next line(s) out.
      removeItem((facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy).defectList, defect);
      this.result(null);
   }

   public function result(data : Object) : void {
      sendNotification(ApplicationFacade.NOTE_LOAD_DEFECT);
   }

   public function fault(info : Object) : void {
      var fault : Fault = (info as FaultEvent).fault;
   }

   private function removeItem(defects : ArrayCollection, defect : Issue) : void {
      var index : int = 0;
      var match : Boolean = false;
      for each(var defectItem : Issue in defects) {
         if (defectItem.reference == defect.reference) {
            match = true;
            break;
         }
         else {
            index++;
         }
      }
      if (match) {
         defects.removeItemAt(index);
      }
   }

}
}