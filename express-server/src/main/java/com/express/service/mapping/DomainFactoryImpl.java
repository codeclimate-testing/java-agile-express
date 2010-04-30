package com.express.service.mapping;

import com.express.dao.*;
import com.express.domain.*;
import com.express.service.dto.*;
import net.sf.dozer.util.mapping.MapperIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service("domainFactory")
public class DomainFactoryImpl implements DomainFactory {
   private final MapperIF beanMapper;

   private final UserDao userDao;
   private final ProjectDao projectDao;
   private final IterationDao iterationDao;
   private final BacklogItemDao backlogItemDao;
   private final ThemeDao themeDao;
   private final IssueDao issueDao;
   private final ProjectWorkerDao projectWorkerDao;

   @Autowired
   public DomainFactoryImpl(@Qualifier("beanMapper") MapperIF beanMapper,
                            @Qualifier("userDao") UserDao userDao,
                            @Qualifier("projectDao") ProjectDao projectDao,
                            @Qualifier("iterationDao") IterationDao iterationDao,
                            @Qualifier("backlogItemDao") BacklogItemDao backlogItemDao,
                            @Qualifier("themeDao") ThemeDao themeDao,
                            @Qualifier("issueDao") IssueDao issueDao,
                            @Qualifier("projectWorkerDao") ProjectWorkerDao projectWorkerDao) {
      this.beanMapper = beanMapper;
      this.userDao = userDao;
      this.projectDao = projectDao;
      this.iterationDao = iterationDao;
      this.backlogItemDao = backlogItemDao;
      this.themeDao = themeDao;
      this.issueDao = issueDao;
      this.projectWorkerDao = projectWorkerDao;
   }

   public User createUser(UserDto dto) {
      User user;
      if (dto.getId() == null || dto.getId() == 0) {
         user = (User) beanMapper.map(dto, User.class, Policy.DEEP.getMapId(User.class));
         user.setCreatedDate(Calendar.getInstance());
      }
      else {
         user = userDao.findById(dto.getId());
         beanMapper.map(dto, user, Policy.DEEP.getMapId(User.class));
      }

      return user;
   }

   public Project createProject(ProjectDto dto, Policy policy) {
      Project project;
      if (dto.getId() == null || dto.getId() == 0) {
         project = (Project) beanMapper.map(dto, Project.class, policy.getMapId(Project.class));
      }
      else {
         project = projectDao.findById(dto.getId());
         beanMapper.map(dto, project, policy.getMapId(Project.class));
      }
      return project;
   }

   public Iteration createIteration(IterationDto dto) {
      Iteration iteration;
      if (dto.getId() == null || dto.getId() == 0) {
         iteration = (Iteration) beanMapper.map(dto, Iteration.class, Policy.SHALLOW.getMapId(Iteration.class));
      }
      else {
         iteration = iterationDao.findById(dto.getId());
         beanMapper.map(dto, iteration,Policy.SHALLOW.getMapId(Iteration.class));
      }

      return iteration;
   }

   public BacklogItem createBacklogItem(BacklogItemDto dto) {
      BacklogItem backlogItem;
      if (dto.getId() == null || dto.getId() == 0) {
         backlogItem = (BacklogItem) beanMapper.map(dto, BacklogItem.class, Policy.SHALLOW.getMapId(BacklogItem.class));
      }
      else {
         backlogItem = backlogItemDao.findById(dto.getId());
         boolean assignedToChanged = detectAssignmentChange(dto, backlogItem);
         beanMapper.map(dto, backlogItem, Policy.SHALLOW.getMapId(BacklogItem.class));
         if (assignedToChanged) {
            if (dto.getAssignedTo() != null) {
               backlogItem.setAssignedTo(userDao.findById(dto.getAssignedTo().getId()));
            }
            else {
               backlogItem.setAssignedTo(null);
            }
         }
         if (dto.getImpediment() != null && dto.getImpediment().getResponsible() != null) {
            backlogItem.getImpediment().setResponsible(userDao.findById(dto.getImpediment().getResponsible().getId()));
         }
      }
      return backlogItem;
   }

   public Theme createTheme(ThemeDto dto) {
      Theme theme;
      if (dto.getId() == null || dto.getId() == 0) {
         theme = (Theme) beanMapper.map(dto, Theme.class);
      }
      else {
         theme = themeDao.findById(dto.getId());
         beanMapper.map(dto, theme);
      }
      return theme;
   }

   public ProjectWorker createProjectWorker(ProjectWorkerDto workerDto) {
      ProjectWorker projectWorker;
      if (workerDto.getId() == null || workerDto.getId() == 0) {
         projectWorker = (ProjectWorker) beanMapper.map(workerDto, ProjectWorker.class, Policy.SHALLOW.getMapId(ProjectWorker.class));
      }
      else {
         projectWorker = projectWorkerDao.findById(workerDto.getId());
         beanMapper.map(workerDto, projectWorker, Policy.SHALLOW.getMapId(ProjectWorker.class));
      }
      return projectWorker;
   }

   public Issue createIssue(IssueDto dto) {
      Issue issue;
      if(dto.getId() == null || dto.getId() == 0) {
         issue = (Issue) beanMapper.map(dto, Issue.class);
         if(dto.getResponsible() != null) {
            issue.setResponsible(userDao.findById(dto.getResponsible().getId()));
         }
      }
      else {
         issue = issueDao.findById(dto.getId());
         boolean responsibleChanged = detectResponsibleChange(dto, issue);
         if(responsibleChanged) {
            Long id = dto.getResponsible().getId();
            dto.setResponsible(null);
            beanMapper.map(dto, issue);
            issue.setResponsible(userDao.findById(id));
         }
         else {
            beanMapper.map(dto, issue);
         }
      }
      return issue;
   }

   private boolean detectAssignmentChange(BacklogItemDto dto, BacklogItem item) {
      if (dto.getAssignedTo() == null && item.getAssignedTo() == null) {
         return false;
      }
      else if (dto.getAssignedTo() == null || item.getAssignedTo() == null) {
         return true;
      }
      else {
         return !(dto.getAssignedTo().getId().equals(item.getAssignedTo().getId()));
      }
   }

   private boolean detectResponsibleChange(IssueDto dto, Issue issue) {
      if (dto.getResponsible() == null && issue.getResponsible() == null) {
         return false;
      }
      else if (dto.getResponsible() == null || issue.getResponsible() == null) {
         return true;
      }
      else {
         return !(dto.getResponsible().getId().equals(issue.getResponsible().getId()));
      }
   }
}
