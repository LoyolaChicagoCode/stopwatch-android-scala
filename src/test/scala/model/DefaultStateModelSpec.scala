package edu.luc.etl.cs313.scala.stopwatch.model

import state.DefaultStopwatchStateMachine

/** A concrete testcase subclass for StatelessBoundedCounter. */
class DefaultStateModelSpec extends AbstractStateModelSpec {
  override def fixtureSUT(dependency: UnifiedMockDependency) =
    new DefaultStopwatchStateMachine(dependency, dependency, dependency)
}
