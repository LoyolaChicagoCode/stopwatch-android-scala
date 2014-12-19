package edu.luc.etl.cs313.scala.stopwatch
package model

import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import common.{ModelStateId, StopwatchUIUpdateListener}
import clock.ClockModel
import state.StopwatchStateMachine
import time.{DefaultTimeModel, TimeModel}
import ModelStateId._

/**
 * An abstract unit test for the state machine abstraction.
 * This is a unit test of an object with multiple dependencies.
 * We use a unified fake object to satisfy all dependencies
 * and verify that the state machine behaved as expected;
 * this design is questionable but acceptable in this relatively
 * simple situation.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractStateModelSpec extends JUnitSuite {

  /** Creates an instance of the SUT. */
  def createSUT(dependency: UnifiedFakeDependency): StopwatchStateMachine

  /** Creates an instance of the home-grown unified mock dependency. */
  def fixtures(): (StopwatchStateMachine, UnifiedFakeDependency) = {
    val dependency = new UnifiedFakeDependency
    (createSUT(dependency), dependency)
  }

  /**
   * Verifies that we're initially in the stopped state (and told the listener
   * about it).
   */
  @Test def preconditionsAreMet(): Unit = {
    val (model, dependency) = fixtures()
    model.actionInit()
    assertEquals(STOPPED, dependency.state)
  }

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds,
   * expect time 5.
   */
  @Test def scenarioStartWaitStopWorks(): Unit = {
    val (model, dependency) = fixtures()
    model.actionInit()
    assertEquals(0, dependency.time)
    // directly invoke the button press event handler methods
    model.onStartStop()
    (1 to 5) foreach { _ => model.onTick() }
    assertEquals(5, dependency.time)
  }

  /**
   * Verifies the following scenario: time is 0, press start, wait 5+ seconds,
   * expect time 5, press lap, wait 4 seconds, expect time 5, press start,
   * expect time 5, press lap, expect time 9, press lap, expect time 0.
   */
  @Test def scenarioStartWaitLapWaitStopLapResetWorks(): Unit = {
    val (model, dependency) = fixtures()
    model.actionInit()
    assertEquals(0, dependency.time)
    // directly invoke the button press event handler methods on model
    model.onStartStop()
    assertEquals(RUNNING, dependency.state)
    assertTrue(dependency.isStarted)
    (1 to 5) foreach { _ => model.onTick() }
    assertEquals(5, dependency.time)
    model.onLapReset()
    assertEquals(LAP_RUNNING, dependency.state)
    assertTrue(dependency.isStarted)
    (1 to 4) foreach { _ => model.onTick() }
    assertEquals(5, dependency.time)
    model.onStartStop()
    assertEquals(LAP_STOPPED, dependency.state)
    assertFalse(dependency.isStarted)
    assertEquals(5, dependency.time)
    model.onLapReset()
    assertEquals(STOPPED, dependency.state)
    assertFalse(dependency.isStarted)
    assertEquals(9, dependency.time)
    model.onLapReset()
    assertEquals(STOPPED, dependency.state)
    assertFalse(dependency.isStarted)
    assertEquals(0, dependency.time)
  }
}

/**
 * Manually implemented single fake object that unifies the three
 * dependencies of the stopwatch state machine model.
 * The three dependencies correspond to the three interfaces
 * this fake object implements.
 */
class UnifiedFakeDependency extends DefaultTimeModel with ClockModel with StopwatchUIUpdateListener {
  var time = -1
  var state: ModelStateId = _
  override def updateTime(timeValue: Int): Unit = this.time = timeValue
  override def updateState(stateId: ModelStateId): Unit = this.state = stateId
  var isStarted = false
  override def start(): Unit = isStarted = true
  override def stop(): Unit = isStarted = false
}