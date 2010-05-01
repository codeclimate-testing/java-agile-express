package com.express.service;

import com.express.dao.BacklogItemDao;
import com.express.dao.IterationDao;
import com.express.dao.ProjectDao;
import com.express.dao.UserDao;
import com.express.domain.*;
import com.express.service.dto.AddImpedimentRequest;
import com.express.service.dto.BacklogItemAssignRequest;
import com.express.service.dto.BacklogItemDto;
import com.express.service.dto.CreateBacklogItemRequest;
import com.express.service.mapping.DomainFactory;
import com.express.service.mapping.Policy;
import com.express.service.mapping.RemoteObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 *
 */
@Service("backlogItemManager")
public class BacklogItemManagerImpl implements BacklogItemManager {

   private final ProjectDao projectDao;

   private final UserDao userDao;

   private final IterationDao iterationDao;

   private final BacklogItemDao backlogItemDao;

   private final DomainFactory domainFactory;

   private final RemoteObjectFactory remoteObjectFactory;

   @Autowired
   public BacklogItemManagerImpl(@Qualifier("projectDao")ProjectDao projectDao,
                                 @Qualifier("userDao")UserDao userDao,
                                 @Qualifier("backlogItemDao")BacklogItemDao backlogItemDao,
                                 @Qualifier("iterationDao")IterationDao iterationDao,
                                 @Qualifier("domainFactory")DomainFactory domainFactory,
                                 @Qualifier("remoteObjectFactory")RemoteObjectFactory remoteObjectFactory) {
      this.projectDao = projectDao;
      this.userDao = userDao;
      this.iterationDao = iterationDao;
      this.backlogItemDao = backlogItemDao;
      this.domainFactory = domainFactory;
      this.remoteObjectFactory = remoteObjectFactory;
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public BacklogItemDto createBacklogItem(CreateBacklogItemRequest request) {
      BacklogItem item = domainFactory.createBacklogItem(request.getBacklogItem());
      if (request.getBacklogItem().getAssignedTo() != null) {
         item.setAssignedTo(userDao.findById(request.getBacklogItem().getAssignedTo().getId()));
      }
      item.makeStatusConsitent();
      Project project = null;
      if (CreateBacklogItemRequest.PRODUCT_BACKLOG_STORY.equals(request.getType())) {
         project = projectDao.findById(request.getParentId());
         project.addBacklogItem(item, true);
      }
      else if (CreateBacklogItemRequest.STORY.equals(request.getType())) {
         Iteration iteration = iterationDao.findById(request.getParentId());
         iteration.addBacklogItem(item, true);
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
   public void markStoryDone(Long id) {
      BacklogItem story = backlogItemDao.findById(id);
      for(BacklogItem task : story.getTasks()) {
         task.setStatus(Status.DONE);
         task.setEffort(0);
      }
      story.setStatus(Status.DONE);
      projectDao.save(story.getProject());
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
            to.addBacklogItem(item, false);
         }
         else {
            project.addBacklogItem(item, false);
         }
      }

      projectDao.save(project);
   }

   @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
   public void addImpediment(AddImpedimentRequest request) {
      BacklogItem item = backlogItemDao.findById(request.getBacklogItemId());
      Issue impediment = domainFactory.createIssue(request.getImpediment());
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
}
