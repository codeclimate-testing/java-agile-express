package com.express.service.dto;

import java.util.List;
import java.io.Serializable;

/**
 *
 */
public class ProjectWorkersUpdateRequest implements Serializable {

   private List<ProjectWorkerDto> workers;

   private Long projectId;

   public List<ProjectWorkerDto> getWorkers() {
      return workers;
   }

   public void setWorkers(List<ProjectWorkerDto> workers) {
      this.workers = workers;
   }

   public Long getProjectId() {
      return projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }
}
