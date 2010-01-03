package com.express.service;

import java.util.List;

import com.express.service.dto.*;

public interface ProjectManager {
   
   /**
    * @return List of all ProjectDtos which are available to the currently logged in user. These 
    * Projects will be loaded using a shallow loading policy.
    */
   List<ProjectDto> findAllProjects();
   
   /**
    * @return ProjectAccessData which contains Lists of all ProjectDtos which are available,
    * pending or granted for a user. These projects wil be loaded using a shallow loading policy.
    */
   ProjectAccessData findAccessRequestData();
   
   
   /**
    * @param id of the Project to be found
    * @return Fully loaded ProjectDto using a deep loading policy (Iterations will only be loaded shallowly)
    */
   ProjectDto findProject(Long id);

   /**
    * @param id of the Iteration to be found
    * @return Fully loaded IterationDto using a deep loading policy
    */
   IterationDto findIteration(Long id);
   
   /**
    * Generates an access request for the Project(s) provided or generates a create project request
    * for the system administrator if a new project name is provided in the request object.
    * @param request containing project access request details
    */
   void projectAccessRequest(ProjectAccessRequest request);

   /**
    *
    * @param id of the request to respond to
    * @param response indicating whether to accept the request.
    */
   void projectAccessResponse(Long id, Boolean response);
   
   /**
    * Updates the project fields based on the fields of the ProjectDto provided.
    * @param projectDto with the parameters which should be used for updating the Project
    * @return projectDto which results from the completion of this update
    */
   ProjectDto updateProject(ProjectDto projectDto);
   
   /**
    * Creates a new Iteration based on the fields in the IterationDto provided.
    * @param iterationDto with the parameters which should be used for the creation of the new Iteration.
    * @return iterationDto which results from the completion of this create
    */
   IterationDto createIteration(IterationDto iterationDto);

   /**
    *
    * @param iterationDto representing the Iteration and containing the information to be updated
    * @return iteratioinDto base on the results of this update
    */
   IterationDto updateIteration(IterationDto iterationDto);

   /**
    * Creates a new BacklogItem based on the BacklogItemDto and type in the request provided.
    * @param request with the parameters which should be used for the creation of the new BacklogItem.
    * @return BacklogItemDto containing details to be used in creating BacklogItem
    */
   BacklogItemDto createBacklogItem(CreateBacklogItemRequest request);

   
   /**
    * UPdates the fields in the backlogItem who's identifier matches the dto provided.
    * @param backlogItemDto who's fields should be updated.
    */
   void updateBacklogItem(BacklogItemDto backlogItemDto);
   
   
   /**
    * Removes the BackogItem identified by the id provided from the system. All attached data will
    * be lost.
    * @param id of the backlogItem to remove.
    */
   void removeBacklogItem(Long id);
   
   
   /**
    * Allows a BacklogItem to be assigned to an Iteration. If the iterationFromId field is null it
    * is assumed that the BacklogItem is being assigned from the uncommitedBacklog of it's owning
    * Project.
    * @param request containing the id of the BacklogItem, id of the originating Iteration (or null)
    * and the id of the destination Iteration.
    */
   void backlogItemAssignmentRequest(BacklogItemAssignRequest request);
   
   /**
    * @param request containing all fields required to determine the load loevel
    * @return List of BacklogItems
    */
   List<BacklogItemDto> loadBacklog(LoadBacklogRequest request);

   /**
    * Service to create EffortRecords for all Iterations which are currently active
    */
   void createEffortRecords();

   /**
    * Update the list of themes for a project
    * @param request containing the projectId of the project and the Themes to update the project
    * with.
    */
   void updateThemes(ThemesUpdateRequest request);

   /**
    *
    * @param projectId id of the project whos themes are to be returned
    * @return list of themeDtos for the project identified by the id provided
    */
   List<ThemeDto> loadThemes(Long projectId);

   /**
    *
    * @param projectId the projectId of the project whos access requests are to be returned
    * @return list of AccessRequestDtos for the project identified by the id provided
    */
   List<AccessRequestDto> loadAccessRequests(Long projectId);

   /**
    * Updates the project workers in the project identified by the projectId provided
    * @param request containing the id of the project to update and the list of ProjectWorkers.
    */
   void updateProjectWorkers(ProjectWorkersUpdateRequest request);

   /**
    *
    * @param request containg request perameters
    * @return String containing comma seperated list
    */
   String getCSV(CSVRequest request);

   /**
    * Creates an Issue as an impediment for a BacklogItem adding it to the BacklogItem and the Iteration
    * @param request continaing all information required to create the impediment
    */
   void addImpediment(AddImpedimentRequest request);

   /**
    * Removes the impediment from the BacklogItem provided
    * @param dto representing the BacklogItem to remove the impediment from.
    */
   void removeImpediment(BacklogItemDto dto);
}
