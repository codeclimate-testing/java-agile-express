package com.express.service.dto;

import com.express.domain.Project;
import com.express.domain.User;

import java.util.Date;
import java.io.Serializable;

public class AccessRequestDto implements Serializable {
   private static final long serialVersionUID = -1531630899821683796L;

   private Long id;

   private Long version;

   private UserDto requestor;

   private Date requestDate;

   private Date resolvedtDate;

   private Integer status;

   private String reason;

   private ProjectDto project;

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

   public UserDto getRequestor() {
      return requestor;
   }

   public void setRequestor(UserDto requestor) {
      this.requestor = requestor;
   }

   public Date getRequestDate() {
      return requestDate;
   }

   public void setRequestDate(Date requestDate) {
      this.requestDate = requestDate;
   }

   public Date getResolvedtDate() {
      return resolvedtDate;
   }

   public void setResolvedtDate(Date resolvedtDate) {
      this.resolvedtDate = resolvedtDate;
   }

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      this.status = status;
   }

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }

   public ProjectDto getProject() {
      return project;
   }

   public void setProject(ProjectDto project) {
      this.project = project;
   }
}
