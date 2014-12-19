package edu.luc.etl.cs313.scala.stopwatch
package model

import common._
import time.TimeModel
import clock.{ClockModel, OnTickListener}

/** Contains the components of the dynamic state machine model. */
object state {

  trait Initializable { def actionInit(): Unit }

  /**
   * The state machine for the state-based dynamic model of the stopwatch.
   * This interface is part of the State pattern.
   */
  trait StopwatchStateMachine extends StopwatchUIListener with OnTickListener with Initializable {
    def state(): StopwatchState
    def actionUpdateView(): Unit
  }

  /** A state in a state machine. This interface is part of the State pattern. */
  trait StopwatchState extends StopwatchUIListener with OnTickListener {
    def updateView(): Unit
    def id(): ModelStateId
  }

  /** An implementation of the state machine for the stopwatch. */
  class DefaultStopwatchStateMachine(
    timeModel: TimeModel,
    clockModel: ClockModel,
    uiUpdateListener: StopwatchUIUpdateListener
  ) extends StopwatchStateMachine with Serializable {

    /** The current internal state of this adapter component. Part of the State pattern. */
    private var st: StopwatchState = _

    protected def goToState(state: StopwatchState): Unit = {
      this.st = state
      uiUpdateListener.updateState(state.id)
    }

    def state(): StopwatchState = st

    // forward event uiUpdateListener methods to the current state
    override def onStartStop(): Unit = st.onStartStop()
    override def onLapReset(): Unit  = st.onLapReset()
    override def onTick(): Unit      = st.onTick()

    def updateUIRuntime(): Unit = uiUpdateListener.updateTime(timeModel.getRuntime)
    def updateUILaptime(): Unit = uiUpdateListener.updateTime(timeModel.getLaptime)

    // actions
    override def actionInit(): Unit       = { goToState(STOPPED) ; actionReset() }
    override def actionUpdateView(): Unit = st.updateView()
    def actionReset(): Unit          = { timeModel.resetRuntime() ; actionUpdateView() }
    def actionStart(): Unit          = { clockModel.start() }
    def actionStop(): Unit           = { clockModel.stop() }
    def actionLap(): Unit            = { timeModel.setLaptime() }
    def actionInc(): Unit            = { timeModel.incRuntime() ; actionUpdateView() }

    // known states

    private object STOPPED extends StopwatchState {
      override def onStartStop() = { actionStart() ; goToState(RUNNING) }
      override def onLapReset()  = { actionReset() ; goToState(STOPPED) }
      override def onTick()      = throw new UnsupportedOperationException("onTick")
      override def updateView()  = updateUIRuntime()
      override def id()       = ModelStateId.STOPPED
    }

    private object RUNNING extends StopwatchState {
      override def onStartStop() = { actionStop() ; goToState(STOPPED) }
      override def onLapReset()  = { actionLap() ; goToState(LAP_RUNNING) }
      override def onTick()      = { actionInc() ; goToState(RUNNING) }
      override def updateView()  = updateUIRuntime()
      override def id()       = ModelStateId.RUNNING
    }

    private object LAP_RUNNING extends StopwatchState {
      override def onStartStop() = { actionStop() ; goToState(LAP_STOPPED) }
      override def onLapReset()  = { goToState(RUNNING) ; actionUpdateView() }
      override def onTick()      = { actionInc() ; goToState(LAP_RUNNING) }
      override def updateView()  = updateUILaptime()
      override def id()       = ModelStateId.LAP_RUNNING
    }

    private object LAP_STOPPED extends StopwatchState {
      override def onStartStop() = { actionStart() ; goToState(LAP_RUNNING) }
      override def onLapReset()  = { goToState(STOPPED) ; actionUpdateView() }
      override def onTick()      = throw new UnsupportedOperationException("onTick")
      override def updateView()  = updateUILaptime()
      override def id()       = ModelStateId.LAP_STOPPED
    }
  }
}