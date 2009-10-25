package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class PermissionsDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<PermissionsDto> setterGetterInvoker = new SetterGetterInvoker<PermissionsDto>(new PermissionsDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}