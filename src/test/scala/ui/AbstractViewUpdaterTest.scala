package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.app.Activity
import org.junit.Test
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import common.ModelStateId

/**
 * An abstract GUI-based functional test for the clickcounter app.
 * This follows the XUnit Testcase Superclass pattern.
 */
trait AbstractViewUpdaterTest extends MockitoSugar with ViewTestHelper {

  // Scala-specific: one of the collaborators is a stackable trait (mixin).
  // We can address this by inserting a test spy wrapper between
  // the actual SUT and the mixin.
  trait FakeTypedActivityHolder extends Activity with TypedActivityHolder {
    lazy val activitySpy = spy(activity)
    override def findView[T](id: TypedResource[T]): T = activitySpy.findView(id)
  }

  @Test def viewUpdaterAccessesTimeTextViews(): Unit = {
    // create subject-under-test (SUT)
    val updater = new ViewUpdater with FakeTypedActivityHolder
    // exercise SUT
    updater.updateTime(1)
    // verify interaction with the mock
    verify(updater.activitySpy).findView(TR.seconds)
    verify(updater.activitySpy).findView(TR.minutes)
  }

  @Test def viewUpdaterAccessesStateName(): Unit = {
    // create subject-under-test (SUT)
    val updater = new ViewUpdater with FakeTypedActivityHolder
    // exercise SUT
    updater.updateState(ModelStateId.RUNNING)
    // verify interaction with the mock
    verify(updater.activitySpy).findView(TR.stateName)
  }
}
