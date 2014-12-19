package edu.luc.etl.cs313.scala.stopwatch.model

import clock._

/** A concrete testcase subclass for StatelessBoundedCounter. */
class DefaultClockModelSpec extends AbstractClockModelSpec {
  override def fixture(listener: OnTickListener) = new DefaultClockModel(listener)
}
