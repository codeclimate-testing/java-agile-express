package com.express.domain;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.unitils.UnitilsJUnit4;

import java.util.Calendar;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class EffortRecordTest extends UnitilsJUnit4 {
   private static final Calendar CAL = Calendar.getInstance();
   private static final Iteration ITERATION = new Iteration();
   private static final Integer EFFORT = 1;

   @Test
   public void testConstructorSetsFields() {
       EffortRecord record = new EffortRecord(CAL, EFFORT, ITERATION);
      assertEquals(CAL, record.getDate());
      assertEquals(EFFORT, record.getEffort());
      assertEquals(ITERATION, record.getIteration());
   }
}
