package com.express.service.dto;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;

public class ThemesUpdateRequest implements Serializable {
   private static final long serialVersionUID = 1258448753201038039L;

   private Long projectId;
   
   private List<ThemeDto> themes;

   public ThemesUpdateRequest() {
      themes = new ArrayList<ThemeDto>();
   }

   public Long getProjectId() {
      return projectId;
   }

   public void setProjectId(Long projectId) {
      this.projectId = projectId;
   }

   public List<ThemeDto> getThemes() {
      return themes;
   }

   public void setThemes(List<ThemeDto> themes) {
      this.themes = themes;
   }
}
