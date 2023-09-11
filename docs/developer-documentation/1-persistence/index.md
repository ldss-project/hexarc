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

The **Persistent Collection Submodule** is the part of the Persistence Module that defines
the concepts required to decouple a service from its persistent storage.

Below is the class diagram of the Persistent Collection Submodule.

![Persistent Collection Submodule](/hexarc/resources/images/persistence-module-persistent-collection.png)

The most important concept of the module is the `PersistentCollection`, which is basically a collection
of data which persists over multiple runs of an application.

A `PersistentCollection` provides four methods corresponding to the
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations, namely:
- `create`: accepts a query for inserting some documents in the `PersistentCollection`;
- `read`: accepts a query for retrieving some documents from the `PersistentCollection`;
- `update`: accepts a query for modifying some documents from the `PersistentCollection`;
- `delete`: accepts a query for removing some documents from the `PersistentCollection`.

A `PersistentCollection` relies on a `QueryLanguage`, which defines the types of query accepted by the
operations above, together with the types of result produced by those operations. This relationship is
made explicit by using `QueryLanguage` as a _self-type_ of `PersistentCollection`, meaning that in order
to create a `PersistentCollection`, a `QueryLanguage` must always be applied to the `PersistentCollection`
as a mixin (i.e. a `PersistentCollection` cannot exist without a `QueryLanguage`).

For each of the [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations, a `QueryLanguage`
defines its input, as a query, and its output, as a query result. That makes a total of eight types, namely:
`CreateQuery`, `CreateQueryResult`, `ReadQuery`, `ReadQueryResult`, `UpdateQuery`, `UpdateQueryResult`, `DeleteQuery`
and `DeleteQueryResult`.

The Persistent Collection Module doesn't only define the abstract concepts of a persistent
storage, but it also provides some built-in implementations already integrated with some of
the most used persistent storage technologies (e.g. [MongoDB](https://www.mongodb.com/))

#### MongoDB Persistent Collection

This section will present the `MongoDBPersistentCollection` as a sample implementation of a
`PersistentCollection`. The implementation relies on
[Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/), which already
provides the concepts required for interacting with a [MongoDB](https://www.mongodb.com/) database.

In order to implement a `PersistentCollection`, first it is required to implement its `QueryLanguage`.
For [MongoDB](https://www.mongodb.com/), the `QueryLanguage` is modelled by the `MongoDBQueryLanguage`,
where queries are defined using [BSON](https://bsonspec.org/) documents, which may describe data, filters,
projections, updates or more general aggregations inside a query.

The `MongoDBQueryLanguage` exposes the implementations of the possible queries in [MongoDB](https://www.mongodb.com/)
and the implementations of their results. Some of these implementations were already provided by
[Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/).

Once the `MongoDBQueryLanguage` is implemented, it is possible to define a `PersistentCollection` which
depends on it, such as the `MongoDBPersistentCollection`.

In order to create a `MongoDBPersistentCollection`, the user must specify a connection string towards a specific
_collection_ inside a [MongoDB](https://www.mongodb.com/) database, basically binding the `MongoDBPersistentCollection`
to that [MongoDB](https://www.mongodb.com/) _collection_.

In a `MongoDBPersistentCollection`, the [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete)
operations are implemented by feeding the user's queries of the `MongoDBQueryLanguage` to a `MongoDBClient`
connected to the [MongoDB](https://www.mongodb.com/) collection that is bound to the `MongoDBPersistentCollection`,
therefore applying the user's queries to that [MongoDB](https://www.mongodb.com/) collection.
 
### BsonDSL Submodule

The **BsonDSL Submodule** is the part of the Persistence Module that defines a DSL
for easily manipulating [BSON](https://bsonspec.org/), reducing boilerplate code.

Below is the class diagram of the BsonDSL Submodule.

![BsonDSL Submodule](/hexarc/resources/images/persistence-module-bson-dsl.png)
