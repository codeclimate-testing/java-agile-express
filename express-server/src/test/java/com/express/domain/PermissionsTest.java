package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * @author Adam Boas
 *         Created on Mar 28, 2009
 */
public class PermissionsTest {
   private static final Log LOG = LogFactory.getLog(PermissionsTest.class);

   @Test
   public void shouldSetAndGetProperties() {
      Permissions perm = new Permissions();
      SetterGetterInvoker<Permissions> setterGetterInvoker = new SetterGetterInvoker<Permissions>(perm);
      setterGetterInvoker.invokeSettersAndGetters();
      LOG.info(perm);
   }
}
