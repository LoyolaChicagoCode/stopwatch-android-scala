package edu.luc.etl.cs313.scala.stopwatch.model

import time.DefaultTimeModel

/** A concrete testcase subclass for the time model. */
class DefaultTimeModelSpec extends AbstractTimeModelSpec {
  override def fixture() = new DefaultTimeModel
}
