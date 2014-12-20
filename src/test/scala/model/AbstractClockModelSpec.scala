package edu.luc.etl.cs313.scala.stopwatch.model

import java.util.concurrent.atomic.AtomicInteger

import org.junit.Assert._
import org.junit.Test
import org.scalatest.junit.JUnitSuite
import clock.{ClockModel, OnTickListener}

/**
 * An abstract unit test for the clock model abstraction.
 * This is an interaction test of an object with a listener
 * dependency; we create anonymous listeners to satisfy the
 * dependency and then verify the interactions.
 * This also follows the XUnit Testcase Superclass pattern.
 */
trait AbstractClockModelSpec extends JUnitSuite {

  def fixture(listener: OnTickListener): ClockModel

  /** Verifies that a stopped clock does not emit any tick events. */
  @Test def stoppedClockDoesntTick(): Unit = {
    // use a thread-safe variable because the timer inside the
    // clock has its own thread
    val i = new AtomicInteger(0)
    val model = fixture(new OnTickListener {
      override def onTick() = i.incrementAndGet()
    })
    Thread.sleep(5500)
    assertEquals(0, i.get)
  }

  /** Verifies that a running clock emits about one tick event per second. */
  @Test def runningClockTicksOncePerSec(): Unit = {
    val i = new AtomicInteger(0)
    val model = fixture(new OnTickListener {
      override def onTick() = i.incrementAndGet()
    })
    model.start()
    Thread.sleep(5500)
    model.stop()
    assertEquals(5, i.get)
  }
}
