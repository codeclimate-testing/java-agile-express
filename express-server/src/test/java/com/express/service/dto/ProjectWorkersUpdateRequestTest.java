package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 */
public class ProjectWorkersUpdateRequestTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ProjectWorkersUpdateRequest> setterGetterInvoker = new SetterGetterInvoker<ProjectWorkersUpdateRequest>(new ProjectWorkersUpdateRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}