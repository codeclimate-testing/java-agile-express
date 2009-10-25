package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class ThemeTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(ThemeTest.class);

   @Test
   public void shouldSetAndGetProperties() {
      Theme theme = new Theme();
      LOG.info(theme);
      SetterGetterInvoker<Theme> setterGetterInvoker = new SetterGetterInvoker<Theme>(new Theme());
      setterGetterInvoker.invokeSettersAndGetters();
   }


}