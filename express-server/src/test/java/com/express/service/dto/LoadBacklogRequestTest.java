package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class LoadBacklogRequestTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<LoadBacklogRequest> setterGetterInvoker = new SetterGetterInvoker<LoadBacklogRequest>(new LoadBacklogRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}
