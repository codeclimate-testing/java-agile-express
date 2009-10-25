package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class UserDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<UserDto> setterGetterInvoker = new SetterGetterInvoker<UserDto>(new UserDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
