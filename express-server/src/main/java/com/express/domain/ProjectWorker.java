package com.express.domain;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "PROJECT_WORKER")
public class ProjectWorker implements Persistable, Comparable<ProjectWorker> {

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_WORKER")
   @TableGenerator(name = "GEN_WORKER", table = "SEQUENCE_LIST", pkColumnName = "NAME",
            valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 100,
            pkColumnValue = "WORKER")
   @Column(name="WORKER_ID")
   private Long id;

   @Version @Column(name="VERSION_NO")
   private Long version;

   @Column(name="CREATED_DATE") @Temporal(value = TemporalType.TIMESTAMP)
   private Calendar createdDate;

   @ManyToOne @JoinColumn(name = "PROJECT_ID")
   private Project project;

   @ManyToOne @JoinColumn(name = "USER_ID")
   private User worker;

   @OneToOne(cascade=CascadeType.ALL) @JoinColumn(name = "PERMISSIONS_ID")
   private Permissions permissions;

   public ProjectWorker() {
      this.permissions = new Permissions();
      this.createdDate = Calendar.getInstance();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Project getProject() {
      return project;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public User getWorker() {
      return worker;
   }

   public void setWorker(User worker) {
      this.worker = worker;
   }

   public Permissions getPermissions() {
      return permissions;
   }

   public void setPermissions(Permissions permissions) {
      this.permissions = permissions;
   }

   public Calendar getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Calendar createdDate) {
      this.createdDate = createdDate;
   }

   public int compareTo(ProjectWorker projectWorker) {
      return this.createdDate.compareTo(projectWorker.getCreatedDate());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof ProjectWorker))
         return false;
      ProjectWorker worker = (ProjectWorker) obj;
      return this.id.equals(worker.getId());
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(ProjectWorker.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("projecrt=").append(project).append(",");
      output.append("worker=").append(worker).append(",");
      output.append("createdDate=").append(createdDate).append("]");
      return output.toString();
   }
}
