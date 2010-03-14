package com.express.service.dto;

import com.express.testutils.SetterGetterInvoker;
import org.junit.Test;

public class DailyProjectStatusRecordDtoTest {
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<DailyProjectStatusRecordDto> setterGetterInvoker = new SetterGetterInvoker<DailyProjectStatusRecordDto>(new DailyProjectStatusRecordDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}