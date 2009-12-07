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
   static final String REF = "ref-1";
   static final String TITLE = "test title";
   static final String SUMMARY = "test summary";
   static final String STATUS = Status.DONE.getTitle();
   static final String ASSIGENED_TO = "test user";

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

   @Test
   public void CSVShouldMatcheKeyFields() {
      item.setReference(REF);
      item.setTitle(TITLE);
      item.setSummary(SUMMARY);
      item.setStatus(Status.DONE);
      User user = new User();
      user.setFirstName("test");
      user.setLastName("user");
      item.setAssignedTo(user);
      String expected = REF + "," + TITLE + "," + SUMMARY + "," + STATUS + "," + ASSIGENED_TO;
      assertEquals(expected, item.toCSV());
   }

}
