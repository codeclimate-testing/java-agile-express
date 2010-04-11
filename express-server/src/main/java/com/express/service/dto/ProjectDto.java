package com.express.service.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class ProjectDto implements Serializable {
   
   private static final long serialVersionUID = 1266194346173201343L;

   private Long id;
   
   private Long version;
   
   private Calendar startDate;
   
   private String title;
   
   private String description;

   private String effortUnit;

   private String reference;
   
   private List<ProjectWorkerDto> projectWorkers;
   
   private List<IterationDto> iterations;
   
   private List<BacklogItemDto> productBacklog;

   private List<DailyProjectStatusRecordDto> history;

   private List<AccessRequestDto> accessRequests;

   private List<String> actors;

   private List<ThemeDto> themes;

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

   public Calendar getStartDate() {
      return startDate;
   }

   public void setStartDate(Calendar startDate) {
      this.startDate = startDate;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getEffortUnit() {
      return effortUnit;
   }

   public void setEffortUnit(String effortUnit) {
      this.effortUnit = effortUnit;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getReference() {
      return reference;
   }

   public void setReference(String reference) {
      this.reference = reference;
   }

   public List<ProjectWorkerDto> getProjectWorkers() {
      return projectWorkers;
   }

   public void setProjectWorkers(List<ProjectWorkerDto> projectWorkers) {
      this.projectWorkers = projectWorkers;
   }

   public List<IterationDto> getIterations() {
      return iterations;
   }

   public void setIterations(List<IterationDto> iterations) {
      this.iterations = iterations;
   }

   public List<BacklogItemDto> getProductBacklog() {
      return productBacklog;
   }

   public void setProductBacklog(List<BacklogItemDto> productBacklog) {
      this.productBacklog = productBacklog;
   }

   public List<DailyProjectStatusRecordDto> getHistory() {
      return history;
   }

   public void setHistory(List<DailyProjectStatusRecordDto> history) {
      this.history = history;
   }

   public List<AccessRequestDto> getAccessRequests() {
      return accessRequests;
   }

   public void setAccessRequests(List<AccessRequestDto> accessRequests) {
      this.accessRequests = accessRequests;
   }

   public List<String> getActors() {
      return actors;
   }

   public void setActors(List<String> actors) {
      this.actors = actors;
   }

   public List<ThemeDto> getThemes() {
      return themes;
   }

   public void setThemes(List<ThemeDto> themes) {
      this.themes = themes;
   }
}
