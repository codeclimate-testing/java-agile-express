package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class IterationDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<IterationDto> setterGetterInvoker = new SetterGetterInvoker<IterationDto>(new IterationDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
