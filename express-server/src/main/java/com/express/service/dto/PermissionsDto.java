package com.express.service.dto;

import java.io.Serializable;

/**
 * @author Adam Boas
 *         Created on Apr 1, 2009
 */
public class PermissionsDto implements Serializable{

   private Long id;
   private Long version;
   private Boolean iterationAdmin;
   private Boolean projectAdmin;
   private static final long serialVersionUID = 4182366610220431019L;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getVersion() {
      return version;
   }

   public void setVersion(Long version) {
      this.version = version;
   }

   public Boolean getIterationAdmin() {
      return iterationAdmin;
   }

   public void setIterationAdmin(Boolean iterationAdmin) {
      this.iterationAdmin = iterationAdmin;
   }

   public Boolean getProjectAdmin() {
      return projectAdmin;
   }

   public void setProjectAdmin(Boolean projectAdmin) {
      this.projectAdmin = projectAdmin;
   }
}
