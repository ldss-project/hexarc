---
title: Implementing a service
layout: default
nav_order: 1
parent: Architecture Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Implementation Vs Definition

In HexArc, a service can be _implemented_ and/or _defined_.

By _implementation_ of a service, we mean the implementation of the components
of the service.

While, by _definition_ of a service, we mean the configuration of a particular
instance of a service, including its declared components and their configuration.

> **Note**: HexArc encourages the separation between the _implementation_ and
> the _definition_ of a service, so that the components of the service could be
> reused in other contexts (e.g. the services modelling a lamp, a dimmable lamp
> and a colored lamp may share the same components that enables the devices to be
> switched on or off...).

## Implementing a service

In HexArc, a service is made of a set of loosely-coupled components, namely 
`Port`s, `Adapter`s and _models_ (i.e. the implementations of the `Port`s).

The components of a service must implement a `init` method, which initializes
the component. The `init` method will be called when the service is deployed,
providing the proper execution context to every component.

Most notably, the execution context of a service component may contain:
- `vertx`: the [Vertx](https://vertx.io/docs/apidocs/io/vertx/core/Vertx.html) instance where the
  service is running;
- `log`: a [Logger](https://www.slf4j.org/api/org/slf4j/Logger.html) specific to that component;
- `api`: the `Port` exposed by that component, if the component is an `Adapter`.

Such context is also available outside the `init` method, but it cannot be safely
accessed before the component initialization.

### Example
{: .no_toc}

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.vertx.core.Future

/** Service implementation. */
object ColoredLampService:

  /** Ports. */
  trait LampSwitchPort extends Port:
    def switch(on: Boolean): Future[Unit]
    def isOn: Future[Boolean]

  trait LampColorPort extends Port:
    def changeColor(color: String): Future[Unit]
    def color: Future[String]

  /** Business logic. */
  class ColoredLampModel extends LampSwitchPort with LampColorPort:
    override def init(context: PortContext): Unit = ???
    override def switch(on: Boolean): Future[Unit] = ???
    override def isOn: Future[Boolean] = ???
    override def changeColor(color: String): Future[Unit] = ???
    override def color: Future[String] = ???

  /** Adapters. */
  class LampSwitchHttpAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit = ???

  class LampSwitchMqttAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit = ???

  class LampColorHttpAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit = ???

  class LampColorMqttAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit = ???
```

Full example
[here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/examples/ColoredLampService.scala).
