package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class ProjectWorkerDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectWorkerDto> setterGetterInvoker = new SetterGetterInvoker<ProjectWorkerDto>(new ProjectWorkerDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}