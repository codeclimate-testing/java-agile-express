package com.express.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IterationDto implements Serializable, Comparable<IterationDto> {
   
   private static final long serialVersionUID = -1846071747249767308L;

   private Long id;
   
   private Long version;
   
   private Date startDate;
   
   private Date endDate;
   
   private String title;
   
   private String description;
   
   private ProjectDto project;
   
   private List<BacklogItemDto> backlog;
   
   private List<EffortRecordDto> burndown;

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

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public ProjectDto getProject() {
      return project;
   }

   public void setProject(ProjectDto project) {
      this.project = project;
   }

   public List<BacklogItemDto> getBacklog() {
      return backlog;
   }

   public void setBacklog(List<BacklogItemDto> backlog) {
      this.backlog = backlog;
   }

   public List<EffortRecordDto> getBurndown() {
      return burndown;
   }

   public void setBurndown(List<EffortRecordDto> burndown) {
      this.burndown = burndown;
   }

   public int compareTo(IterationDto iteration) {
      return this.startDate.compareTo(iteration.getStartDate());
   }

}
