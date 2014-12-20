package edu.luc.etl.cs313.scala.stopwatch
package ui

import android.view.View
import org.mockito.Mockito._
import org.scalatest.FunSpec
import org.scalatest.mock.MockitoSugar
import common.StopwatchModel

/**
 * A concrete unit test for InputListener that uses stubbing and mocking
 * to replace the real dependencies (collaborators) and test the
 * interactions.
 */
class InputListenerSpec extends FunSpec with MockitoSugar {

  /** Factory method for test fixtures. */
  def fixture() = new {
    // create fake instances of the collaborators
    val mdl = mock[StopwatchModel]
    // create subject-under-test (SUT) and inject fake dependency
    val adapter = new InputListener {
      override lazy val model = mdl // injected fake dependency
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
