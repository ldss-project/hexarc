---
title: Architecture Module
layout: default
nav_order: 1
parent: Developer Documentation
---

# Architecture Module
{: .no_toc}

This chapter will delve into the implementation of the **Architecture Module**
within the HexArc framework.

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Implementation 

The **Architecture Module** is the core module of the HexArc architecture, defining and
implementing all the concepts required to describe, configure and deploy a service,
following the best practices of the Hexagonal Architecture.

Below is the complete class diagram of the Architecture Module.

![Architecture Module](/hexarc/resources/images/architecture-module.png)

As shown in the diagram, the Architecture Module is made of two main submodules:
- **Components Submodule**: defines the concepts required to describe a service;
- **VertxDSL Submodule**: provides a DSL for configuring and deploying services
  in a declarative way.

As of now, HexArc supports only [Vertx](https://vertx.io/) as the underlying
framework for deploying services, but this isn't a binding choice. In fact,
some effort could be put in order to support other technologies 
(e.g. [Akka](https://akka.io/)...) in new modules for HexArc, abstracting the
concepts shared among different modules.

### Components Submodule

The **Components Submodule** is the part of the Architecture Module that defines
the concepts required to describe a service, following the best practices of the
Hexagonal Architecture.

Below is the class diagram of the Components Submodule.

![Components Submodule](/hexarc/resources/images/architecture-module-components.png)

The Components Submodule divides a service into components, called `ServiceComponent`s,
each taking care of providing or supporting the provision of part of the affordances of
the service.

In order to define a `ServiceComponent`, you need to specify how it will be configured when
initialized. In fact, the simplest `ServiceComponent` is just a function specifying its
configuration.

The main `ServiceComponent`s in a service are the following:
- `Port`s: defines what are the affordances of the service with respect to a specific use
  case. `Port`s are parts of the contract of a service, as such they should be represented as
  _traits_. To address the concrete implementation of one or more `Port`s, HexArc adopts the term
  **model**.
- `Adapter`s: typically defines how the affordances of the service are exposed to its users, enabling
  technologies for communicating with one or more `Port`s of the service. This is the case of _inbound_
  `Adapter`s, which are the most common types. However, an `Adapter` could be used also to monitor the
  service and to communicate its events to third-party services, which is the case of _outbound_
  `Adapter`s.
- `VertxService`: it's the service itself, or at least the part of the service holding together its
  `ServiceComponent`s. In fact, this `ServiceComponent` is used internally for integrating all the
  `ServiceComponent`s of the service into a single `Verticle` that can be deployed within
  [Vertx](https://vertx.io/). Likely, it won't be used by the end user, unless he requires for some
  reason to personalize how the integration between the `ServiceComponent`s happens.

Upon the deployment of a service, proper execution contexts, called `ServiceComponentContext`s,
are generated for all of its `ServiceComponent`s. Then, each `ServiceComponent` is initialized
consuming its corresponding `ServiceComponentContext`.

A `ServiceComponentContext` may contain all sorts of useful information for initializing a
`ServiceComponent`. This information depends on the type of the `ServiceComponent` to initialize,
but all `ServiceComponentContext`s provide:
- `name`: the name of the service containing the `ServiceComponent`;
- `vertx`: the `Vertx` instance on which the service is deployed;
- `log`: an [Slf4j](https://www.slf4j.org/) `Logger` specific to the `ServiceComponent`.

More in detail, there are three types of `ServiceComponentContext`:
- `ServiceContext`: the `ServiceComponentContext` provided when a `VertxService` is initialized,
  that is when its corresponding `Verticle` is deployed.
- `PortContext`: the `ServiceComponentContext` provided when a `Port` is initialized. It also
  contains the `ServiceContext` of the service who owns that `Port`.
- `AdapterContext`: the `ServiceComponentContext` provided when an `Adapter` is initialized. It
  also contains the `Port` exposed by that `Adapter` and its `PortContext`.

The Components Submodule provides the end user with all the means for instantiating a service,
letting him personalize the integration of its `ServiceComponent`s and the deployment of the
service. However, since these processes can be complex, HexArc provides a DSL to make things
easier, less imperative and more explicit.

### VertxDSL Submodule

The **VertxDSL Submodule** is the part of the Architecture Module that defines the DSL for
configuring and deploying services in a declarative way, without the hassle of manually
integrating their `ServiceComponent`s.

Below is the class diagram of the VertxDSL Submodule.

![VertxDSL Submodule](/hexarc/resources/images/architecture-module-vertx-dsl.png)

The entrypoint of the module is the DSL, modelled by the object `VertxDSL`. The `VertxDSL`
consists in a set of exports defining the vocabulary of the DSL and enriching its syntax
by means of different extensions.

In particular, the vocabulary of the DSL is defined by the `VertxDSL.Vocabulary`, which
exports the different contexts of the DSL, called `DSLContext`s, as keywords of the DSL.

A `DSLContext` defines which keywords of a DSL are or aren't allowed inside the portion
of code within the `DSLContext`. In fact, a `DSLContext` may be a `DSLContext.Root`,
meaning that its corresponding keyword can be used anywhere, or a `DSLContext.Child`,
meaning that its corresponding keyword can only be used within its parent `DSLContext`.

For example, referring to the 
[User Documentation](/hexarc/user-documentation/0-architecture/1-service-definition),
an `Adapter` may be defined only within a `Port` and it cannot be defined as a direct
child of a `Service`. In order to explicit when a context is closed, the example also
reports the `end new` scala syntax, which is completely optional.

```scala
new DeploymentGroup(Vertx.vertx()):         // opening "DeploymentGroup" DSLContext (Root)
  new Service:                                // opening "Service" DSLContext (Child)
    name = "ColoredLampService"

    new Port[LampSwitchPort]:                   // opening "Port" DSLContext (Child)
      name = "SwitchPort"
      model = ColoredLampModel()
                                                  // Here the keyword `Adapter` exists
      new Adapter(LampSwitchHttpAdapter()):       // opening "Adapter" DSLContext (Child)
        name = "Http"
      end new                                     // closing "Adapter" DSLContext
    end new                                     // closing "Port" DSLContext
                                                // Here the keyword `Adapter` does not exist
    new Adapter(LampSwitchHttpAdapter()):       // ERROR: keyword `Adapter` is not inside a `Port`
      name = "Http"
    end new
  end new                                     // closing "Service" DSLContext 
end new                                     // closing "DeploymentGroup" DSLContext 
```
> **Note**: here `DeploymentGroup`, `Service`, `Port` and `Adapter` are the keywords of the DSL.
> In particular, `Port` and `Adapter` are not the same as their homonyms in the Components Module.

As introduced by the example above, HexArc defines four main types of `DSLContext`:
- `DeploymentGroupDSLContext`: a `DSLContext.Root` describing the configuration for the deployment of
  a group of services. Such configuration consists of a list of the services that should be deployed.
  These services can then be deployed by calling `deploy` on the `DeploymentGroupDSLContext`.

  The `VertxDSL.Vocabulary` exposes this type of `DSLContext` as the keyword `DeploymentGroup`. While a
  `DeploymentGroupDSLContext` exposes the following `ServiceDSLContext` as the keyword `Service`. The
  actual implementation relies on the definition of a type member called `Service`.
- `ServiceDSLContext`: a `DSLContext.Child` of `DeploymentGroupDSLContext` describing the configuration
  for the instance of a service to be deployed. Such configuration consists of a list of the `Port`s forming
  the contract of the service. 
  
  When a `ServiceDSLContext` is opened (i.e. created) within a `DeploymentGroupDSLContext`, it
  automatically adds itself to the services that should be deployed by the `DeploymentGroupDSLContext`.

  The `VertxDSL.Vocabulary` exposes this type of `DSLContext` as the keyword `Service`, so that it could be
  lazily configured outside a `DeploymentGroupDSLContext`. While a `ServiceDSLContext` exposes the following
  `PortDSLContext` as the keyword `Port`.
- `PortDSLContext`: a `DSLContext.Child` of `ServiceDSLContext` describing the configuration of a `Port` of
  a service. Such configuration includes the contract exposed by the `Port` (defined as type parameter),
  the actual implementation of the `Port` and a lists of the `Adapter`s installed on the `Port`.

  When a `PortDSLContext` is opened within a `ServiceDSLContext`, it automatically adds itself to the `Port`s
  of the service configured by the `ServiceDSLContext`.

  The `VertxDSL.Vocabulary` exposes this type of `DSLContext` as the keyword `Port` (not to be confused with
  the homonym component), so that it could be lazily configured outside a `ServiceDSLContext`. While a
  `PortDSLContext` exposes the following `AdapterDSLContext` as the keyword `Adapter` (still, not to be confused
  with the homonym component).

- `AdapterDSLContext`: a `DSLContext.Child` of `PortDSLContext` describing the configuration of an
  `Adapter` of a `Port` of a service. Such configuration consists of the implementation of the
  `Adapter`.

  When an `AdapterDSLContext` is opened within a `PortDSLContext`, it automatically adds itself to the
  `Adapter`s of the `Port` configured by the `PortDSLContext`.

  The `VertxDSL.Vocabulary` exposes this type of `DSLContext` as the keyword `Adapter` (not to be confused with
  the homonym component), so that it could be lazily configured outside a `PortDSLContext`, provided the type
  of `Port` on which it can be installed.

> #### Functional DSL Vs YAML-like DSL
> 
> One of the reasons why HexArc migrated from its original _functional_ syntax to a _YAML-like_ syntax
> (based on the definition of anonymous classes) is **type inference**. For example, inside a `Port`
> keyword, the `Adapter` keywords automatically refer to the proper type of `Adapter` for that `Port`,
> without requiring the user to explicit that type for each `Adapter`.
> ```scala
> // Original functional syntax
> deploy(Vertx.vertx()){
>   service("CustomLamp"){
>     port[DimmableLampPort](DimmableLampModel()){ 
>       // Here `[DimmableLampPort]` couldn't be omitted
>       adapter[DimmableLampPort](DimmableLampLocalAdapter())   
>       adapter[DimmableLampPort](DimmableLampHttpAdapter())
>       adapter[DimmableLampPort](DimmableLampMqttAdapter())
>     }
>     port[ColoredLampPort](ColoredLampModel()){
>       // Here `[ColoredLampPort]` couldn't be omitted
>       adapter[ColoredLampPort](ColoredLampLocalAdapter())   
>       adapter[ColoredLampPort](ColoredLampHttpAdapter())
>       adapter[ColoredLampPort](ColoredLampMqttAdapter())
>     }
>   }
> }
> ```
> ```scala
> // Current YAML-like syntax
> new DeploymentGroup(Vertx.vertx()):
>   new Service:
>     name = "CustomLamp"
>     new Port[DimmableLampPort]:
>       model = DimmableLampModel()
>       // Here `Adapter` can only mean `Adapter[DimmableLampPort]`
>       new Adapter(DimmableLampLocalAdapter())         
>       new Adapter(DimmableLampHttpAdapter())
>       new Adapter(DimmableLampMqttAdapter())
>     new Port[ColoredLampPort]:
>       model = ColoredLampModel()
>       // Here `Adapter` can only mean `Adapter[ColoredLampPort]`
>       new Adapter(ColoredLampLocalAdapter())         
>       new Adapter(ColoredLampHttpAdapter())
>       new Adapter(ColoredLampMqttAdapter())
> }
> ```
> Other reasons of the migration include the following:
> - **A YAML-like syntax feels more proper for representing a data structure**, such as the configuration
>   of the deployment of a group of services. In that sense, it is also more direct to extend the DSL
>   without reducing its readability (e.g. just add a method for configuring a field in a `DSLContext`...).
> - **A YAML-like syntax provides support for scoped keywords**. All the keywords of a functional DSL
>   are available everywhere in the code, even though some require positioning within a specific scope.
>   By defining keywords as type members instead of functions, it is possible to make keywords available
>   only in the scopes where they can actually be used.
> 
> Of course the YAML-like syntax comes with its downsides, the most noticeable being that the
> functional syntax still appears cleaner as it does not require the boilerplate code that a
> YAML-like syntax does require (e.g. the `new` keyword is unfortunately mandatory for creating
> anonymous classes in Scala 3...).

By opening the `DSLContext`s of the `VertxDSL`, a tree-like structure is generated starting from the root,
which is always a `DeploymentGroupDSLContext`.

Internally, each of the four types of `DSLContext` provide a `close` method, which is used to finalize their
configuration. In particular, when `deploy` is called on a `DeploymentGroupDSLContext`, all of the `DSLContext`s
belonging to its tree are closed bottom-up, configuring the corresponding `ServiceComponent`s, finally the
`DeploymentGroupDSLContext` closes itself, deploying the configured services.

The deployment of the services is delegated to a support class called `Deployment`. Its companion object provides
methods for deploying services and obtaining their corresponding `Deployment`s, While, an instance of the `Deployment`
class itself allows to undeploy the corresponding service.

In addition to the `DSLContext`s, another way the `VertxDSL` enriches its syntax is by means of _extensions_.
In particular, it exports all the extension methods provided by the `VertxDSLExtensions` object, which include methods
for manipulating `Future`s (e.g. awaiting the deployment or un-deployment of a service...).

To summarize, the `VertxDSL` is defined through _keywords_, where a keyword can be either:
- a `DSLContext`, exposing other _scoped keywords_ in the form of:
  - public or protected methods;
  - public or protected type member.
- an _extension method_ provided by some extension of the DSL.