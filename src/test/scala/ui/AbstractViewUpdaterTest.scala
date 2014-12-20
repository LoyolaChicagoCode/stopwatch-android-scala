package edu.luc.etl.cs313.scala.stopwatch
package ui

import org.junit.Test
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import common.ModelStateId

/**
 * An abstract unit test for the view updater.
 * This is an interaction test of an object with dependencies
 * (collaborators). Specifically to Scala, one of the dependencies
 * is a stackable trait (mixin); we can handle this by mixing in
 * a test spy wrapper between the actual SUT and the injected activity.
 * This test follows the XUnit Testcase Superclass pattern.
 */
trait AbstractViewUpdaterTest extends MockitoSugar with ViewTestHelper {

  trait TypedActivitySpy extends TypedActivityHolder {
    lazy val activitySpy = spy(activity)
    override def findView[T](id: TypedResource[T]): T = activitySpy.findView(id)
  }

  @Test def viewUpdaterAccessesTimeTextViews(): Unit = {
    // create subject-under-test (SUT)
    val updater = new ViewUpdater with TypedActivitySpy
    // exercise SUT
    updater.updateTime(1)
    // verify interaction with the mock
    verify(updater.activitySpy).findView(TR.seconds)
    verify(updater.activitySpy).findView(TR.minutes)
  }

  @Test def viewUpdaterAccessesStateName(): Unit = {
    // create subject-under-test (SUT)
    val updater = new ViewUpdater with TypedActivitySpy
    // exercise SUT
    updater.updateState(ModelStateId.RUNNING)
    // verify interaction with the mock
    verify(updater.activitySpy).findView(TR.stateName)
  }
}
