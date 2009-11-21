package com.express.service.mapping;

import com.express.domain.*;
import com.express.service.dto.*;

public interface RemoteObjectFactory {
   
   
   /**
    * Creates a UserDto and maps all fields from the User domain object onto it to a depth indicated
    * by the Policy object.
    * @param user The User who's fields are to be mapped onto the DTO.
    * @param policy The policy to apply during mapping
    * @return the UserDto object which results from the mapping exercise.
    */
   UserDto createUserDto(User user, Policy policy);
   
   /**
    * Creates a ProjectDto and maps all fields from the Project domain object onto it to a depth 
    * indicated by the Policy object.
    * @param project The Project who's fields are to be mapped onto the DTO.
    * @param policy The policy to apply during mapping
    * @return the ProjectDto object which results from the mapping exercise.
    */
   ProjectDto createProjectDto(Project project, Policy policy);
   
   /**
    * Creates a IterationDto and maps all fields from the Iteration domain object onto it to a depth
    * indicated by the Policy object.
    * @param iteration The Iteration who's fields are to be mapped onto the DTO.
    * @param policy The policy to apply during mapping
    * @return the IterationDto object which results from the mapping exercise.
    */
   IterationDto createIterationDto(Iteration iteration, Policy policy);
   
   /**
    * Creates a BacklogItemDto and maps all fields from the BacklogItem domain object onto it to a 
    * depth indicated by the Policy object.
    * @param backlogItem The backlogItem who's fields are to be mapped onto the DTO.
    * @param policy The policy to apply during mapping
    * @return the BacklogItemDto object which results from the mapping exercise.
    */
   BacklogItemDto createBacklogItemDto(BacklogItem backlogItem, Policy policy);
   
   /**
    * Creates a ThemeDto and maps all fields from the Theme domain object onto it.
    * @param theme The theme who's fields are to be mapped onto the DTO.
    * @return the ThemeDto object which results from the mapping exercise.
    */
   ThemeDto createThemeDto(Theme theme);

   AccessRequestDto createAccessRequestDto(AccessRequest request);
}
