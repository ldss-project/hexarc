---
title: Persistence Module
layout: default
nav_order: 2
parent: Developer Documentation
---

# Persistence Module
{: .no_toc}

This chapter will delve into the implementation of the **Persistence Module**
within the HexArc framework.

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Implementation

The **Persistence Module** is a supporting module of the HexArc architecture, providing
tools to decouple the business logic of a service from specific persistent storage
technologies, often required to maintain a state outside the service lifetime. Moreover,
it provides tools to easily manipulate data inside a service.

Below is the complete class diagram of the Persistence Module.

![Persistence Module](/hexarc/resources/images/persistence-module.png)

As shown in the diagram, the Persistence Module is made of two main submodules:
- **Persistence Collection Submodule**: defines the concepts required to decouple a service
  from its persistent storage;
- **BsonDSL Submodule**: provides a DSL for working with [BSON](https://bsonspec.org/)
  documents in a declarative way.

### Persistent Collection Submodule

![Persistent Collection Submodule](/hexarc/resources/images/persistence-module-persistent-collection.png)

### BsonDSL Submodule

![BsonDSL Submodule](/hexarc/resources/images/persistence-module-bson-dsl.png)
