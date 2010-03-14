package com.express.service.dto;

import java.io.Serializable;
import java.util.Date;

public class DailyProjectStatusRecordDto implements Serializable, Comparable<DailyProjectStatusRecordDto> {

   private static final long serialVersionUID = -2430698032474395774L;

   private Long id;

   private Date date;

   private Integer totalPoints;

   private Integer completedPoints;

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

   public Integer getTotalPoints() {
      return totalPoints;
   }

   public void setTotalPoints(Integer totalPoints) {
      this.totalPoints = totalPoints;
   }

   public Integer getCompletedPoints() {
      return completedPoints;
   }

   public void setCompletedPoints(Integer completedPoints) {
      this.completedPoints = completedPoints;
   }

   public int compareTo(DailyProjectStatusRecordDto record) {
      return date.compareTo(record.getDate());
   }
}