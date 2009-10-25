package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class ProjectDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectDto> setterGetterInvoker = new SetterGetterInvoker<ProjectDto>(new ProjectDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
