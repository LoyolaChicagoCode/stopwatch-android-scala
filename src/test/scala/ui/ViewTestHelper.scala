package edu.luc.etl.cs313.scala.stopwatch.ui

import android.widget.{Button, TextView}
import edu.luc.etl.cs313.scala.stopwatch.common.TimeConstants._

/**
 * A helper for tests with dependencies on views.
 * Provides auxiliary methods for easy access to UI widgets.
 */
trait ViewTestHelper {

  /** The activity to be provided by concrete subclasses of this test. */
  def activity(): MainActivity

  protected def textViewToInt(t: TextView): Int = t.getText.toString.trim.toInt

  def displayedValue(): Int = {
    val ts = activity.findView(TR.seconds)
    val tm = activity.findView(TR.minutes)
    SEC_PER_MIN * textViewToInt(tm) + textViewToInt(ts)
  }

  protected def startStopButton(): Button = activity.findView(TR.startStop)

  protected def resetLapButton(): Button  = activity.findView(TR.resetLap)


}
