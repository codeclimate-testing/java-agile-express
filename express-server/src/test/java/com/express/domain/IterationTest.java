package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class IterationTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(IterationTest.class);

   Iteration iteration;

   @Before
   public void setUp() {
      iteration = new Iteration();
      LOG.info(iteration);
   }
   
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<Iteration> setterGetterInvoker = new SetterGetterInvoker<Iteration>(new Iteration());
       setterGetterInvoker.invokeSettersAndGetters();
   }

}
