package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class EffortRecordDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<EffortRecordDto> setterGetterInvoker = new SetterGetterInvoker<EffortRecordDto>(new EffortRecordDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
