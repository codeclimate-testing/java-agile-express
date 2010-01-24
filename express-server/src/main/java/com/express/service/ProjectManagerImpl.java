package com.express.service;

import com.express.dao.*;
import com.express.domain.*;
import com.express.service.dto.*;
import com.express.service.internal.UserService;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.Policy;
import com.express.service.mapping.RemoteObjectFactory;
import com.express.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("projectManager")
@Transactional(readOnly = true)
public class ProjectManagerImpl implements ProjectManager {

   private final ProjectDao projectDao;

   private final UserService userService;

   private final IterationDao iterationDao;

   private final BacklogItemDao backlogItemDao;

   private final UserDao userDao;

   private final AccessRequestDao accessRequestDao;

   private final DomainFactory domainFactory;

   private final RemoteObjectFactory remoteObjectFactory;

   private final NotificationService notificationService;

   @Autowired
   public ProjectManagerImpl(@Qualifier("internalUserService") UserService userService,
                             @Qualifier("projectDao") ProjectDao projectDao,
                             @Qualifier("remoteObjectFactory") RemoteObjectFactory remoteObjectFactory,
                             @Qualifier("domainFactory") DomainFactory domainFactory,
                             @Qualifier("iterationDao") IterationDao iterationDao,
                             @Qualifier("backlogItemDao") BacklogItemDao backlogItemDao,
                             @Qualifier("userDao") UserDao userDao,
                             @Qualifier("accessRequestDao") AccessRequestDao accessRequestDao,
                             @Qualifier("notificationService") NotificationService notificationService) {
      this.projectDao = projectDao;
      this.userService = userService;
      this.remoteObjectFactory = remoteObjectFactory;
      this.domainFactory = domainFactory;
      this.iterationDao = iterationDao;
      this.backlogItemDao = backlogItemDao;
      this.userDao = userDao;
      this.accessRequestDao = accessRequestDao;
      this.notificationService = notificationService;
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public IterationDto createIteration(IterationDto iterationDto) {
      Iteration iteration = domainFactory.createIteration(iterationDto);
      Project project = projectDao.findById(iterationDto.getProject().getId());
      project.addIteration(iteration);
      projectDao.save(project);
      return remoteObjectFactory.createIterationDto(
            project.findIterationByTitle(iteration.getTitle()), Policy.DEEP);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public IterationDto updateIteration(IterationDto iterationDto) {
      Project project = projectDao.findById(iterationDto.getProject().getId());
      Iteration iteration = null;
      for(Iteration existing : project.getIterations()) {
         if(existing.getId().equals(iterationDto.getId())) {
            iteration = existing;
            break;
         }
      }
      if(iteration == null) {
         throw new IllegalArgumentException("Iteration does not exist");
      }
      iteration.setTitle(iterationDto.getTitle());
      iteration.setDescription(iterationDto.getDescription());
      iteration.setStartDate(Calendar.getInstance());
      iteration.getStartDate().setTimeInMillis(iterationDto.getStartDate().getTime());
      iteration.setEndDate(Calendar.getInstance());
      iteration.getEndDate().setTimeInMillis(iterationDto.getEndDate().getTime());
      projectDao.save(iteration.getProject());
      return remoteObjectFactory.createIterationDto(iteration, Policy.DEEP);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public ProjectDto updateProject(ProjectDto projectDto) {
      Project project = domainFactory.createProject(projectDto, Policy.SHALLOW);
      projectDao.save(project);
      return remoteObjectFactory.createProjectDto(project, Policy.DEEP);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public BacklogItemDto createBacklogItem(CreateBacklogItemRequest request) {
      User assignedTo = null;
      if (request.getBacklogItem().getAssignedTo() != null) {
         assignedTo = userDao.findById(request.getBacklogItem().getAssignedTo().getId());
      }
      BacklogItem item = domainFactory.createBacklogItem(request.getBacklogItem());
      item.makeStatusConsitent();
      if (assignedTo != null) {
         item.setAssignedTo(assignedTo);
      }
      Project project = null;
      if (CreateBacklogItemRequest.PRODUCT_BACKLOG_STORY.equals(request.getType())) {
         project = projectDao.findById(request.getParentId());
         project.addBacklogItem(item);
      }
      else if (CreateBacklogItemRequest.STORY.equals(request.getType())) {
         Iteration iteration = iterationDao.findById(request.getParentId());
         iteration.addBacklogItem(item);
         project = iteration.getProject();
      }
      else if (CreateBacklogItemRequest.TASK.equals(request.getType())) {
         BacklogItem parent = backlogItemDao.findById(request.getParentId());
         parent.addTask(item);
         project = parent.getProject();
      }
      item.createReference();
      projectDao.save(project);
      return remoteObjectFactory.createBacklogItemDto(
            project.findBacklogItemByReference(item.getReference()), Policy.DEEP);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void updateBacklogItem(BacklogItemDto backlogItemDto) {
      BacklogItem backlogItem = domainFactory.createBacklogItem(backlogItemDto);
      backlogItem.makeStatusConsitent();
      projectDao.save(backlogItem.getProject());
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void updateImpediment(IssueDto issueDto) {
      Issue issue = domainFactory.createIssue(issueDto);
      projectDao.save(issue.getIteration().getProject());
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void removeBacklogItem(Long id) {
      BacklogItem backlogItem = backlogItemDao.findById(id);
      Project project = backlogItem.getProject();
      if (backlogItem.getParent() != null) {
         BacklogItem parent = backlogItem.getParent();
         parent.removeTask(backlogItem);
      }
      else if (backlogItem.getIteration() != null) {
         Iteration iteration = backlogItem.getIteration();
         iteration.removeBacklogItem(backlogItem);
      }
      else {
         project.removeBacklogItem(backlogItem);
      }
      projectDao.save(project);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void backlogItemAssignmentRequest(BacklogItemAssignRequest request) {
      Project project = null;
      for (Long itemId : request.getItemIds()) {
         BacklogItem item = backlogItemDao.findById(itemId);
         project = item.getProject();
         if (request.getIterationFromId() != null && request.getIterationFromId() != 0) {
            Iteration from = item.getIteration();
            if (from.getId().longValue() != request.getIterationFromId().longValue()) {
               throw new IllegalArgumentException("IterationFromId was not the current owner of " +
                     "the backlogItem you are trying to assign");
            }
            if (!from.removeBacklogItem(item)) {
               throw new IllegalArgumentException("The backlogItem you are trying to assign " +
                     "was not contained in Iteration indicated by your iterationFromId");
            }
         }
         else {
            if (!project.removeBacklogItem(item)) {
               throw new IllegalArgumentException("The backlogItem you are trying to assign " +
                     "was not contained in it's Project's uncommitedBacklog");
            }
         }
         if (request.getIterationToId() != null && request.getIterationToId() != 0) {
            Iteration to = iterationDao.findById(request.getIterationToId());
            to.addBacklogItem(item);
         }
         else {
            project.addBacklogItem(item);
         }
      }

      projectDao.save(project);
   }

   public List<ProjectDto> findAllProjects() {
      List<Project> projects = projectDao.findAll(userService.getAuthenticatedUser());
      List<ProjectDto> dtos = new ArrayList<ProjectDto>();
      for (Project project : projects) {
         dtos.add(remoteObjectFactory.createProjectDto(project, Policy.SHALLOW));
      }
      return dtos;
   }

   public ProjectAccessData findAccessRequestData() {
      User user = userService.getAuthenticatedUser();
      ProjectAccessData data = new ProjectAccessData();

      List<ProjectDto> pending = new ArrayList<ProjectDto>();
      for (AccessRequest request : user.getAccessRequests()) {
         pending.add(remoteObjectFactory.createProjectDto(request.getProject(), Policy.SHALLOW));
      }
      data.setPendingList(pending);

      List<ProjectDto> available = new ArrayList<ProjectDto>();
      for (Project project : projectDao.findAvailable(user)) {
         if (!user.hasPendingRequest(project)) {
            available.add(remoteObjectFactory.createProjectDto(project, Policy.SHALLOW));
         }
      }
      data.setAvailableList(available);

      List<ProjectDto> granted = new ArrayList<ProjectDto>();
      for (Project project : projectDao.findAll(user)) {
         granted.add(remoteObjectFactory.createProjectDto(project, Policy.SHALLOW));
      }
      data.setGrantedList(granted);
      return data;
   }

   public ProjectDto findProject(Long id) {
      Project project = projectDao.findById(id);
      return remoteObjectFactory.createProjectDto(project, Policy.DEEP);
   }

   public IterationDto findIteration(Long id) {
      Iteration iteration = iterationDao.findById(id);
      return remoteObjectFactory.createIterationDto(iteration, Policy.DEEP);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void projectAccessRequest(ProjectAccessRequest request) {
      User user = userService.getAuthenticatedUser();
      if (request.getNewProject() != null) {
         Project project = domainFactory.createProject(request.getNewProject(), Policy.DEEP);
         ProjectWorker worker = new ProjectWorker();
         worker.getPermissions().setProjectAdmin(Boolean.TRUE);
         project.addProjectWorker(worker);
         worker.setWorker(user);
         projectDao.save(project);
         return;
      }
      List<ProjectDto> projects = request.getExistingProjects();
      for (ProjectDto projectDto : projects) {
         Project project = projectDao.findById(projectDto.getId());
         AccessRequest newRequest = new AccessRequest();
         newRequest.setRequestDate(Calendar.getInstance());
         newRequest.setStatus(AccessRequest.UNRESOLVED);
         user.addAccessRequest(newRequest);
         project.addAccessRequest(newRequest);
         for (User manager : project.getProjectManagers()) {
            notificationService.sendProjectAccessRequestNotification(newRequest, manager);
         }
         projectDao.save(project);
      }
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void projectAccessResponse(Long id, Boolean response) {
      AccessRequest request = accessRequestDao.findById(id);
      User requestor = request.getRequestor();
      if (request.getProject().isManager(userService.getAuthenticatedUser())) {
         Project project = request.getProject();
         if (response) {
            ProjectWorker worker = new ProjectWorker();
            worker.setWorker(requestor);
            project.addProjectWorker(worker);
            notificationService.sendProjectAccessAccept(request);
         }
         else {
            notificationService.sendProjectAccessReject(request);
         }
         requestor.removeAccessRequest(request);
         project.removeAccessRequest(request);
         projectDao.save(project);
         userDao.save(requestor);
      }
      else {
         throw new RemoteAccessException("You are not authorized to approve requests");
      }
   }

   public List<BacklogItemDto> loadBacklog(LoadBacklogRequest request) {
      List<BacklogItemDto> backlogDtos = new ArrayList<BacklogItemDto>();
      Set<BacklogItem> backlog;
      if (LoadBacklogRequest.TYPE_ITERATION.equals(request.getType())) {
         Iteration iteration = iterationDao.findById(request.getParentId());
         backlog = iteration.getBacklog();
      }
      else {
         Project project = projectDao.findById(request.getParentId());
         backlog = project.getProductBacklog();
      }
      for (BacklogItem item : backlog) {
         backlogDtos.add(remoteObjectFactory.createBacklogItemDto(item, Policy.DEEP));
      }
      Collections.sort(backlogDtos);
      return backlogDtos;
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void createEffortRecords() {
      List<Iteration> iterations = iterationDao.findOpenIterations();
      for (Iteration iteration : iterations) {
         EffortRecord record =
               new EffortRecord(Calendar.getInstance(), iteration.getTaskEffortRemaining(), iteration);
         iteration.addBurndownRecord(record);
         iteration.calculateDeliveredVelocity();
         projectDao.save(iteration.getProject());
      }
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void updateThemes(ThemesUpdateRequest request) {
      Project project = projectDao.findById(request.getProjectId());
      project.clearThemes();
      for(ThemeDto themeDto : request.getThemes()) {
         project.addTheme(domainFactory.createTheme(themeDto));
      }
      projectDao.save(project);
   }

   public List<ThemeDto> loadThemes(Long projectId) {
      Project project = projectDao.findById(projectId);
      List<ThemeDto> themeDtos = new ArrayList<ThemeDto>();
      for(Theme theme : project.getThemes()) {
         themeDtos.add(remoteObjectFactory.createThemeDto(theme));
      }
      Collections.sort(themeDtos);
      return themeDtos;
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void updateProjectWorkers(ProjectWorkersUpdateRequest request) {
      Project project = projectDao.findById(request.getProjectId());
      project.getProjectWorkers().clear();
      for(ProjectWorkerDto workerDto : request.getWorkers()) {
         project.addProjectWorker(domainFactory.createProjectWorker(workerDto));
      }
      projectDao.save(project);
   }

   public String getCSV(CSVRequest request) {
      if(CSVRequest.TYPE_ITERATION_BACKLOG == request.getType()) {
         return iterationDao.findById(request.getId()).getBacklogAsCSV();
      }
      else {
         return projectDao.findById(request.getId()).getProductBacklogAsCSV();
      }
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void addImpediment(AddImpedimentRequest request) {
      BacklogItem item = backlogItemDao.findById(request.getBacklogItemId());
      Issue impediment = domainFactory.createIssue(request.getImpediment());
      iterationDao.findById(request.getIterationId()).addImpediment(impediment);
      item.setImpediment(impediment);
      projectDao.save(item.getProject());
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void removeImpediment(BacklogItemDto dto) {
      BacklogItem item = backlogItemDao.findById(dto.getId());
      Issue impediment = item.getImpediment();
      impediment.setEndDate(Calendar.getInstance());
      item.setImpediment(null);
      projectDao.save(item.getProject());
   }

   public List<AccessRequestDto> loadAccessRequests(Long projectId) {
      Project project = projectDao.findById(projectId);
      List<AccessRequestDto> requests = new ArrayList<AccessRequestDto>();
      for(AccessRequest request : project.getAccessRequests()) {
         requests.add(remoteObjectFactory.createAccessRequestDto(request));
      }
      return requests;
   }
}
