package com.express.service.dto;

import java.io.Serializable;
import java.util.Date;

public class EffortRecordDto implements Serializable {

   private static final long serialVersionUID = -2430698032474395774L;
   
   private Long id;
   
   private Date date;
   
   private Integer effort;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Integer getEffort() {
      return effort;
   }

   public void setEffort(Integer effort) {
      this.effort = effort;
   }

}
