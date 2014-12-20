package edu.luc.etl.cs313.scala.stopwatch.ui

import org.junit.Assert._
import org.junit.Test

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractFunctionalTest extends ViewTestHelper {

  @Test def activityExists(): Unit = assertNotNull(activity)

  @Test def activityHasCorrectInitialValue(): Unit = assertEquals(0, displayedValue)

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds, expect time 5.
   *
   * @throws Throwable
   */
  @Test def scenarioStartWaitStopWorks(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, displayedValue)
      assertTrue(startStopButton().performClick())
    }
    Thread.sleep(5500) // <-- do not run this in the UI thread!
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(5, displayedValue)
      assertTrue(startStopButton.performClick())
    }
  }

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds,
   * expect time 5, press lap, wait 4 seconds, expect time 5, press start,
   * expect time 5, press lap, expect time 9, press lap, expect time 0.
   *
   * @throws Throwable
   */
  @Test def scenarioStartWaitLapWaitStopLapResetWorks(): Unit = {
    activity.runOnUiThread {
      assertEquals(0, displayedValue)
      assertTrue(startStopButton.performClick())
    }
    Thread.sleep(5500) // <-- do not run this in the UI thread!
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(5, displayedValue)
      assertTrue(resetLapButton.performClick())
    }
    Thread.sleep(4000) // <-- do not run this in the UI thread!
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(5, displayedValue)
      assertTrue(startStopButton.performClick())
    }
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(5, displayedValue)
      assertTrue(resetLapButton.performClick())
    }
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(9, displayedValue)
      assertTrue(resetLapButton.performClick())
    }
    runUiThreadTasks()
    activity.runOnUiThread {
      assertEquals(0, displayedValue)
    }
  }

  /**
   * Explicitly runs tasks scheduled to run on the UI thread in case this is required
   * by the testing framework, e.g., Robolectric. When this is not required,
   * it should be overridden with an empty method.
   */
  protected def runUiThreadTasks(): Unit
}
