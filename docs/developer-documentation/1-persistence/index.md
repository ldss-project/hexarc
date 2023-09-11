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
defines its input as a query and its output as a query result. That makes a total of eight types, namely:
`CreateQuery`, `CreateQueryResult`, `ReadQuery`, `ReadQueryResult`, `UpdateQuery`, `UpdateQueryResult`, `DeleteQuery`
and `DeleteQueryResult`.

The Persistent Collection Module doesn't only define the abstract concepts of a persistent
storage, but it also provides some built-in implementations already integrated with some of
the most used persistent storage technologies (e.g. [MongoDB](https://www.mongodb.com/)...).

#### MongoDB Persistent Collection

This section will present the `MongoDBPersistentCollection` as a sample implementation of a
`PersistentCollection`. The implementation relies on
[Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/), which already
provides the concepts required for interacting with a [MongoDB](https://www.mongodb.com/) database.

Before implementing a `PersistentCollection`, its `QueryLanguage` should be defined.
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
operations are implemented by feeding the user's queries of the `MongoDBQueryLanguage` to a `MongoDBClient`,
that is connected to the [MongoDB](https://www.mongodb.com/) collection bound to the `MongoDBPersistentCollection`,
therefore applying the user's queries to that [MongoDB](https://www.mongodb.com/) collection.
 
### BsonDSL Submodule

The **BsonDSL Submodule** is the part of the Persistence Module that defines a DSL
for easily manipulating [BSON](https://bsonspec.org/) documents, reducing boilerplate code.

Below is the class diagram of the BsonDSL Submodule.

![BsonDSL Submodule](/hexarc/resources/images/persistence-module-bson-dsl.png)

The entrypoint of the module is the DSL, modelled by the object `BsonDSL`. The `BsonDSL` depends on
the [Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/) library and provides
a functional DSL for creating and updating `BsonDocument`s (which is the class used by
[Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/) for modelling
[BSON](https://bsonspec.org/) documents).

In HexArc, the creation of a `BsonDocument` relies on a `BsonSpecification`, which is basically a list
of the key-value pairs in a [BSON](https://bsonspec.org/) document.

The `BsonDSL` creates a new `BsonSpecification` each time the user starts creating a new
`BsonDocument`. The `BsonSpecification` is used internally to keep track of the state of that
`BsonDocument` as the user defines its key-value pairs. Finally, when the user is done, the
`BsonSpecification` is parsed producing the corresponding `BsonDocument`.

The `BsonDSL` provides two methods for creating a `BsonDocument`:
- `emptyBson`: creates a new `BsonDocument` with no entries;
- `bson`: creates a new `BsonDocument` configured by a context function provided as input.
Moreover, it provides a method for creating `BsonArray`s, called `array`.

When creating a `BsonDocument` using the `bson` method, some extension methods are made available
for binding the keys of the `BsonDocument` to their respective `BsonValue`s, namely:
- `String.::`: binds a string key to a `BsonValue`, adding the key-value pair to the `BsonSpecification` given
  in the context where `::` is used;
- `String.:#`: binds a string key to a `BsonDocument`, adding the key-value pair to the `BsonSpecification` given
  in the context where `:#` is used;
- `String.:*`: binds a string key to a `BsonArray`, adding the key-value pair to the `BsonSpecification` given
  in the context where `:*` is used.

Since in [Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/) `BsonDocument`s and
`BsonArray`s are `BsonValue`s, these last two extension methods are actually redundant. In fact, they are
used as a shortcut in the DSL, as demonstrated in the
[User Documentation](/hexarc/user-documentation/1-persistence/2-working-with-bson#creating-bson-documents).

The problem with `::` is that it only accepts `BsonValue`s as values that can be bound to a key in a
`BsonDocument`. For example, to bind a key to a `String`, it is required to bind the key to a `BsonString`,
which is a subtype of `BsonValue`, like this:

```scala
bson {
  "stringField" :: BsonString("stringValue")
}
```

Instead, it would be nicer if `::` could be more general, accepting `String`s, `Int`s and any other type, like
this:

```scala
bson {
  "stringField" :: "stringValue"
}
```

To solve this problem, each key-defining method accepts a given `BsonEncoder`, which tells how to convert
an object into its corresponding `BsonValue`.

The `BsonDSL` already exposes a built-in set of standard codecs, providing `BsonEncoder`s and `BsonDecoder`s (the
inverse operation of a `BsonEncoder`) for the most common types used when creating `BsonDocument`s. In addition to
the standard codecs, the user is also able to create codecs for his own custom types, extending the `BsonDSL`
according to his requirements.

Since most types in [Scala](https://scala-lang.org/) would be converted into a `BsonDocument` (instead of a general
`BsonValue`), more specific types of `BsonDecoder` and `BsonEncoder` have been provided, namely `BsonDocumentDecoder`
and `BsonDocumentEncoder`, which handles the conversions of objects from and to `BsonDocument`s.

Among the standard codecs, the `GenericCodec` provides two extension methods that allow any object to be converted
into its corresponding `BsonValue`, given a proper `BsonEncoder`, and a `BsonValue` to be converted into its
corresponding object, given a proper `BsonDecoder`. These are `T.asBson` and `BsonValue.as[T]` respectively.

In addition to the creation of `BsonDocument`s, the `BsonDSL` provides an extension method for updating
`BsonDocument`, called `BsonDocument.update`, which creates a new `BsonDocument` by replacing or adding
key-value pairs to an existing `BsonDocument`.

Finally, one last functionality provided by the `BsonDSL` is the ability to read key-value pairs from existing
`BsonDocument`s. In particular, this functionality is handled within the `BsonExtension` which is exposed by the
`BsonDSL`.

The `BsonExtension` provides two main extension methods that can be used to access the content of a `BsonDocument`:
- `BsonDocument.apply`: retrieves the value bound to a given key or path in a `BsonDocument`, where a path is a
  concatenation of keys separated by dots if the `BsonDocument` contains nested documents. This method is safe
  meaning that it returns an `Option[BsonValue]`, taking into account that the given key or path may not be bound
  to any value.
- `Option[BsonValue].apply`: same as above, treating the `BsonValue` as a `BsonDocument` if possible. This method
  allows the following concatenation syntax for accessing nested key-value pairs:
  ```scala
  val document: BsonDocument = 
    bson {
      "field" :# {
        "nestedField1" :: "value1"
        "nestedField2" :: "value2"
      } 
    }
  
  val field: Option[BsonValue] = document("field")
  val nestedField1: Option[BsonValue] = field("nestedField1")
  val nestedField2: Option[BsonValue] = document("field")("nestedField2")
  ```
  Alternatively, the `BsonExtension` also provides support for the following syntax:
  ```scala
  val nestedField1: Option[BsonValue] = document("field.nestedField1")
  ```
  Although, even if it is more concise, this latter syntax had to be implemented separately, while the former syntax
  came for free from the interactions within the DSL.

For both classes, similar extension methods exist to avoid working with `Option`s when the user is certain of the
content of its `BsonDocument`s. These are called `BsonDocument.require` and `Option[BsonValue].require`.

```scala
val nestedField1: BsonValue = document.require("field.nestedField1")
```

Still, all of these extension methods extract `BsonValue`s from `BsonDocument`s, while it should be made possible
to convert such `BsonValue` into `String`s, `Int`s or any other type.

Here come into play the aforementioned `BsonDecoder`s and in particular the `BsonValue.as[T]` method, which enables
the following syntax:

```scala
val nestedField1: Option[String] = document("field.nestedField1").map(_.as[String])
val nestedField2: String = document.require("field.nestedField2").as[String]
```