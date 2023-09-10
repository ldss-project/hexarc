---
title: User Documentation
layout: default
nav_order: 2
has_children: true
has_toc: false
---

# User Documentation
{: .no_toc}

This chapter will explain how to use the HexArc framework, briefly presenting
its main concepts and tutoring the end user in exploiting its core functionalities.

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Quick Start

### Installation

To start using HexArc in your [Scala 3](https://www.scala-lang.org/) project, you'll need to add it as
a dependency. Here's how to do it in a [Gradle](https://gradle.org/) project.

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.jahrim:hexarc:<version>")
}
```

If you are having any trouble configuring your project for HexArc, try consulting the configuration
of the following projects: [chess-game-service](https://github.com/ldss-project/chess-game-service),
[authentication-service](https://github.com/ldss-project/authentication-service),
[statistics-service](https://github.com/ldss-project/statistics-service).

### Tutorial

This section provides a tutorial-by-example showcasing the functionalities provided by the
HexArc framework for the end user.

- **Architecture Module**
  - Vertx
    - [Implementing a service](/hexarc/user-documentation/0-architecture/0-service-implementation)
    - [Defining a service](/hexarc/user-documentation/0-architecture/1-service-definition)
    - [Deploying a service](/hexarc/user-documentation/0-architecture/2-service-deployment)
- **Persistence Module**
  - MongoDB
    - [Creating a Persistent Collection](/hexarc/user-documentation/1-persistence/0-persistent-collection-creation)
    - [Accessing a Persistent Collection](/hexarc/user-documentation/1-persistence/1-persistent-collection-access)
    - [Working with BSON documents](/hexarc/user-documentation/1-persistence/2-working-with-bson)
