package com.express.domain;

import javax.persistence.*;

/**
 * Models the level of access a User has to a Project.
 *
 * @author adam boas
 */
@Entity
@Table(name = "permissions")
public class Permissions implements Persistable {

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_permissions")
   @TableGenerator(name = "gen_permissions", table = "sequence_list", pkColumnName = "name",
            valueColumnName = "next_value", allocationSize = 1, initialValue = 3000,
            pkColumnValue = "permissions")
   @Column(name="permissions_id")
   private Long id;

   @Version @Column(name="version_no")
   private Long version;

   @Column(name = "project_admin")
   private Boolean projectAdmin = Boolean.FALSE;

   @Column(name = "iteration_admin")
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
