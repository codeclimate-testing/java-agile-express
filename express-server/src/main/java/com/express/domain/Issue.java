package com.express.domain;

import javax.persistence.*;
import java.util.Calendar;

/**
 *
 */
@Entity
@Table(name = "issue")
public class Issue implements Persistable{
   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_issue")
   @TableGenerator(name = "gen_issue", table = "sequence_list", pkColumnName = "name",
         valueColumnName = "next_value", allocationSize = 1, initialValue = 100,
         pkColumnValue = "issue")
   @Column(name = "issue_id")
   private Long id;

   @Version
   @Column(name = "version_no")
   private Long version;

   @Column(name = "title")
   private String title;

   @Column(name = "description")
   private String description;

   @Column(name = "start_date")
   private Calendar startDate;

   @Column(name = "end_date")
   private Calendar endDate;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
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

   public Calendar getStartDate() {
      return startDate;
   }

   public void setStartDate(Calendar startDate) {
      this.startDate = startDate;
   }

   public Calendar getEndDate() {
      return endDate;
   }

   public void setEndDate(Calendar endDate) {
      this.endDate = endDate;
   }
}
