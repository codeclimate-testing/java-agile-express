package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

   @Test
   public void shouldReturnCompletedPointsOnlyForStoriesWhichAreDone() {
      BacklogItem story = new BacklogItem();
      int effort = 10;
      story.setEffort(effort);
      iteration.addBacklogItem(story, false);
      assertThat("completed story points", iteration.getStoryPointsCompleted(), equalTo(0));
      story.setStatus(Status.DONE);
      assertThat("completed story points", iteration.getStoryPointsCompleted(), equalTo(effort));
   }

   @Test
   public void shouldReturnStoryPointsForAllStories() {
      BacklogItem story = new BacklogItem();
      int effort = 10;
      story.setEffort(effort);
      assertThat("story points before adding story", iteration.getStoryPoints(), equalTo(0));
      iteration.addBacklogItem(story, false);
      assertThat("story points after adding story", iteration.getStoryPoints(), equalTo(effort));
   }

}
