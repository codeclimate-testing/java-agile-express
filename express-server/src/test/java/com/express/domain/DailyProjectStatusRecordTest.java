package com.express.domain;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * @author Adam Boas
 *         Created on Mar 23, 2009
 */
public class DailyProjectStatusRecordTest extends UnitilsJUnit4 {
   private static final Calendar CAL = Calendar.getInstance();
   private static final Project PROJECT = new Project();
   private static final Integer TOTAL_POINTS = 2;
   private static final Integer COMPLETED_POINTS = 3;

   @Test
   public void testConstructorSetsFields() {
       DailyProjectStatusRecord record = new DailyProjectStatusRecord(CAL, TOTAL_POINTS, COMPLETED_POINTS, PROJECT);
      assertEquals(CAL, record.getDate());
      assertEquals(TOTAL_POINTS, record.getTotalPoints());
      assertEquals(COMPLETED_POINTS, record.getCompletedPoints());
      assertEquals(PROJECT, record.getProject());
   }
}