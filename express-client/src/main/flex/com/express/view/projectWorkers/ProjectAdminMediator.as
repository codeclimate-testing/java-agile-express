package com.express.view.projectWorkers {
import com.express.ApplicationFacade;
import com.express.controller.event.GridButtonEvent;
import com.express.model.ProjectProxy;
import com.express.model.domain.AccessRequest;

import mx.controls.Alert;
import mx.controls.dataGridClasses.DataGridColumn;
import mx.events.CloseEvent;

import org.puremvc.as3.patterns.mediator.Mediator;

public class ProjectAdminMediator extends Mediator{
   public static const NAME : String = "ProjectAdminMediator";

   private var _proxy : ProjectProxy;

   public function ProjectAdminMediator(viewComp : ProjectAdmin, name : String = NAME) {
      super(name, viewComp);
      _proxy = facade.retrieveProxy(ProjectProxy.NAME) as ProjectProxy;
      viewComp.grdColRrequestor.labelFunction = formatRequestor;
      viewComp.grdRequest.addEventListener(GridButtonEvent.CLICK, handleGridButtonClick);
      viewComp.grdRequest.dataProvider = _proxy.accessRequests;
   }

   private function handleGridButtonClick(event : GridButtonEvent) : void {
      switch(event.action) {
         case GridButtonEvent.ACTION_ACCEPT :
            _proxy.selectedAccessRequest = view.grdRequest.selectedItem as AccessRequest;
            Alert.show("Please click yes if you want to accept", "Confirm Accept",
                       Alert.YES | Alert.NO, null, handleAcceptConfirm, null, Alert.YES);
            break;
         case GridButtonEvent.ACTION_REJECT :
            Alert.show("Please click yes if you want to reject", "Confirm Reject",
                       Alert.YES | Alert.NO, null, handleRejectConfirm, null, Alert.YES);
            break;
      }
   }

   private function handleAcceptConfirm(event : CloseEvent) : void {
      if (event.detail == Alert.YES) {
         _proxy.selectedAccessRequest.status = AccessRequest.APPROVED;
         sendNotification(ApplicationFacade.NOTE_PROJECT_ACCESS_RESPONSE, true);
      }
   }

   private function handleRejectConfirm(event : CloseEvent) : void {
      if (event.detail == Alert.YES) {
         _proxy.selectedAccessRequest.status = AccessRequest.REJECTED;
         sendNotification(ApplicationFacade.NOTE_PROJECT_ACCESS_RESPONSE, false);
      }
   }

   public function get view() : ProjectAdmin {
      return viewComponent as ProjectAdmin;
   }

   private function formatRequestor(row : Object, col : DataGridColumn) : String {
      var item : AccessRequest = row as AccessRequest;
      return item.requestor.fullName;
   }
}
}