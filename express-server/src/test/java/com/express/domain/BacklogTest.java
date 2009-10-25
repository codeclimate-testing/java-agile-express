package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class BacklogTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(BacklogTest.class);

   BacklogItem item;

   @Before
   public void setUp() {
      item = new BacklogItem();
   }

   @Test
   public void shouldSetAndGetProperties() {
      SetterGetterInvoker<BacklogItem> setterGetterInvoker = new SetterGetterInvoker<BacklogItem>(item);
      setterGetterInvoker.invokeSettersAndGetters();
      LOG.info(item);
   }

}
