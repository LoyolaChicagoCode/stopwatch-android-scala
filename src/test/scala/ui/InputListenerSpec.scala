package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.view.View
import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar
import common.StopwatchModel

/**
 * A concrete unit test of InputListener that uses stubbing and mocking
 * to replace the real dependencies (collaborators).
 */
class InputListenerSpec extends FunSpec with MockitoSugar {

  /** Factory method for test fixtures. */
  def fixture() = new {
    // create fake instances of the collaborators
    val mdl = mock[StopwatchModel]
    // Scala-specific: one of the collaborators is a stackable trait (mixin).
    // This one is a stub (as opposed to a mock) because we are not asserting
    // or verifying anything on it.
    trait FakeViewUpdater {
      def updateView() = { }
    }
    // create subject-under-test (SUT)
    val adapter = new InputListener with FakeViewUpdater {
      override lazy val model = mdl // injected dependency
    }
  }

  val DUMMY: View = null

  describe("A clickcounter input listener") {
    it("passes onStartStop to the model") {
      // create and import fixture
      val f = fixture()
      import f._
      // exercise SUT
      adapter.onStartStop(DUMMY)
      // verify interaction with the mock
      verify(mdl).onStartStop()
    }
    it("passes onLapReset to the model") {
      // create and import fixture
      val f = fixture()
      import f._
      // exercise SUT
      adapter.onLapReset(DUMMY)
      // verify interaction with the mock
      verify(mdl).onLapReset()
    }
  }
}
