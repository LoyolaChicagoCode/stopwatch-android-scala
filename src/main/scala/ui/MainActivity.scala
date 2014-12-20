package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import common._
import model.ConcreteStopwatchModelFacade

/**
 * The main Android activity, which provides the required lifecycle methods.
 * By mixing in the abstract Adapter behavior, this class serves as the Adapter
 * in the Model-View-Adapter pattern. It connects the Android GUI view with the
 * model. The model implementation is configured externally via the resource
 * R.string.model_class.
 */
class MainActivity extends Activity with InputListener with ViewUpdater {

  private val TAG = "stopwatch-android-scala"

  /**
   * The model on which this activity depends. The model also depends on
   * this activity; we inject this dependency using abstract member override.
   */
  protected val model: StopwatchModel = new ConcreteStopwatchModelFacade {
    lazy val listener = MainActivity.this
  }

  override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    Log.i(TAG, "onCreate")
    // inject the (implicit) dependency on the view
    setContentView(R.layout.main)
  }

  override def onStart() = {
    super.onStart()
    Log.i(TAG, "onStart")
    model.onStart()
  }
}