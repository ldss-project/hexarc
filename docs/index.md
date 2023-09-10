---
title: Homepage
layout: default
nav_order: 1
---

# HexArc
{: .no_toc}

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Introduction

**HexArc** is a [Scala 3](https://www.scala-lang.org/) framework for defining and deploying
services in a declarative way, while adhering to the best practices of the **Hexagonal Architecture**
(also known as **Clean Architecture**), defining clear boundaries between the affordances
of a service and expressing their intra-service and inter-service dependencies through
loose-coupling.

The HexArc framework is divided into **modules**, each providing different tools for
controlling the complexities of a service and tackling the problem of expressing
particular types of dependency as weak dependencies.

The main modules provided by the HexArc framework are the following:
- **Architecture Module**

  The Architecture Module is the core module of the HexArc framework.
  It defines the main concepts of the Hexagonal Architecture, making the design of services
  cleaner and providing DSLs to easily configure and deploy services.

  In HexArc, a service is made of two types of components:
    - **Ports** : the possible perspectives on the business logic of the service, implemented
      by corresponding **models**.
    - **Adapters** : the technologies enabled for interacting with the ports of the service.

  To simplify, when defining a service, consider to:
    - Enable **use cases** through ports.
    - Enable **technologies** through adapters.

- **Persistence Module**

  The Persistence Module is a supporting module which tackles the problems of decoupling
  the business logic of a service from specific persistent storage technologies, which are
  often required to maintain a state outside the service lifetime. Moreover, the module provides
  DSLs to easily manipulate data inside a service.

  The main component of the Persistence Module is the **persistent collection**, which is
  best described as a collection whose content persist over different runs of an application.
  The purpose of a persistent collection is to abstract away from the specific technologies
  used to persist the state of the service, acting as a **data access layer**.

  A persistent collection can be accessed and modified by means of general
  [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations, namely
  **create**, **read**, **update** and **delete**.

On top of the abstract concepts provided to solve the dependency problems, each module may also
already provide implementations of such concepts to support specific technologies, without the
end user needing to implement them himself.

## User Documentation

If it is your first time here, or you are interested in adopting this framework,
please refer to the [User Documentation](/hexarc/user-documentation) to gain 
familiarity with HexArc.

## Developer Documentation

If you are interested in contributing to this framework, please refer to the
[Developer Documentation](/hexarc/developer-documentation).
