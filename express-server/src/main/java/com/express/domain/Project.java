package com.express.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OptimisticLock;

import javax.persistence.*;
import java.util.*;

/**
 * Project is the core container in the Express system. The product backlog is assigned to the
 * Project and then assigned to it's Iterations when ready to be worked on.
 */
@Entity
@Table(name = "PROJECT")
@NamedQueries({
      @NamedQuery(name = "Project.findAll", query = "SELECT P FROM Project P"),
      @NamedQuery(name = "Project.findNotWorkingOn", query = "SELECT DISTINCT P FROM Project P WHERE P NOT IN(SELECT P FROM Project P JOIN P.projectWorkers PW WHERE PW.worker = ?1)"),
      @NamedQuery(name = "Project.findWorkingOn", query = "SELECT P FROM Project P JOIN P.projectWorkers PW WHERE PW.worker = ?1")
})
public class Project implements Persistable {
   private static final long serialVersionUID = 5917736851219902630L;

   public static final String QUERY_FIND_ALL = "Project.findAll";
   public static final String QUERY_FIND_WORKING_ON = "Project.findWorkingOn";
   public static final String QUERY_FIND_NOT_WORKING_ON = "Project.findNotWorkingOn";

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_PROJECT")
   @TableGenerator(name = "GEN_PROJECT", table = "SEQUENCE_LIST", pkColumnName = "NAME",
         valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 100,
         pkColumnValue = "PROJECT")
   @Column(name = "PROJECT_ID")
   private Long id;

   @Version
   @Column(name = "VERSION_NO")
   private Long version;

   @Column(name = "START_DATE")
   @Temporal(value = TemporalType.TIMESTAMP)
   private Calendar startDate;

   @Column(name = "TITLE")
   private String title;

   @Column(name = "DESCRIPTION")
   private String description;

   @Column(name = "REFERENCE")
   private String reference;

   @Column(name = "EFFORT_UNIT")
   private String effortUnit;

   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
   @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
   @OptimisticLock(excluded = true)
   private Set<ProjectWorker> projectWorkers;

   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
   @OptimisticLock(excluded = true)
   private Set<Iteration> iterations;

   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
   @OptimisticLock(excluded = true)
   private Set<BacklogItem> productBacklog;

   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
   @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
   @OptimisticLock(excluded = true)
   private Set<AccessRequest> accessRequests;

   @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
   @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
   private Set<Theme> themes;

   public Project() {
      projectWorkers = new HashSet<ProjectWorker>();
      iterations = new HashSet<Iteration>();
      productBacklog = new HashSet<BacklogItem>();
      accessRequests = new HashSet<AccessRequest>();
      themes = new HashSet<Theme>();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
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

   public String getEffortUnit() {
      return effortUnit;
   }

   public void setEffortUnit(String effortUnit) {
      this.effortUnit = effortUnit;
   }

   public Set<ProjectWorker> getProjectWorkers() {
      return projectWorkers;
   }

   public void setProjectWorkers(Set<ProjectWorker> projectWorkers) {
      this.projectWorkers = projectWorkers;
   }

   public void addProjectWorker(ProjectWorker projectWorker) {
      projectWorker.setProject(this);
      this.projectWorkers.add(projectWorker);
   }

   public void removeProjectWorker(ProjectWorker projectWorker) {
      projectWorker.setProject(null);
      this.projectWorkers.remove(projectWorker);
   }

   public List<User> getProjectManagers() {
      List<User> managers = new ArrayList<User>();
      for(ProjectWorker worker : projectWorkers) {
         if(worker.getPermissions().getProjectAdmin()) {
            managers.add(worker.getWorker());
         }
      }
      Collections.sort(managers);
      return managers;
   }

   public boolean isManager(User user) {
      for(ProjectWorker worker : projectWorkers) {
         if(worker.getWorker().equals(user)) {
            return true;
         }
      }
      return false;
   }

   public Set<Iteration> getIterations() {
      return iterations;
   }

   public void setIterations(Set<Iteration> iterations) {
      this.iterations = iterations;
   }

   public void addIteration(Iteration iteration) {
      this.iterations.add(iteration);
      iteration.setProject(this);
   }

   public void removeIteration(Iteration iteration) {
      this.iterations.remove(iteration);
      iteration.setProject(null);
   }

   public Set<BacklogItem> getProductBacklog() {
      return this.productBacklog;
   }

   public void setProductBacklog(Set<BacklogItem> productBacklog) {
      this.productBacklog = productBacklog;
   }

   public void addBacklogItem(BacklogItem backlogItem) {
      this.productBacklog.add(backlogItem);
      backlogItem.setProject(this);
      backlogItem.setIteration(null);
   }

   public boolean removeBacklogItem(BacklogItem backlogItem) {
      boolean result = this.productBacklog.remove(backlogItem);
      if (result) {
         backlogItem.setProject(null);
      }
      return result;
   }

   public Set<AccessRequest> getAccessRequests() {
      return accessRequests;
   }

   public void setAccessRequests(Set<AccessRequest> accessRequests) {
      this.accessRequests = accessRequests;
   }

   public void addAccessRequest(AccessRequest accessRequest) {
      this.accessRequests.add(accessRequest);
      accessRequest.setProject(this);
   }

   public void removeAccessRequest(AccessRequest accessRequest) {
      this.accessRequests.remove(accessRequest);
      accessRequest.setProject(null);
   }

   public Set<Theme> getThemes() {
      return themes;
   }

   public void setThemes(Set<Theme> themes) {
      this.themes = themes;
   }

   public void addTheme(Theme theme) {
      this.themes.add(theme);
      theme.setProject(this);
   }

   public boolean removeTheme(Theme theme) {
      boolean result = this.themes.remove(theme);
      if (result) {
         theme.setProject(null);
      }
      return result;
   }

   public void clearThemes() {
      this.themes.clear();
   }

   public int getTotalStoryCount() {
      synchronized (this) {
         int total = 0;
         total += productBacklog.size();
         for (Iteration iteration : iterations) {
            total += iteration.getBacklog().size();
         }
         return total;
      }
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof Project))
         return false;
      Project project = (Project) obj;
      return this.id.equals(project.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(Project.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("reference=").append(reference).append(",");
      output.append("title=").append(title).append(",");
      output.append("startDate=").append(startDate).append("]");
      return output.toString();
   }

   public BacklogItem findBacklogItemByReference(String ref) {
      for (BacklogItem item : productBacklog) {
         if (item.getReference().equals(ref)) {
            return item;
         }
         BacklogItem task = item.findTaskByReference(ref);
         if(task != null) {
            return task;
         }
      }
      BacklogItem item = null;
      for (Iteration iteration : iterations) {
         item = iteration.findBacklogItemByReference(ref);
         if (item != null) {
            return item;
         }
      }
      return item;
   }

   public Iteration findIterationByTitle(String title) {
      for (Iteration iteration : iterations) {
         if (iteration.getTitle().equals(title)) {
            return iteration;
         }
      }
      return null;
   }

}
