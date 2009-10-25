package com.express.controller
{
import com.express.model.ProjectProxy;

import mx.collections.ArrayCollection;
import mx.controls.Alert;
import mx.rpc.IResponder;
import mx.rpc.events.FaultEvent;

import org.puremvc.as3.interfaces.INotification;
import org.puremvc.as3.patterns.command.SimpleCommand;

   /**
    * Loads the backlog for the selected Iteration.
    */
public class DefectLoadCommand extends SimpleCommand implements IResponder
{
   override public function execute(notification:INotification):void {
      //TODO: send the item in the notification body to remote service
   }

   public function result(data : Object) :void {

   }

   public function fault(info : Object) : void {
      Alert.show((info as FaultEvent).fault.message);
      trace((info as FaultEvent).fault.message);
   }

   private function doDummyLoad() : void {
      var proxy : ProjectProxy = (facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy);
      var data : Object = new Object();
      var result : ArrayCollection = new ArrayCollection();
      /*
       for each(var item : BacklogItem in proxy.dummyBacklogItems) {
       result.addItem(item);
       }
       for each(item in proxy.extraBacklogItems) {
       result.addItem(item);
       }
       */
      data.result = result;
      this.result(data);
   }
}
}