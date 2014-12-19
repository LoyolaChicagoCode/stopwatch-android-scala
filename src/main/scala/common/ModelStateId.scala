package edu.luc.etl.cs313.scala.stopwatch.common

/** A model state that the view may want to represent. */
trait ModelStateId

object ModelStateId {
  case object STOPPED extends ModelStateId
  case object RUNNING extends ModelStateId
  case object LAP_RUNNING extends ModelStateId
  case object LAP_STOPPED extends ModelStateId
}