package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class ChangePasswordRequestTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<ChangePasswordRequest> setterGetterInvoker = new SetterGetterInvoker<ChangePasswordRequest>(new ChangePasswordRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
