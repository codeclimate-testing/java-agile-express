package com.express.service.dto;

/**
 *
 */
public class AddImpedimentRequest {

   Long backlogItemId;

   Long iterationId;

   IssueDto impediment;

   public Long getBacklogItemId() {
      return backlogItemId;
   }

   public void setBacklogItemId(Long backlogItemId) {
      this.backlogItemId = backlogItemId;
   }

   public Long getIterationId() {
      return iterationId;
   }

   public void setIterationId(Long iterationId) {
      this.iterationId = iterationId;
   }

   public IssueDto getImpediment() {
      return impediment;
   }

   public void setImpediment(IssueDto impediment) {
      this.impediment = impediment;
   }
}
