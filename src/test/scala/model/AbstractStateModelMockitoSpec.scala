package edu.luc.etl.cs313.scala.stopwatch
package model

import org.junit.Test
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar
import common.StopwatchUIUpdateListener
import common.ModelStateId._
import clock.ClockModel
import state.StopwatchStateMachine
import time.TimeModel

/**
 * An abstract unit test for the state machine abstraction.
 * This is an interaction test of an object with multiple
 * dependencies; we use various dynamically generated fake
 * objects to satisfy all dependencies and verify that the
 * state machine behaved as expected.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractStateModelMockitoSpec extends JUnitSuite with MockitoSugar {

  trait Dependencies {
    val timeModel: TimeModel
    val clockModel: ClockModel
    val uiUpdateListener: StopwatchUIUpdateListener
  }

  /** Creates an instance of the SUT. */
  def createSUT(dependencies: Dependencies): StopwatchStateMachine

  /** Creates an instance of the home-grown unified mock dependency. */
  def fixtures(): (StopwatchStateMachine, Dependencies) = {
    val dependencies = new Dependencies {
      override val timeModel = mock[TimeModel]
      override val clockModel = mock[ClockModel]
      override val uiUpdateListener = mock[StopwatchUIUpdateListener]
    }
    (createSUT(dependencies), dependencies)
  }

  /**
   * Verifies that we're initially in the stopped state (and told the listener
   * about it).
   */
  @Test def preconditionsAreMet(): Unit = {
    val (model, dependencies) = fixtures()
    model.actionInit()
    verify(dependencies.uiUpdateListener).updateState(STOPPED)
    verify(dependencies.uiUpdateListener).updateTime(0)
    verifyNoMoreInteractions(dependencies.uiUpdateListener, dependencies.clockModel)
  }

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds,
   * expect time 5.
   */
  @Test def scenarioStartWaitStopWorks(): Unit = {
    val (model, dependencies) = fixtures()
    val t = 5
    model.actionInit()
    verify(dependencies.clockModel, never).start()
    verify(dependencies.timeModel, never).incRuntime()
    verify(dependencies.uiUpdateListener).updateState(STOPPED)
    // directly invoke the button press event handler methods
    model.onStartStop()
    verify(dependencies.clockModel).start()
    verify(dependencies.uiUpdateListener).updateState(RUNNING)
    (1 to t) foreach { _ => model.onTick() }
    verify(dependencies.timeModel, times(t)).incRuntime()
    verify(dependencies.uiUpdateListener, times(t + 1)).updateTime(anyInt)
    verifyNoMoreInteractions(dependencies.clockModel)
  }

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds,
   * expect time 5, press lap, wait 4 seconds, expect time 5, press start,
   * expect time 5, press lap, expect time 9, press lap, expect time 0.
   */
  @Test def scenarioStartWaitLapWaitStopLapResetWorks(): Unit = {
    val (model, dependencies) = fixtures()
    val t1 = 5
    val t2 = 9
    model.actionInit()
    verify(dependencies.clockModel, never).start()
    verify(dependencies.clockModel, never).stop()
    verify(dependencies.uiUpdateListener).updateState(STOPPED)
    verify(dependencies.timeModel, never).incRuntime()
    verify(dependencies.uiUpdateListener).updateTime(0)
    when(dependencies.timeModel.getRuntime).thenReturn(t1)
    // directly invoke the button press event handler methods on model
    model.onStartStop()
    verify(dependencies.clockModel).start()
    verify(dependencies.uiUpdateListener).updateState(RUNNING)
    (1 to t1) foreach { _ => model.onTick() }
    verify(dependencies.timeModel, times(t1)).incRuntime()
    verify(dependencies.uiUpdateListener, times(t1)).updateTime(t1)
    when(dependencies.timeModel.getRuntime).thenReturn(t2)
    when(dependencies.timeModel.getLaptime).thenReturn(t1)
    model.onLapReset()
    verify(dependencies.uiUpdateListener).updateState(LAP_RUNNING)
    (t1 + 1 to t2) foreach { _ => model.onTick() }
    verify(dependencies.timeModel, times(t2)).incRuntime()
    verify(dependencies.uiUpdateListener, times(t2)).updateTime(t1)
    model.onStartStop()
    verify(dependencies.clockModel).stop()
    verify(dependencies.uiUpdateListener).updateState(LAP_STOPPED)
    verify(dependencies.timeModel, times(t2)).incRuntime()
    when(dependencies.timeModel.getRuntime).thenReturn(0)
    model.onLapReset()
    verify(dependencies.uiUpdateListener, times(2)).updateState(STOPPED)
    verify(dependencies.uiUpdateListener, times(2)).updateTime(0)
    verifyNoMoreInteractions(dependencies.clockModel)
  }
}
