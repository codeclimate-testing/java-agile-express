package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.util.HashSet;
import java.util.Set;

public class BacklogTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(BacklogTest.class);
   static final String REF = "ref-1";
   static final Theme THEME = new Theme();
   static final String THEME_TITLE = "test";
   static final String TITLE = "test title";
   static final String SUMMARY = "test summary";
   static final String STATUS = Status.DONE.getTitle();
   static final String ASSIGENED_TO = "test user";
   static final Integer EFFORT = 5;
   static final Integer BUSINESS_VALUE = 4;

   BacklogItem item;

   @Before
   public void setUp() {
      item = new BacklogItem();
      THEME.setTitle(THEME_TITLE);
      Set<Theme> themes = new HashSet<Theme>();
      themes.add(THEME);
      item.setThemes(themes);
   }

   @Test
   public void shouldSetAndGetProperties() {
      SetterGetterInvoker<BacklogItem> setterGetterInvoker = new SetterGetterInvoker<BacklogItem>(item);
      setterGetterInvoker.invokeSettersAndGetters();
      LOG.info(item);
   }

   @Test
   public void CSVShouldMatchKeyFields() {
      item.setEffort(EFFORT);
      item.setBusinessValue(BUSINESS_VALUE);
      item.setReference(REF);
      item.setTitle(TITLE);
      item.setSummary(SUMMARY);
      item.setStatus(Status.DONE);
      User user = new User();
      user.setFirstName("test");
      user.setLastName("user");
      item.setAssignedTo(user);
      String expected = REF + "," + THEME_TITLE + " ," + TITLE + "," + SUMMARY + "," + STATUS + "," + ASSIGENED_TO+ ","
                        + EFFORT + "," + BUSINESS_VALUE;
      assertEquals(expected, item.toCSV());
   }

}
