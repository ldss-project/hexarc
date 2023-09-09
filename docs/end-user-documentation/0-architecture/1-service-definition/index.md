---
title: Defining a service
layout: default
nav_order: 2
grand_parent: End User Documentation
parent: Architecture Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Defining a service

In HexArc, the _definition_ of a service consists in the configuration of the
service and its components.

The tools for defining a service are provided by the `VertxDSL`. The way this
DSL allows the user to define a service is best seen in the code below.

### Example
{: .no_toc}

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import ColoredLampService.*

/** Service definition. */
def myColoredLampService: DeploymentGroup ?=> Service =
  new Service:                                 // a new service
    name = "ColoredLampService"                // named "ColoredLampService" (for logging purposes)

    val lampModel = ColoredLampModel()
    new Port[LampSwitchPort]:                  // with a new use-case of type [LampSwitchPort]
      name = "SwitchPort"                        // named "SwitchPort"
      model = lampModel                          // implemented by <lampModel>
        
      new Adapter(LampSwitchHttpAdapter()):      // exposed by a new adapter of type <LampSwitchHttpAdapter>
        name = "Http"                              // named "Http"
      new Adapter(LampSwitchMqttAdapter()):      // also, exposed by a new adapter of type <LampSwitchMqttAdapter>
        name = "Mqtt"                              // named "Mqtt"
        
    new Port[LampColorPort]:                   // also, with a new use-case of type [LampColorPort]
      name = "ColorPort"                         // named "SwitchPort"
      model = lampModel                          // implemented by <lampModel>
        
      new Adapter(LampColorHttpAdapter()):       // exposed by a new adapter of type <LampColorHttpAdapter>
        name = "Http"                              // named "Http"
      new Adapter(LampColorMqttAdapter()):       // also, exposed by a new adapter of type <LampSwitchMqttAdapter>
        name = "Mqtt"                              // named "Mqtt"
```
