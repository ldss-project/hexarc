---
title: End User Documentation
layout: default
nav_order: 2
has_children: true
---

# End User Documentation
{: .no_toc}

This chapter will briefly present the main concepts of the HexArc framework 
and discuss its core functionalities from the perspective of the end user.

## Contenuti
{: .no_toc}

- TOC
{:toc}

---

## Introduction

HexArc is a [Scala 3](https://www.scala-lang.org/) framework for declaring and deploying
services in a declarative way, while adhering to the best practices of the
[Hexagonal Architecture]((https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)))
(also known as Clean Architecture), defining clear boundaries between the affordances
of a service and expressing their intra-service and inter-service dependencies through
loose-coupling.

The HexArc framework is divided in modules, each providing different tools for
controlling the complexities of a service and tackling the problem of expressing 
particular types of dependency as weak dependencies.

The main modules provided by the HexArc framework are the following:
- **Architecture Module**
    
  The `Architecture Module` is the core module of the HexArc framework.
  It defines the main concepts of the [Hexagonal Architecture](https://en.wikipedia.org/wiki/Hexagonal_architecture_(software)),
  providing DSLs to easily configure and deploy services.

  In HexArc, a service is made of two types of components:
  - `Port`s : the possible perspectives on the business logic of the service, implemented
    by corresponding models.
  - `Adapter`s : the technologies enabled for interacting with the `Port`s of the service.
    
  To simplify, when defining a service, consider to:
  - Enable **use cases** through `Port`s.
  - Enable **technologies** through `Adapter`s.

- **Persistence Module**

  The `Persistence Module` is a supporting module which tackles the problems of decoupling
  the business logic of a service from specific persistent storage technologies, which are
  often required to maintain a state outside the service lifetime. Moreover, the module provides
  DSLs to easily manipulate data inside a service.

  The main component of the `Persistence Module` is the `PersistenceCollection`, which is
  best described as a collection whose content persist over different runs of an application.
  The purpose of a `PersistentCollection` is to abstract away from the specific technologies
  used to persist the state of the service, acting as a _data access layer_.

  A `PersistenceCollection` can be accessed and modified by means of general
  [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations, namely
  `create`, `read`, `update` and `delete`.

On top of the abstract concepts provided to solve dependency problems, each module may also already
provide implementations of such concepts to support specific technologies, without the end user
needing to implement them himself.

## Quick Start

### Installation

To start using HexArc in your [Scala 3](https://www.scala-lang.org/) project, you'll need to add it as
a dependency. Here's how to do it in a [Gradle](https://gradle.org/) project.

```kotlin
dependencies {
    implementation("io.github.jahrim:hexarc:<version>")
}
```

If you are having any trouble configuring your project for HexArc, try consulting the configuration
of the following projects: [chess-game-service](https://github.com/ldss-project/chess-game-service),
[authentication-service](https://github.com/ldss-project/authentication-service),
[statistics-service](https://github.com/ldss-project/statistics-service).

### Core Functionalities

This section provides a tutorial-by-example showcasing the functionalities provided by the
HexArc framework for the end user.

- `Architecture Module`
    - Vertx
        - [Implementing a service](/hexarc/end-user-documentation/0-architecture/0-service-implementation)
        - [Defining a service](/hexarc/end-user-documentation/0-architecture/1-service-definition)
        - [Deploying a service](/hexarc/end-user-documentation/0-architecture/2-service-deployment)
- `Persistence Module`
    - MongoDB
        - [Creating a Persistent Collection](/hexarc/end-user-documentation/1-persistence/0-persistent-collection-creation)
        - [Accessing a Persistent Collection](/hexarc/end-user-documentation/1-persistence/1-persistent-collection-access)
        - [Working with BSON documents](/hexarc/end-user-documentation/1-persistence/2-working-with-bson)
