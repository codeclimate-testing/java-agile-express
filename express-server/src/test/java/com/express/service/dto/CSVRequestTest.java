package com.express.service.dto;

import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 */
public class CSVRequestTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<CSVRequest> setterGetterInvoker = new SetterGetterInvoker<CSVRequest>(new CSVRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}