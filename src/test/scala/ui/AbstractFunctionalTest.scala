package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.widget.{Button, TextView}
import org.junit.Assert._
import org.junit.Test
import common.TimeConstants.SEC_PER_MIN

/**
 * Abstract GUI-level test superclass of several essential stopwatch scenarios.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractFunctionalTest {

  /** The activity to be provided by concrete subclasses of this test. */
  protected def activity(): MainActivity

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

  // auxiliary methods for easy access to UI widgets

  protected def textViewToInt(t: TextView): Int = t.getText.toString.trim.toInt

  def displayedValue(): Int = {
    val ts = activity.findView(TR.seconds)
    val tm = activity.findView(TR.minutes)
    SEC_PER_MIN * textViewToInt(tm) + textViewToInt(ts)
  }

  protected def startStopButton(): Button = activity.findView(TR.startStop)

  protected def resetLapButton(): Button  = activity.findView(TR.resetLap)

  /**
   * Explicitly runs tasks scheduled to run on the UI thread in case this is required
   * by the testing framework, e.g., Robolectric. When this is not required,
   * it should be overridden with an empty method.
   */
  protected def runUiThreadTasks(): Unit
}
