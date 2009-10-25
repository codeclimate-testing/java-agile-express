package com.express.domain;

import org.unitils.UnitilsJUnit4;
import org.junit.Test;
import com.express.testutils.SetterGetterInvoker;

/**
 * @author Adam Boas
 *         Created on Mar 22, 2009
 */
public class AccessRequestTest extends UnitilsJUnit4 {

   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<AccessRequest> setterGetterInvoker = new SetterGetterInvoker<AccessRequest>(new AccessRequest());
       setterGetterInvoker.invokeSettersAndGetters();
   }
}
