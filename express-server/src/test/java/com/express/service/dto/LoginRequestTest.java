package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class LoginRequestTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<LoginRequest> setterGetterInvoker = new SetterGetterInvoker<LoginRequest>(new LoginRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
