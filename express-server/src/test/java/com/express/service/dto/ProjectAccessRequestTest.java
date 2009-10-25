package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class ProjectAccessRequestTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectAccessRequest> setterGetterInvoker = new SetterGetterInvoker<ProjectAccessRequest>(new ProjectAccessRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
