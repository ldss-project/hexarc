---
title: Accessing a Persistent Collection
layout: default
nav_order: 2
parent: Persistence Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Accessing a Persistent Collection

In HexArc, a `PersistentCollection` provides four methods corresponding to the
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations
for accessing and updating its data, namely `create`, `read`, `update` and `read`. 

The way a service can interact with a specific `PersistentCollection` depends on
the `QueryLanguage` of the `PersistentCollection`. Still, every `QueryLanguage`
defines four types of queries corresponding to the
[CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) operations:
- `CreateQuery`: a query for creating entities;
- `ReadQuery`: a query for reading entities;
- `UpdateQuery`: a query for updating entities;
- `DeleteQuery`: a query for deleting entities.

Together with the types of queries, a `QueryLanguage` defines the type of their
results, namely `CreateQueryResult`, `ReadQueryResult`, `UpdateQueryResult`,
`DeleteQueryResult`.

All these types can be accessed from the reference to the `PersistentCollection`
created by the user.

### Examples
{: .no_toc}

This section will present a set of examples that show how to access a `PersistentCollection`,
referring to the `MongoDBPersistentCollection` implementation.

> **Note**: the query types defined by the `MongoDBQueryLanguage` used by the
> `MongoDBPersistentCollection` are integrated with [Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/),
> so it is possible to define queries using its DSL.

#### Create one

```scala
import io.github.jahrim.hexarc.persistence.mongodb.MongoDBPersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.CreateQuery

// Mongo Java Driver
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

// Create a Persistent Collection
val userCollection: MongoDBPersistentCollection = ???

// Use the Persistent Collection
type QueryResult = userCollection.CreateQueryResult
val queryResult: Try[QueryResult] = 
  userCollection.create(
    CreateQuery(
      document = 
        BsonDocument()
          .append("username", BsonString("Marco"))
          .append("email", BsonString("marco@hexarc.com"))
    )
  )
```

#### Create many

```scala
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.CreateQuery
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

type QueryResult = userCollection.CreateQueryResult
val queryResult: Try[QueryResult] = 
  userCollection.create(
    CreateQuery.many(
      documents = Seq(
        BsonDocument()
          .append("username", BsonString("Marco"))
          .append("email", BsonString("marco@hexarc.com")),
        BsonDocument()
          .append("username", BsonString("Giovanni"))
          .append("email", BsonString("giovanni@hexarc.com"))
      )
    )
  )
```

#### Read many

```scala
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.ReadQuery
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

type QueryResult = userCollection.ReadQueryResult
val queryResult: Try[QueryResult] = 
  userCollection.read(
    ReadQuery(
      filter = Filters.eq("username", "Marco"),
      projection = Projections.include("email")
    )
  )
  
println(queryResult)   // Success(Seq({ "email": "marco@hexarc.com" }))
```

#### Read with Aggregation Pipeline

```scala
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.ReadQuery
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

type QueryResult = userCollection.ReadQueryResult
val queryResult: Try[QueryResult] =
  userCollection.read(
    ReadQuery.pipeline(
      Aggregates.`match`(Filters.eq("username", "Marco")),
      Aggregates.project(Projections.include("username")),
      Aggregates.limit(1)
    )
  )
```

#### Update many

```scala
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.UpdateQuery
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

type QueryResult = userCollection.UpdateQueryResult
val queryResult: Try[QueryResult] = 
  userCollection.update(
    UpdateQuery(
      filter = Filters.eq("username", "Marco"),
      update = Updates.set("email", BsonString("marco2@hexarc.com"))
    )
  )
```

#### Delete many

```scala
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.DeleteQuery
import com.mongodb.client.model.{Filters, Projections, Updates, Aggregates}
import org.bson.{BsonDocument, BsonString}

type QueryResult = userCollection.DeleteQueryResult
val queryResult: Try[QueryResult] =
  userCollection.delete(
    DeleteQuery(
      filter = Filters.eq("username", "Marco")
    )
  )
```