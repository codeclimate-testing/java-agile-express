package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class BacklogItemDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<BacklogItemDto> setterGetterInvoker = new SetterGetterInvoker<BacklogItemDto>(new BacklogItemDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
