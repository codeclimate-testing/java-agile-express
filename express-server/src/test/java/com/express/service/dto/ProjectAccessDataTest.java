package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class ProjectAccessDataTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectAccessData> setterGetterInvoker = new SetterGetterInvoker<ProjectAccessData>(new ProjectAccessData());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}