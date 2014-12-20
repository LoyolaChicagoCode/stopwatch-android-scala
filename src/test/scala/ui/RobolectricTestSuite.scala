package edu.luc.etl.cs313.scala.stopwatch
package ui

import org.junit.runner.RunWith
import org.robolectric.{Robolectric, RobolectricTestRunner}
import org.scalatest.junit.JUnitSuite

/** Concrete Robolectric test subclass. */
@RunWith(classOf[RobolectricTestRunner])
class RobolectricTestSuite extends JUnitSuite
with AbstractFunctionalTest
with AbstractViewUpdaterTest {

  override lazy val activity =
    Robolectric.buildActivity(classOf[MainActivity]).create().start().resume().get

  override protected def runUiThreadTasks() = Robolectric.runUiThreadTasks()
}
