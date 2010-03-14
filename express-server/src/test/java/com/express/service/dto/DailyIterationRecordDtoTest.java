package com.express.service.dto;

import org.junit.Test;

import com.express.testutils.SetterGetterInvoker;

public class DailyIterationRecordDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<DailyIterationStatusRecordDto> setterGetterInvoker = new SetterGetterInvoker<DailyIterationStatusRecordDto>(new DailyIterationStatusRecordDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
