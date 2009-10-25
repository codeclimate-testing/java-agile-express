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
 * An Effort record is an immutable object. It represents the remaining effort for some task or 
 * tasks on a given date. It should never be altered.
 * @author adam
 *
 */
@Entity
@Table(name = "EFFORT")
public class EffortRecord implements Persistable, Comparable<EffortRecord> {
   private static final long serialVersionUID = 3358201566728092604L;

   @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_EFFORT")
   @TableGenerator(name = "GEN_EFFORT", table = "SEQUENCE_LIST", pkColumnName = "NAME",
            valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 100, 
            pkColumnValue = "EFFORT")
   @Column(name="EFFORT_REC_ID")
   private Long id;
   
   @Column(name = "RECORD_DATE") @Temporal(TemporalType.DATE)
   private Calendar date;
   
   @Column(name = "REMAINING_EFFORT")
   private Integer effort;
   
   @ManyToOne @JoinColumn(name = "ITERATION_ID")
   private Iteration iteration;

   /**
    * This constructor exists to fulfill the Serializable contract, it should not be used.
    */
   protected EffortRecord() { }

   public EffortRecord(Calendar date, Integer effort, Iteration iteration) {
      this.date = date;
      this.effort = effort;
      this.iteration = iteration;
   }

   public Long getId() {
      return id;
   }

   public Calendar getDate() {
      return date;
   }

   public Integer getEffort() {
      return effort;
   }

   public Iteration getIteration() {
      return iteration;
   }

   public int compareTo(EffortRecord effortRecord) {
      return this.date.compareTo(effortRecord.getDate());
   }
}
