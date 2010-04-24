package com.express.domain;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A DailyIterationStatusRecord is an immutable object. It represents a record for tasks and story effort for an
 * iteration on a given date. It should never be altered.
 *
 * @author adam boas
 *
 */
@Entity
@Table(name = "effort")
public class DailyIterationStatusRecord implements Persistable, Comparable<DailyIterationStatusRecord> {
   private static final long serialVersionUID = 3358201566728092604L;

   @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_it_status_rec")
   @TableGenerator(name = "gen_it_status_rec", table = "SEQUENCE_LIST", pkColumnName = "NAME",
            valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 100, 
            pkColumnValue = "EFFORT")
   @Column(name="effort_rec_id")
   private Long id;
   
   @Column(name = "record_date") @Temporal(TemporalType.DATE)
   private Calendar date;
   
   @Column(name = "remaining_effort")
   private Integer taskHoursRemaining;

   @Column(name = "points_total")
   private Integer totalPoints;

   @Column(name = "points_completed")
   private Integer completedPoints;

   @ManyToOne @JoinColumn(name = "iteration_id")
   private Iteration iteration;

   /**
    * This constructor exists to fulfill the Serializable contract, it should not be used.
    */
   protected DailyIterationStatusRecord() { }

   public DailyIterationStatusRecord(Calendar date,
                       Integer taskHoursRemaining,
                       Integer totalPoints,
                       Integer completedPoints,
                       Iteration iteration) {
      this.date = date;
      this.taskHoursRemaining = taskHoursRemaining;
      this.totalPoints = totalPoints;
      this.completedPoints = completedPoints;
      this.iteration = iteration;
   }

   public Long getId() {
      return id;
   }

   public Calendar getDate() {
      return date;
   }

   public Integer getTaskHoursRemaining() {
      return taskHoursRemaining;
   }

   public Iteration getIteration() {
      return iteration;
   }

   public Integer getTotalPoints() {
      return totalPoints;
   }

   public Integer getCompletedPoints() {
      return completedPoints;
   }

   public int compareTo(DailyIterationStatusRecord dailyIterationStatusRecord) {
      return this.date.compareTo(dailyIterationStatusRecord.getDate());
   }
}