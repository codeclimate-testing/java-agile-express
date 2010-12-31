package com.express.service.mapping.converters;

import com.express.domain.Project;
import com.express.service.dto.ProjectDto;
import com.express.service.dto.ProjectWorkerDto;
import com.googlecode.simpleobjectassembler.converter.AbstractObjectConverter;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectToProjectDtoConverter extends AbstractObjectConverter<Project, ProjectDto> {
   @Override
   public void convert(Project project, ProjectDto projectDto) {
      Collections.sort(projectDto.getProductBacklog());
      Collections.sort(projectDto.getIterations());
      Collections.sort(projectDto.getHistory());
      projectDto.setProjectWorkers(new ArrayList<ProjectWorkerDto>());
   }
}
