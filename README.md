[![Build Status](https://travis-ci.org/LoyolaChicagoCode/stopwatch-android-scala.svg?branch=master)](https://travis-ci.org/LoyolaChicagoCode/stopwatch-android-scala) 
[![Coverage Status](https://img.shields.io/coveralls/LoyolaChicagoCode/stopwatch-android-scala.svg)](https://coveralls.io/r/LoyolaChicagoCode/stopwatch-android-scala) 
[![Download](https://api.bintray.com/packages/loyolachicagocode/generic/stopwatch-android-scala/images/download.svg) ](https://bintray.com/loyolachicagocode/generic/stopwatch-android-scala/_latestVersion)

# Learning Objectives

## Modeling

* Modeling state-dependent behavior with [state machine diagrams](http://en.wikipedia.org/wiki/UML_state_machine)
  (see also [here](/LoyolaChicagoCode/stopwatch-android-scala/src/default/doc))
* Distinguishing between view states and (behavioral) model states 
  (see also [here](http://cder.cs.luc.edu/html/chapter.html#interactive-behaviors-and-implicit-concurrency-with-internal-timers))

## Semantics

* [Event-driven/asynchronous program execution](http://en.wikipedia.org/wiki/Event-driven_programming)
* User-triggered input events
* Internal events from background timers
* [Concurrency issues: single-thread rule of accessing/updating the view in the GUI thread](http://stackoverflow.com/questions/11772658/why-is-a-single-threaded-model-used-to-update-the-ui-as-main-thread)
* [Android activity lifecycle](http://developer.android.com/training/basics/activity-lifecycle/starting.html)
  and [preserving state automatically under rotation](http://developer.android.com/guide/topics/resources/runtime-changes.html)

## Architecture and Design

* Key architectural issues and patterns
    * [Simple dependency injection (DI)](http://www.martinfowler.com/articles/injection.html)
    * [Model-view-adapter (MVA) architectural pattern](http://en.wikipedia.org/wiki/Model–view–adapter)
    * Mapping MVA to Android
    * [Difference between MVA and model-view-controller (MVC)](https://www.palantir.com/2009/04/model-view-adapter)
    * Distinguishing among dumb, reactive, and autonomous model components
* Key design patterns
    * Implementing event-driven behavior: [Observer pattern](http://sourcemaking.com/design_patterns/observer)
    * Implementing state-dependent behavior: [State pattern](http://sourcemaking.com/design_patterns/state)
    * Hiding complexity in the model from the adapter: [Façade pattern](http://sourcemaking.com/design_patterns/facade)
    * Representing tasks as objects: [Command pattern](http://sourcemaking.com/design_patterns/command)
	* Restoring an object from an externalized snapshot: [Memento pattern](http://sourcemaking.com/design_patterns/memento)
* [Relevant class-level design principles](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
    * Dependency Inversion Principle (DIP)
    * Single Responsibility Principle (SRP)
    * Interface Segregation Principle (ISP)
* Package-level architecture and [relevant principles](http://butunclebob.com/ArticleS.UncleBob.PrinciplesOfOod)
    * [Dependency graph](http://en.wikipedia.org/wiki/Dependency_graph)
      (see also [here](/LoyolaChicagoCode/stopwatch-android-scala/src/default/doc))
    * Stable Dependencies Principle (SDP)
    * Acyclic Dependencies Principle (ADP)
* [Architectural journey](/LoyolaChicagoCode/stopwatch-android-scala/commits)

## Testing

* [Different types of testing](http://en.wikipedia.org/wiki/Software_testing)
    * Component-level unit testing
        * state-based testing
        * interaction testing
    * System testing
    * Instrumentation testing
* Key design patterns
    * Factoring out reusable test code: [Testcase Superclass pattern](http://xunitpatterns.com/Testcase%20Superclass.html)
    * Deferring method implementation to subclasses: [Template Method pattern](http://sourcemaking.com/design_patterns/template_method)
    * Allowing subclasses to decide which class to instantiate: [Factory Method pattern](http://sourcemaking.com/design_patterns/factory_method)
* [Test coverage](http://en.wikipedia.org/wiki/Code_coverage) (see also [here](http://martinfowler.com/bliki/TestCoverage.html))
* Test frameworks
    - Unit testing and [Behavior-Driven Development (BDD)](http://en.wikipedia.org/wiki/Behavior-driven_development)
      with [ScalaTest](http://scalatest.org)
    - [Mock objects](http://en.wikipedia.org/wiki/Mock_object) with [Mockito](http://mockito.github.io)
    - Functional testing (out-of-container) using [Robolectric](http://robolectric.org/)

# Observations

In addition to the [observations from the click counter example](https://github.com/LoyolaChicagoCode/clickcounter-android-scala/blob/master/README.md),
the situation here is further complicated by the view updater's stronger
dependency on its stackable mixin trait. We tackle this problem by inserting
a test spy wrapper between the actual SUT and the mixin.


# Building and Running

Please refer to [these notes](http://lucoodevcourse.github.io/notes/scalaandroiddev.html) for details.

