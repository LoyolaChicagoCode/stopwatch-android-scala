package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.view.View
import common._
import edu.luc.etl.cs313.scala.stopwatch.common.ModelStateId.{LAP_STOPPED, LAP_RUNNING, RUNNING, STOPPED}

/**
 * An input listener mixin as part of the Adapter in the Model-View-Adapter pattern.
 * It maps semantic events to interactions with the model. To enable unit testing,
 * this has no class-level dependencies on Android and is separate from the view
 * updater by leaving the updateView method abstract.
 */
trait InputListener {

  protected val model: StopwatchModel

  /**
   * Forwards the semantic ``onStartStop`` event to the model.
   * (Semantic as opposed to, say, a concrete button press.)
   * This and similar events are connected to the
   * corresponding onClick events (actual button presses) in the view itself,
   * usually with the help of the graphical layout editor; the connection also
   * shows up in the XML source of the view layout.
   */
  def onStartStop(view: View): Unit = model.onStartStop()

  /** Forwards the semantic ``onLapReset`` event to the model. */
  def onLapReset(view: View): Unit = model.onLapReset()
}

/**
 * A view updater mixin as part of the Adapter in the Model-View-Adapter pattern.
 * It implements the `updateView` method to update the view from the model.
 */
trait ViewUpdater extends TypedActivity with StopwatchUIUpdateListener {

  /**
   * Updates the seconds and minutes in the UI. It is this UI adapter's
   * responsibility to schedule these incoming events on the UI thread.
   */
  def updateTime(time: Int): Unit = runOnUiThread {
    val tvS = findView(TR.seconds)
    val tvM = findView(TR.minutes)
    val seconds = time % TimeConstants.SEC_PER_MIN
    val minutes = time / TimeConstants.SEC_PER_MIN
    tvS.setText((seconds / 10).toString + (seconds % 10).toString)
    tvM.setText((minutes / 10).toString + (minutes % 10).toString)
  }

  /**
   * Updates the state name shown in the UI. It is this UI adapter's
   * responsibility to schedule these incoming events on the UI thread.
   */
  def updateState(stateId: ModelStateId): Unit = runOnUiThread {
    val stateName = findView(TR.stateName)
    stateName.setText(getString(ViewUpdater.resourceIdFor(stateId)))
  }

  /** Wraps a block of code in a Runnable and runs it on the UI thread. */
  def runOnUiThread(block: => Unit): Unit =
    runOnUiThread(new Runnable() {
      override def run() = block
    })
}

object ViewUpdater {
  import ModelStateId._
  /** Maps a model state to an Android resource ID. */
  val resourceIdFor = Map[ModelStateId, Int](
    STOPPED -> R.string.STOPPED,
    RUNNING -> R.string.RUNNING,
    LAP_RUNNING -> R.string.LAP_RUNNING,
    LAP_STOPPED -> R.string.LAP_STOPPED
  )
}