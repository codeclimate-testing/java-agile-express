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

   @Column(name = "description") @Lob
   private String description;

   @Column(name = "start_date")
   private Calendar startDate;

   @Column(name = "end_date")
   private Calendar endDate;

   @ManyToOne @JoinColumn(name = "iteration_id")
   private Iteration iteration;

   @OneToOne(mappedBy = "impediment")
   private BacklogItem backlogItem;

   @ManyToOne(cascade = CascadeType.ALL) @JoinColumn(name = "user_id")
   private User responsible;

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

   public BacklogItem getBacklogItem() {
      return backlogItem;
   }

   public void setBacklogItem(BacklogItem backlogItem) {
      this.backlogItem = backlogItem;
   }

   public Iteration getIteration() {
      return iteration;
   }

   public void setIteration(Iteration iteration) {
      this.iteration = iteration;
   }

   public User getResponsible() {
      return responsible;
   }

   public void setResponsible(User responsible) {
      this.responsible = responsible;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof Issue))
         return false;
      Issue issue = (Issue) obj;
      return this.id.equals(issue.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(Issue.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("title=").append(title).append(",");
      output.append("description=").append(description).append(",");
      output.append("startDate=").append(startDate).append(",");
      output.append("endDate=").append(endDate).append("]");
      return output.toString();
   }
}
