package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class CreateBacklogItemRequestTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<CreateBacklogItemRequest> setterGetterInvoker = new SetterGetterInvoker<CreateBacklogItemRequest>(new CreateBacklogItemRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}
