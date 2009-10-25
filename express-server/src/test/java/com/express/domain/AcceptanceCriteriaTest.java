package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class AcceptanceCriteriaTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(AcceptanceCriteriaTest.class);

   AcceptanceCriteria criteria;

   @Before
   public void setUp() {
      criteria = new AcceptanceCriteria();
   }

   @Test
   public void shouldSetAndGetProperties() {
      SetterGetterInvoker<AcceptanceCriteria> setterGetterInvoker = new SetterGetterInvoker<AcceptanceCriteria>(criteria);
      setterGetterInvoker.invokeSettersAndGetters();
      LOG.info(criteria);
   }

}