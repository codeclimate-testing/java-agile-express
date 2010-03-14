package com.express.domain;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.unitils.UnitilsJUnit4;

import java.util.Calendar;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class DailyIterationStatusRecordTest extends UnitilsJUnit4 {
   private static final Calendar CAL = Calendar.getInstance();
   private static final Iteration ITERATION = new Iteration();
   private static final Integer EFFORT = 1;
   private static final Integer TOTAL_POINTS = 2;
   private static final Integer COMPLETED_POINTS = 3;

   @Test
   public void testConstructorSetsFields() {
       DailyIterationStatusRecord record = new DailyIterationStatusRecord(CAL, EFFORT, TOTAL_POINTS, COMPLETED_POINTS, ITERATION);
      assertEquals(CAL, record.getDate());
      assertEquals(EFFORT, record.getTaskHoursRemaining());
      assertEquals(ITERATION, record.getIteration());
      assertEquals(TOTAL_POINTS, record.getTotalPoints());
      assertEquals(COMPLETED_POINTS, record.getCompletedPoints());
   }
}
