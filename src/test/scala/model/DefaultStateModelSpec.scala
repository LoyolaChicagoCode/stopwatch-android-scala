package edu.luc.etl.cs313.scala.stopwatch.model

import state.DefaultStopwatchStateMachine

/** A concrete testcase subclass for StatelessBoundedCounter. */
class DefaultStateModelSpec extends AbstractStateModelSpec {
  override def createSUT(dependency: UnifiedFakeDependency) =
    new DefaultStopwatchStateMachine(dependency, dependency, dependency)
}

