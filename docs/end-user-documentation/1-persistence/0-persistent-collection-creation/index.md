---
title: Creating a Persistent Collection
layout: default
nav_order: 1
grand_parent: End User Documentation
parent: Persistence Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Creating a Persistent Collection

In HexArc, a `PersistentCollection` is a collection (much in the same sense as 
[java.util.Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html))
whose content persist over multiple run of an application.

The process of creating a `PersistentCollection` may vary depending on which tools the specific
implementation of `PersistentCollection` relies on. Still, every single `PersistentCollection`
is bound to a specific `QueryLanguage` (e.g. [MongoDB](https://www.mongodb.com/),
[SQL](https://blog.ansi.org/sql-standard-iso-iec-9075-2023-ansi-x3-135/#gref)...), which is either
inferred from the implementation or specified from the user.

For example, the `MongoDBPersistentCollection` is a `PersistentCollection` based on the
`MongoDBQueryLanguage`, where queries are defined in terms of [BSON](https://bsonspec.org/)
documents.

### Example
{: .no_toc}

```scala
import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.MongoDBPersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.language.MongoDBQueryLanguage

val persistentCollection: PersistentCollection with MongoDBQueryLanguage =
  MongoDBPersistentCollection(
    connection = "mongodb://mongodb0.example.com:27017",
    database = "example-authentication",
    collection = "example-users"
  ).get
```

## Abstracting from database implementations

Separating the concept of a `PersistentCollection` from its `QueryLanguage` lets a service
declare what language it will be using for communicating with the `PersistentCollection`,
without ever specifying the specific technology it requires for storing its data.

### Example
{: .no_toc}

A service may declare to require a `PersistentCollection` based on the `MongoDBQueryLanguage`,
which the service will use to formulate its queries.

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.MongoDBPersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.language.MongoDBQueryLanguage

/** This service must use MongoDB as a PersistentCollection. */
def strictAuthenticationService(
  users: MongoDBPersistentCollection
): DeploymentGroup ?=> Service = ???

/**
 * This service could use any PersistentCollection capable of understanding MongoDB queries.
 * In other words, it is technology-agnostic.
 */
def flexibleAuthenticationService(
  users: PersistentCollection with MongoDBQueryLanguage
): DeploymentGroup ?=> Service = ???
```

Here the second service is not declaring a dependency on a [MongoDB](https://www.mongodb.com/) database.
Instead, it's just declaring that it will use the `MongoDBQueryLanguage` to formulate its queries.

In this way, any `PersistentCollection` based on any database (e.g. [MongoDB](https://www.mongodb.com/),
[SQL](https://blog.ansi.org/sql-standard-iso-iec-9075-2023-ansi-x3-135/#gref), [SQLServer](https://www.microsoft.com/it-it/sql-server/sql-server-2022)...)
can be served to the service, as long as it is capable of interpreting a query written in the `MongoDBQueryLanguage`
that will be used by the service (e.g. translating [BSON](https://bsonspec.org/) queries into
[SQL](https://blog.ansi.org/sql-standard-iso-iec-9075-2023-ansi-x3-135/#gref) queries...).