package com.express.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Adam Boas
 *         Created on Apr 1, 2009
 */
public class ProjectWorkerDto implements Serializable {

   private Long id;
   private Long version;
   private Date createdDate;
   private ProjectDto project;
   private UserDto worker;
   private PermissionsDto permissions;
   private static final long serialVersionUID = 8326902224838584802L;

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

   public Date getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }

   public ProjectDto getProject() {
      return project;
   }

   public void setProject(ProjectDto project) {
      this.project = project;
   }

   public UserDto getWorker() {
      return worker;
   }

   public void setWorker(UserDto worker) {
      this.worker = worker;
   }

   public PermissionsDto getPermissions() {
      return permissions;
   }

   public void setPermissions(PermissionsDto permissions) {
      this.permissions = permissions;
   }
}
