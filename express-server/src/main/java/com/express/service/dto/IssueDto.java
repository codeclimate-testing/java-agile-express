package com.express.service.dto;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class IssueDto implements Serializable {
   private Long id;

   private Long version;

   private String title;

   private String description;

   private Date startDate;

   private Date endDate;

   private IterationDto iteration;

   private UserDto responsible;

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

   public IterationDto getIteration() {
      return iteration;
   }

   public void setIteration(IterationDto iteration) {
      this.iteration = iteration;
   }

   public UserDto getResponsible() {
      return responsible;
   }

   public void setResponsible(UserDto responsible) {
      this.responsible = responsible;
   }
}
