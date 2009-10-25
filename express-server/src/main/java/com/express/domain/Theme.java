package com.express.domain;

import javax.persistence.*;

@Entity
@Table(name = "theme")
public class Theme implements Persistable, Comparable<Theme> {

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_theme")
   @TableGenerator(name = "gen_theme", table = "sequence_list", pkColumnName = "name",
            valueColumnName = "next_value", allocationSize = 1, initialValue = 100,
            pkColumnValue = "theme")
   @Column(name="theme_id")
   private Long id;

   @Version @Column(name="version_no")
   private Long version;

   @Column(name = "title")
   private String title;

   @Column(name = "description")
   private String description;

   @ManyToOne()
   @JoinColumn(name = "project_id")
   private Project project;

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

   public Project getProject() {
      return project;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public int compareTo(Theme theme) {
      return this.title.compareTo(theme.getTitle());
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof Theme))
         return false;
      Theme theme = (Theme) obj;
      return this.id.equals(theme.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(Theme.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("title=").append(title).append(",");
      output.append("descritpion=").append(description).append("]");
      return output.toString();
   }
}
