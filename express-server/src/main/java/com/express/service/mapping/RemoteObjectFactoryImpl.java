package com.express.service.mapping;

import java.util.*;

import net.sf.dozer.util.mapping.MapperIF;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.express.dao.ProjectDao;
import com.express.domain.*;
import com.express.service.dto.*;

@Service("remoteObjectFactory")
public class RemoteObjectFactoryImpl implements RemoteObjectFactory {
   
   private final MapperIF beanMapper;
   private final ProjectDao projectDao;

   @Autowired
   public RemoteObjectFactoryImpl(@Qualifier("beanMapper")MapperIF beanMapper,
                                  @Qualifier("projectDao")ProjectDao projectDao) {
      this.beanMapper = beanMapper;
      this.projectDao = projectDao;
   }

   public UserDto createUserDto(User user, Policy policy) {
      UserDto userDto = (UserDto)beanMapper.map(user, UserDto.class, policy.getMapId(User.class));
      List<Project> projects = projectDao.findAll(user);
      userDto.setHasProjects(projects.size() > 0);
      return userDto;
   }

   public ProjectDto createProjectDto(Project project, Policy policy) {
      ProjectDto projectDto = (ProjectDto)beanMapper.map(project,
                                        ProjectDto.class,
                                        policy.getMapId(Project.class));
      if(policy == Policy.DEEP) {
         Set<String> actors = new HashSet<String>();
         appendActors(actors, project.getProductBacklog());
         for(Iteration iteration : project.getIterations()) {
            appendActors(actors, iteration.getBacklog());
         }
         projectDto.setActors(new ArrayList<String>(actors));
      }
      if(projectDto.getProductBacklog() != null) {
         Collections.sort(projectDto.getProductBacklog());
      }
      if(projectDto.getIterations() != null) {
         Collections.sort(projectDto.getIterations());
      }
      if(projectDto.getProjectWorkers() == null) {
         projectDto.setProjectWorkers(new ArrayList<ProjectWorkerDto>());
      }
      return projectDto;
   }

   private void appendActors(Set<String> actors, Set<BacklogItem> items) {
      for(BacklogItem item : items) {
         actors.add(item.getAsA());
      }
   }

   public IterationDto createIterationDto(Iteration iteration, Policy policy) {
      IterationDto iterationDto = (IterationDto)beanMapper.map(iteration,
                                                               IterationDto.class,
                                                               policy.getMapId(Iteration.class));
      Collections.sort(iterationDto.getBacklog());
      return iterationDto;
   }

   public BacklogItemDto createBacklogItemDto(BacklogItem backlogItem, Policy policy) {
      BacklogItemDto item = (BacklogItemDto)beanMapper.map(backlogItem,
                                            BacklogItemDto.class,
                                            policy.getMapId(BacklogItem.class));
      Collections.sort(item.getThemes());
      return item;
   }

   public ThemeDto createThemeDto(Theme theme) {
      return (ThemeDto)beanMapper.map(theme, ThemeDto.class);
   }

   public AccessRequestDto createAccessRequestDto(AccessRequest request) {
      return (AccessRequestDto)beanMapper.map(request, AccessRequestDto.class);
   }
}
