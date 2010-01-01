package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class IssueTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(IssueTest.class);

   Issue criteria;

   @Before
   public void setUp() {
      criteria = new Issue();
   }

   @Test
   public void shouldSetAndGetProperties() {
      SetterGetterInvoker<Issue> setterGetterInvoker = new SetterGetterInvoker<Issue>(criteria);
      setterGetterInvoker.invokeSettersAndGetters();
      LOG.info(criteria);
   }

}