package com.express.service;

import static org.easymock.EasyMock.expect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.unitils.UnitilsJUnit4;
import org.unitils.easymock.EasyMockUnitils;
import static org.unitils.easymock.EasyMockUnitils.replay;
import org.unitils.easymock.annotation.Mock;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.Authentication;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

import com.express.dao.*;
import com.express.domain.*;
import com.express.service.dto.*;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.Policy;
import com.express.service.mapping.RemoteObjectFactory;
import com.express.service.notification.NotificationService;
import com.express.service.internal.UserService;

import java.util.ArrayList;
import java.util.List;

public class ProjectManagerImplTest extends UnitilsJUnit4 {

   static final Long ID = 1l;
   @Mock
   private ProjectDao projectDao;
   @Mock
   private IterationDao iterationDao;
   @Mock
   private BacklogItemDao backlogItemDao;
   @Mock
   private UserDao userDao;
   @Mock
   private UserService userService;
   @Mock
   private DomainFactory domainFactory;
   @Mock
   private RemoteObjectFactory remoteObjectFactory;
   @Mock
   NotificationService notificationService;
   @Mock
   AccessRequestDao accessRequestDao;
   
   private ProjectManager projectManager;

   @Before
   public void setUp() {
      projectManager = new ProjectManagerImpl(userService,
                                              projectDao,
                                              remoteObjectFactory,
                                              domainFactory,
                                              iterationDao,
                                              backlogItemDao,
                                              userDao,
                                              accessRequestDao,
                                              notificationService);
   }

   private void intializeSecureContext() {
      User user = new User();
      user.setId(ID);
      Authentication auth = new UsernamePasswordAuthenticationToken(user, "xxx");
      SecurityContextHolder.getContext().setAuthentication(auth);
   }
   
   @Test
   public void testBasicCreateProject() {
      ProjectDto dto = new ProjectDto();
      Project domain = new Project();
      expect(domainFactory.createProject(dto,Policy.SHALLOW)).andReturn(domain);
      projectDao.save(domain);
      expect(remoteObjectFactory.createProjectDto(domain, Policy.DEEP)).andReturn(dto);
      EasyMockUnitils.replay();
      projectManager.updateProject(dto);
   }
   
   @Test
   public void testBasicCreateIteration() {
      String title = "test title";
      ProjectDto projectDto = new ProjectDto();
      Long projectId = 1l;
      projectDto.setId(projectId);
      Project project = new Project();
      IterationDto dto = new IterationDto();
      dto.setTitle(title);
      dto.setProject(projectDto);
      Iteration domain = new Iteration();
      domain.setTitle(title);
      expect(domainFactory.createIteration(dto)).andReturn(domain);
      expect(projectDao.findById(projectId)).andReturn(project);
      projectDao.save(project);
      expect(remoteObjectFactory.createIterationDto(domain, Policy.DEEP)).andReturn(dto);
      EasyMockUnitils.replay();
      projectManager.createIteration(dto);
   }

   @Test
   public void testCreateEffortRecords() {
      Project project = new Project();
      List<Iteration> iterations = new ArrayList<Iteration>();
      Iteration iteration = new Iteration();
      iteration.setProject(project);
      iterations.add(iteration);
      iteration = new Iteration();
      iteration.setProject(project);
      iterations.add(iteration);
      expect(iterationDao.findOpenIterations()).andReturn(iterations);
      projectDao.save(project);
      projectDao.save(project);
      EasyMockUnitils.replay();
      projectManager.createEffortRecords();
   }

   @Test
   public void testFindAllProjects() {
      this.intializeSecureContext();
      Project project = new Project();
      List<Project> projects = new ArrayList<Project>();
      projects.add(project);
      User user = new User();
      user.setId(ID);
      expect(userService.getAuthenticatedUser()).andReturn(user);
      expect(projectDao.findAll(user)).andReturn(projects);
      expect(remoteObjectFactory.createProjectDto(project, Policy.SHALLOW)).andReturn(new ProjectDto());
      EasyMockUnitils.replay();
      List<ProjectDto> dtos = projectManager.findAllProjects();
      assertEquals(projects.size(), dtos.size());
   }

   @Test
   public void testFindProject() {
      Project project = new Project();
      expect(projectDao.findById(ID)).andReturn(project);
      expect(remoteObjectFactory.createProjectDto(project, Policy.DEEP)).andReturn(new ProjectDto());
      EasyMockUnitils.replay();
      projectManager.findProject(ID);
   }
   
   @Test
   public void createUncommitedBacklogItemUnassigned() {
      CreateBacklogItemRequest request = new CreateBacklogItemRequest();
      BacklogItemDto itemDto = new BacklogItemDto();
      request.setBacklogItem(itemDto);
      request.setType(CreateBacklogItemRequest.UNCOMMITED_STORY);
      Long ID = 1l;
      request.setParentId(ID);
      BacklogItem item = new BacklogItem();
      item.setReference("S-1");
      Project project = new Project();

      expect(domainFactory.createBacklogItem(itemDto)).andReturn(item);
      expect(projectDao.findById(ID)).andReturn(project);
      projectDao.save(project);
      expect(remoteObjectFactory.createBacklogItemDto(item, Policy.DEEP)).andReturn(new BacklogItemDto());
      EasyMockUnitils.replay();
      projectManager.createBacklogItem(request);
   }

   @Test
   public void testRemoveBacklogItem() {
      BacklogItem item = new BacklogItem();
      Long id = 1l;
      Project project = new Project();
      project.addBacklogItem(item);

      expect(backlogItemDao.findById(id)).andReturn(item);
      projectDao.save(project);
      EasyMockUnitils.replay();
      projectManager.removeBacklogItem(id);
   }

   @Test
   public void testUpdateBacklogItem() {
      BacklogItemDto itemDto = new BacklogItemDto();
      BacklogItem item = new BacklogItem();
      Project project = new Project();
      project.addBacklogItem(item);


      expect(domainFactory.createBacklogItem(itemDto)).andReturn(item);
      projectDao.save(project);
      EasyMockUnitils.replay();
      projectManager.updateBacklogItem(itemDto);
   }

   @Test
   public void testUpdateThemes() {
      Long projectId = 1l;
      Project project = new Project();
      ThemesUpdateRequest request = new ThemesUpdateRequest();
      ThemeDto dto = new ThemeDto();
      request.getThemes().add(dto);
      request.setProjectId(projectId);
      expect(projectDao.findById(projectId)).andReturn(project);
      expect(domainFactory.createTheme(dto)).andReturn(new Theme());
      projectDao.save(project);
      EasyMockUnitils.replay();
      projectManager.updateThemes(request);
   }

   @Test
   public void testLoadThemes() {
      Long projectId = 1l;
      Theme theme = new Theme();
      Project project = new Project();
      project.addTheme(theme);
      expect(projectDao.findById(projectId)).andReturn(project);
      expect(remoteObjectFactory.createThemeDto(theme)).andReturn(new ThemeDto());
      replay();
      assertEquals(1, projectManager.loadThemes(projectId).size());
   }

   @Test
   public void testLoadProductBacklog() {
      Long projectId = 1l;
      Project project = new Project();
      BacklogItem item = new BacklogItem();
      project.addBacklogItem(item);
      LoadBacklogRequest request = new LoadBacklogRequest();
      request.setType(LoadBacklogRequest.TYPE_PROJECT);
      request.setParentId(projectId);
      expect(projectDao.findById(projectId)).andReturn(project);
      expect(remoteObjectFactory.createBacklogItemDto(item, Policy.DEEP)).andReturn(new BacklogItemDto());
      replay();
      assertEquals(1, projectManager.loadBacklog(request).size());
   }

   @Test
   public void testLoadIterationBacklog() {
      Long iterationId = 1l;
      Iteration iteration = new Iteration();
      BacklogItem item = new BacklogItem();
      iteration.addBacklogItem(item);
      LoadBacklogRequest request = new LoadBacklogRequest();
      request.setType(LoadBacklogRequest.TYPE_ITERATION);
      request.setParentId(iterationId);
      expect(iterationDao.findById(iterationId)).andReturn(iteration);
      expect(remoteObjectFactory.createBacklogItemDto(item, Policy.DEEP)).andReturn(new BacklogItemDto());
      replay();
      assertEquals(1, projectManager.loadBacklog(request).size());
   }

   @Test
   public void testUpdateProjectWorkers() {
      ProjectWorkersUpdateRequest request = new ProjectWorkersUpdateRequest();
      Project project = new Project();
      request.setProjectId(1l);
      List<ProjectWorkerDto> workers = new ArrayList<ProjectWorkerDto>();
      ProjectWorkerDto worker = new ProjectWorkerDto();
      workers.add(worker);
      request.setWorkers(workers);
      expect(projectDao.findById(request.getProjectId())).andReturn(project);
      expect(domainFactory.createProjectWorker(worker)).andReturn(new ProjectWorker());
      projectDao.save(project);
      replay();
      projectManager.updateProjectWorkers(request);
      assertEquals(1, project.getProjectWorkers().size());
   }

   @Test
   public void testLoadAccessRequests() {
      Long projectId = 1l;
      Project project = new Project();
      AccessRequest accessRequest = new AccessRequest();
      project.addAccessRequest(accessRequest);
      expect(projectDao.findById(projectId)).andReturn(project);
      expect(remoteObjectFactory.createAccessRequestDto(accessRequest)).andReturn(new AccessRequestDto());
      replay();
      assertEquals(1, projectManager.loadAccessRequests(projectId).size());
   }

   @Test
   public void csvrequestShouldSwitchOnIterationType() {
      CSVRequest request = new CSVRequest();
      Long id = 1l;
      request.setId(id);
      request.setType(CSVRequest.TYPE_ITERATION_BACKLOG);
      expect(iterationDao.findById(id)).andReturn(new Iteration());
      replay();
      projectManager.getCSV(request);
   }

   @Test
   public void csvrequestShouldSwitchProductOnType() {
      CSVRequest request = new CSVRequest();
      Long id = 1l;
      request.setId(id);
      request.setType(CSVRequest.TYPE_PRODUCT_BACKLOG);
      expect(projectDao.findById(id)).andReturn(new Project());
      replay();
      projectManager.getCSV(request);
   }

}
