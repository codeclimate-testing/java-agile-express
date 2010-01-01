package com.express.service.dto;

import com.express.testutils.SetterGetterInvoker;
import org.junit.Test;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class IssueDtoTest {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<IssueDto> setterGetterInvoker = new SetterGetterInvoker<IssueDto>(new IssueDto());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}