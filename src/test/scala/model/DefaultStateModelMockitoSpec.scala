package edu.luc.etl.cs313.scala.stopwatch.model

/** A concrete testcase subclass for StatelessBoundedCounter. */
class DefaultStateModelMockitoSpec extends AbstractStateModelMockitoSpec {
  override def createSUT(dependencies: Dependencies) =
    new state.DefaultStopwatchStateMachine(
      dependencies.timeModel, dependencies.clockModel, dependencies.uiUpdateListener)
}
