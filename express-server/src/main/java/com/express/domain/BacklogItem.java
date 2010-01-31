package com.express.domain;

import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

/**
 * BacklogItem models stories and tasks which can be contained in the product backlog or assigned
 * to an Iteration where they can be worked on. By definition BacklogItems which have a parent are
 * a Task, if the backlogItem does not have a parent then it has either a project or an iteration.
 *
 * @author adam boas
 */
@Entity
@Table(name = "backlog")
public class BacklogItem implements Persistable, Comparable<BacklogItem> {
   private static final long serialVersionUID = 1680156807969361460L;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_backlog")
   @TableGenerator(name = "gen_backlog", table = "sequence_list", pkColumnName = "name",
         valueColumnName = "next_value", allocationSize = 1, initialValue = 100,
         pkColumnValue = "backlog")
   @Column(name = "backlog_id")
   private Long id;

   @Version
   @Column(name = "version_no")
   private Long version;

   @Column(name = "title")
   private String title;

    @Column(name = "as_a")
   private String asA;

   @Column(name = "i_want")
   private String want;

   @Column(name = "so_that")
   private String soThat;

   @Column(name = "summary")
   private String summary;

   @Column(name = "description")
   @Lob
   private String detailedDescription;

   @Enumerated(EnumType.ORDINAL)
   @Column(name = "status")
   private Status status;

   @Column(name = "reference")
   private String reference;

   @Column(name = "business_value")
   private Integer businessValue;

   @Column(name = "effort")
   private Integer effort;

   @Column(name = "task_count")
   private Integer taskCount = 0;

   @ManyToOne(cascade = CascadeType.ALL)
   @JoinColumn(name = "impediment_id", referencedColumnName = "issue_id")
   private Issue impediment;

   @ManyToOne
   @JoinColumn(name = "user_id")
   private User assignedTo;

   @ManyToOne
   @JoinColumn(name = "project_id")
   @OptimisticLock(excluded = true)
   private Project project;

   @ManyToOne
   @JoinColumn(name = "iteration_id")
   @OptimisticLock(excluded = true)
   private Iteration iteration;

   @ManyToOne
   @JoinColumn(name = "parent_id", referencedColumnName = "backlog_id")
   private BacklogItem parent;

   @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
   private Set<BacklogItem> tasks;

   @ManyToMany
   @JoinTable(name = "backlog_theme", joinColumns = {
         @JoinColumn(name = "backlog_id") }, inverseJoinColumns = {
         @JoinColumn(name = "theme_id") })
   private Set<Theme> themes;

   @OneToMany(mappedBy = "backlogItem", cascade = CascadeType.ALL)
   @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
   private Set<AcceptanceCriteria> acceptanceCriteria;

   public BacklogItem() {
      tasks = new HashSet<BacklogItem>();
      themes = new HashSet<Theme>();
      acceptanceCriteria = new HashSet<AcceptanceCriteria>();
   }

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

   public Status getStatus() {
      return status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public String getReference() {
      return reference;
   }

   public void setReference(String reference) {
      this.reference = reference;
   }

   public Integer getBusinessValue() {
      return businessValue;
   }

   public void setBusinessValue(Integer businessValue) {
      this.businessValue = businessValue;
   }

   public Integer getEffort() {
      return effort;
   }

   public Integer getTaskCount() {
      //For versions before 0.7.3 taskCount will not have been maintained
      if(taskCount == 1 && tasks.size() > 1) {
         taskCount = tasks.size() + 1;
      }
      return taskCount;
   }

   public void setEffort(Integer effort) {
      this.effort = effort;
   }

   public User getAssignedTo() {
      return assignedTo;
   }

   public void setAssignedTo(User assignedTo) {
      this.assignedTo = assignedTo;
   }

   public Project getProject() {
      if (project != null) {
         return project;
      }
      if (iteration != null) {
         return iteration.getProject();
      }
      if (parent != null) {
         return parent.getProject();
      }
      return null;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public Iteration getIteration() {
      return iteration;
   }

   public void setIteration(Iteration iteration) {
      this.iteration = iteration;
   }

   public String getAsA() {
      return asA;
   }

   public void setAsA(String asA) {
      this.asA = asA;
   }

   public String getWant() {
      return want;
   }

   public void setWant(String want) {
      this.want = want;
   }

   public String getSoThat() {
      return soThat;
   }

   public void setSoThat(String soThat) {
      this.soThat = soThat;
   }

   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   public Issue getImpediment() {
      return impediment;
   }

   public void setImpediment(Issue impediment) {
      this.impediment = impediment;
   }

   public String getDetailedDescription() {
      return detailedDescription;
   }

   public void setDetailedDescription(String detailedDescription) {
      this.detailedDescription = detailedDescription;
   }


   public BacklogItem getParent() {
      return parent;
   }


   public void setParent(BacklogItem parent) {
      this.parent = parent;
   }


   public Set<BacklogItem> getTasks() {
      return tasks;
   }


   public void setTasks(Set<BacklogItem> tasks) {
      this.tasks = tasks;
   }

   public void addTask(BacklogItem task) {
      this.tasks.add(task);
      task.setParent(this);
      taskCount++;
   }

   public boolean removeTask(BacklogItem task) {
      boolean result = this.tasks.remove(task);
      if (result) {
         task.setParent(null);
      }
      return result;
   }

   public Set<Theme> getThemes() {
      return themes;
   }

   public void setThemes(Set<Theme> themes) {
      this.themes = themes;
   }

   public Set<AcceptanceCriteria> getAcceptanceCriteria() {
      return acceptanceCriteria;
   }

   public void setAcceptanceCriteria(Set<AcceptanceCriteria> acceptanceCriteria) {
      this.acceptanceCriteria = acceptanceCriteria;
   }

   public void createReference() {
      if (reference != null) {
         return;
      }
      StringBuilder ref = new StringBuilder();
      if (parent != null) {
         ref.append(parent.getReference());
         ref.append(":T-");
         ref.append(parent.getTaskCount());
      }
      else {
         ref.append(getProject().getReference()).append("-");
         ref.append(getProject().getStoryCount());
      }
      this.reference = ref.toString();
   }

   public int compareTo(BacklogItem item) {
      if (this.id == null) {
         return 1;
      }
      else if (item.getId() == null) {
         return -1;
      }
      return this.businessValue.compareTo(item.getBusinessValue());
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof BacklogItem))
         return false;
      BacklogItem backlogItem = (BacklogItem) obj;
      return this.id.equals(backlogItem.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(BacklogItem.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("reference=").append(reference).append(",");
      output.append("status=").append(status).append(",");
      output.append("asA=").append(asA).append(",");
      output.append("want=").append(want).append(",");
      output.append("soThat=").append(soThat).append(",");
      output.append("summary=").append(summary).append("]");
      return output.toString();
   }

   /**
    * Retrieves the hours remaining in all tasks for this User Story.
    *
    * @return int representing the number of hours remaining to complete this Story.
    */
   public int getTaskEffortRemaining() {
      int remainingEffort = 0;
      for (BacklogItem task : tasks) {
         remainingEffort += task.getEffort();
      }
      return remainingEffort;
   }

   public void makeStatusConsitent() {
      if (parent != null) {
         parent.makeStatusConsitent();
         return;
      }
      else if (tasks.size() == 0) {
         return;
      }
      Map<Status, Integer> counts = new HashMap<Status, Integer>();
      counts.put(Status.OPEN, 0);
      counts.put(Status.IN_PROGRESS, 0);
      counts.put(Status.TEST, 0);
      counts.put(Status.DONE, 0);
      for (BacklogItem task : tasks) {
         counts.put(task.getStatus(), counts.get(task.getStatus()) + 1);
      }
      if (counts.get(Status.OPEN) == tasks.size()) {
         status = Status.OPEN;
      }
      else if (counts.get(Status.IN_PROGRESS) > 0) {
         status = Status.IN_PROGRESS;
      }
      else if (counts.get(Status.TEST) == tasks.size()) {
         status = Status.TEST;
      }
      else if (counts.get(Status.DONE) == tasks.size()) {
         status = Status.DONE;
      }
   }

   public BacklogItem findTaskByReference(String ref) {
      for (BacklogItem task : tasks) {
         if (task.getReference().equals(ref)) {
            return task;
         }
      }
      return null;
   }

   public static String getCSVTitleLine() {
      return "Reference," + "Themes," + "Title," + "Summary," + "Status," + "Assigned To," + "Effort," + "Business Value";
   }

   public String toCSV() {
      StringBuilder result = new StringBuilder();
      result.append(reference).append(",");
      for(Theme theme : themes) {
         result.append(theme.getTitle()).append(" ");
      }
      result.append(",");
      result.append(title).append(",");
      result.append(summary).append(",");
      result.append(status.getTitle()).append(",");
      if(assignedTo == null) {
         result.append("unassigned,");
      }
      else {
         result.append(assignedTo.getFullName()).append(",");
      }
      result.append(effort).append(",");
      result.append(businessValue);
      for(AcceptanceCriteria criteria : acceptanceCriteria) {
         result.append("\n");
         result.append(criteria.toCSV());
      }
      return result.toString();
   }
}
