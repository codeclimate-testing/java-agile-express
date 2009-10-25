package com.express.domain;

import javax.persistence.*;

@Entity
@Table(name = "PERMISSIONS")
public class Permissions implements Persistable {

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_PERMISSIONS")
   @TableGenerator(name = "GEN_PERMISSIONS", table = "SEQUENCE_LIST", pkColumnName = "NAME",
            valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 3000,
            pkColumnValue = "PERMISSIONS")
   @Column(name="PERMISSIONS_ID")
   private Long id;

   @Version @Column(name="VERSION_NO")
   private Long version;

   @Column(name = "PROJECT_ADMIN")
   private Boolean projectAdmin = Boolean.FALSE;

   @Column(name = "ITERATION_ADMIN")
   private Boolean iterationAdmin = Boolean.FALSE;
   private static final long serialVersionUID = 2241450015771645982L;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Boolean getIterationAdmin() {
      return iterationAdmin;
   }

   public void setIterationAdmin(Boolean iterationAdmin) {
      this.iterationAdmin = iterationAdmin;
   }

   public Boolean getProjectAdmin() {
      return projectAdmin;
   }

   public void setProjectAdmin(Boolean projectAdmin) {
      this.projectAdmin = projectAdmin;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof Permissions))
         return false;
      Permissions permissions = (Permissions) obj;
      return this.id.equals(permissions.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(Permissions.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("projectAdmin=").append(projectAdmin).append(",");
      output.append("iterationAdmin=").append(iterationAdmin).append("]");
      return output.toString();
   }
}
