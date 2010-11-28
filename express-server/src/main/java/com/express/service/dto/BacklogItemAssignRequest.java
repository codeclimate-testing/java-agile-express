package com.express.service.dto;

import java.io.Serializable;
import java.util.List;


public class BacklogItemAssignRequest implements Serializable {

   private static final long serialVersionUID = 4494774116538737140L;
   
   private Long[] itemIds;

   private Long projectId;
   
   private Long iterationFromId;
   
   private Long iterationToId;

   public Long getIterationFromId() {
      return iterationFromId;
   }

   public void setIterationFromId(Long iterationFromId) {
      this.iterationFromId = iterationFromId;
   }

   public Long getIterationToId() {
      return iterationToId;
   }

   public Long getProjectId() {
      return projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public void setIterationToId(Long iterationToId) {
      this.iterationToId = iterationToId;
   }

   public Long[] getItemIds() {
      return itemIds;
   }

   public void setItemIds(Long[] itemIds) {
      this.itemIds = itemIds;
   }
}
