package com.express.service;

import com.express.dao.BacklogItemDao;
import com.express.dao.IterationDao;
import com.express.dao.ProjectDao;
import com.express.dao.UserDao;
import com.express.domain.*;
import com.express.service.dto.AddImpedimentRequest;
import com.express.service.dto.BacklogItemDto;
import com.express.service.dto.CreateBacklogItemRequest;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.Policy;
import com.express.service.mapping.RemoteObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 *
 */
public class BacklogItemManagerImplTest {

   @Mock
   private ProjectDao projectDao;
   @Mock
   private IterationDao iterationDao;
   @Mock
   private UserDao userDao;
   @Mock
   private BacklogItemDao backlogItemDao;
   @Mock
   private DomainFactory domainFactory;
   @Mock
   private RemoteObjectFactory remoteObjectFactory;

   private BacklogItemManager backlogItemManager;

   @Before
   public void setUp() {
      MockitoAnnotations.initMocks(this);
      backlogItemManager = new BacklogItemManagerImpl(projectDao, userDao, backlogItemDao,
            iterationDao, domainFactory, remoteObjectFactory);
   }

   @Test
   public void shouldCreateUnassignedProductBacklogItem() {
      CreateBacklogItemRequest request = new CreateBacklogItemRequest();
      BacklogItemDto itemDto = new BacklogItemDto();
      request.setBacklogItem(itemDto);
      request.setType(CreateBacklogItemRequest.PRODUCT_BACKLOG_STORY);
      Long ID = 1l;
      request.setParentId(ID);
      BacklogItem item = new BacklogItem();
      item.setReference("S-1");
      Project project = new Project();

      given(domainFactory.createBacklogItem(itemDto)).willReturn(item);
      given(projectDao.findById(ID)).willReturn(project);
      projectDao.save(project);
      given(remoteObjectFactory.createBacklogItemDto(item, Policy.DEEP)).willReturn(new BacklogItemDto());

      backlogItemManager.createBacklogItem(request);
   }

   @Test
   public void shouldRemoveBacklogItem() {
      BacklogItem item = new BacklogItem();
      Long id = 1l;
      Project project = new Project();
      project.addBacklogItem(item, true);

      given(backlogItemDao.findById(id)).willReturn(item);
      projectDao.save(project);

      backlogItemManager.removeBacklogItem(id);
   }

   @Test
   public void shouldUpdateBacklogItem() {
      BacklogItemDto itemDto = new BacklogItemDto();
      BacklogItem item = new BacklogItem();
      Project project = new Project();
      project.addBacklogItem(item, true);
      given(domainFactory.createBacklogItem(itemDto)).willReturn(item);
      projectDao.save(project);

      backlogItemManager.updateBacklogItem(itemDto);
   }

   @Test
   public void shouldMarkStoryDoneWhenMarkStoryDoneCalled() {
      long id = 1l;
      BacklogItem story = createStoryWithTasksWithStatusOpen();
      given(backlogItemDao.findById(id)).willReturn(story);
      backlogItemManager.markStoryDone(id);
      assertEquals(Status.DONE, story.getStatus());
   }

   @Test
   public void shouldMarkAllTasksDoneWhenMarkingStoryDone() {
      long id = 1l;
      BacklogItem story = createStoryWithTasksWithStatusOpen();
      given(backlogItemDao.findById(id)).willReturn(story);
      backlogItemManager.markStoryDone(id);
      for(BacklogItem task : story.getTasks()){
         assertEquals(Status.DONE, task.getStatus());
      }
   }

   private BacklogItem createStoryWithTasksWithStatusOpen() {
      BacklogItem story = new BacklogItem();
      BacklogItem task1 = new BacklogItem();
      task1.setStatus(Status.OPEN);
      story.addTask(task1);
      BacklogItem task2 = new BacklogItem();
      task2.setStatus(Status.OPEN);
      story.addTask(task2);
      return story;
   }

   @Test
   public void shouldCreateImpedimentForStoryBasedOnRequest() {
      AddImpedimentRequest request = new AddImpedimentRequest();
      long id = 1l;
      request.setBacklogItemId(id);

      BacklogItem story = new BacklogItem();
      Project project = new Project();
      Iteration iteration = new Iteration();
      story.setProject(project);
      //story.setIteration(iteration);
      given(backlogItemDao.findById(id)).willReturn(story);
      given(domainFactory.createIssue(request.getImpediment())).willReturn(new Issue());
      projectDao.save(project);
      backlogItemManager.addImpediment(request);
   }
}