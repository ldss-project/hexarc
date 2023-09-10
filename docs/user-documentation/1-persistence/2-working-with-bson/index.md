---
title: Working with BSON Documents
layout: default
nav_order: 3
grand_parent: User Documentation
parent: Persistence Module
---

## Table of Contents
{: .no_toc}

- TOC
{:toc}

---

## Working with BSON Documents

Along with the other tools, HexArc also provides a DSL to work with [BSON](https://bsonspec.org/)
documents in a more declarative way and with less boilerplate code with respect to
[Mongo Java Driver](https://www.mongodb.com/docs/drivers/java/sync/current/).
This DSL is called `BsonDSL`.

At the moment, the DSL supports only a few of the existing [BSON](https://bsonspec.org/) types, which
should be enough for most common applications. If it wasn't enough, the DSL can be easily extended by
defining custom `BsonEncoder`s and `BsonDecoder`s, either for the required missing
[BSON](https://bsonspec.org/) types or your own custom types (e.g. POJOs...).

### Example
{: .no_toc}

This section will present a set of examples that show how to manipulate [BSON](https://bsonspec.org/)
documents using the `BsonDSL`.

#### Creating BSON Documents

```scala
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.*

// Creating documents without `BsonDSL`
val documentWithoutDSL: BsonDocument =
  BsonDocument()
    .append("booleanField", BsonBoolean(true))
    .append("stringField", BsonString("value"))
    .append("intField", BsonInt32(10))
    .append("longField", BsonInt64(10_000_000_000_000L))
    .append("doubleField", BsonDouble(0.33d))
    .append("dateField", BsonDateTime(java.time.Instant.now.toEpochMilli))
    .append("arrayField1", BsonArray(
      java.util.List.of(BsonInt32(1), BsonInt32(2), BsonInt32(3))
    ))
    .append("arrayField2", BsonArray(
      java.util.List.of(BsonInt32(1), BsonInt32(2), BsonInt32(3))
    ))
    .append("objectField1", BsonDocument()
      .append("subfield", BsonInt32(0))
    )
    .append("objectField2", BsonDocument()
      .append("subfield", BsonInt32(0))
    )

// Creating documents with `BsonDSL`
val documentWithDSL: BsonDocument = 
  bson {
    "booleanField" :: true                 // declare a boolean
    "stringField" :: "value"               // declare a string
    "intField" :: 10                       // declare an integer
    "longField" :: 10_000_000_000_000L     // declare a long
    "doubleField" :: 0.33D                 // declare a double
    "dateField" :: java.time.Instant.now   // declare a date
    "arrayField1" :: array(1,2,3)          // declare a homogeneous array
    "arrayField2" :* (1,2,3)               // shortcut syntax for declaring a homogeneous array
    "objectField1" :: bson {               // declare an object
      "subfield" :: 0
    }
    "objectField2" :# {                    // shortcut syntax for declaring an object
      "subfield" :: 0
    }       
  }
```

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/persistence/bson/dsl/BsonDSLTest.scala).

#### Accessing BSON Documents

```scala
val document: BsonDocument = documentWithDSL

// Accessing an element via `apply` retrieves an `Option[BsonValue]`.
val bsonBooleanOption: Option[BsonValue] = document("booleanField")

// `BsonValue`s can be decoded into objects via the method `as[T]`.
// Objects can be encoded back to `BsonValue`s via the method `asBson`.
val booleanOption: Option[Boolean] = bsonBooleanOption.map(_.as[Boolean])

// Accessing an element via `require` retrieves a `BsonValue` or throws
// a `NoSuchElementException`.
val boolean: Boolean = document.require("booleanField").as[Boolean]

// `Option[BsonValue]`s can also be accessed via `apply` for accessing nested
// elements.
val subfieldOption: Option[BsonValue] = document("objectField1")("subField")

// Field paths are also accepted to access nested elements.
val subfield: Int = document.require("objectField1.subField").as[Int]
```

#### Updating BSON Documents

```scala
val document: BsonDocument =
  bson {
    "field1" :: "value"
    "field2" :: "value"
  }
println(document)
// Output: { "field1": "value", "field2": "value" }

val updatedDocument: BsonDocument = 
  document.update {
    "field2" :: "otherValue"
    "field3" :: "otherValue"
  }
println(updatedDocument)
// Output: { "field1": "value", "field2": "otherValue", "field3": "otherValue" }
```

#### Extending the BsonDSL

```scala
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.github.jahrim.hexarc.persistence.bson.codecs.{
  BsonDocumentDecoder, 
  BsonDocumentEncoder, 
  BsonDecoder, 
  BsonDecoder
}
import org.bson.BsonDocument

// Define a custom class and its codec
case class CustomObject(subfield1: Int, subfield2: Long, subfield3: String)

/** Codec for `CustomObject`. */
object CustomObjectCodec:
  /** A `BsonEncoder` converting `CustomObject`s to `BsonDocument`s. */
  given BsonDocumentEncoder[CustomObject] = obj =>
    bson {
      "subfield1" :: obj.subfield1
      "subfield2" :: obj.subfield2
      "subfield3" :: obj.subfield3
    }

  /** A `BsonDecoder` converting `BsonDocument`s to `CustomObject`s. */
  given BsonDocumentDecoder[CustomObject] = bson =>
    CustomObject(
      bson.require("subfield1").as[Int],
      bson.require("subfield2").as[Long],
      bson.require("subfield3").as[String]
    )

// Use interchangeably
import CustomObjectCodec.given
val document: BsonDocument = 
  bson { "objectField" :: CustomObject(10, 10L, "10") }
val customObject: CustomObject =
  document.require("objectField").as[CustomObject]
```

> **Note**: defining `BsonDocumentEncoder`s and `BsonDocumentDecoder`s is required
> to provide a custom codec for `BsonDocument`s. In order to provide a custom codec
> for _primitive types_, it's possible to use the more general `BsonEncoder`s and
> `BsonDecoder`s, which handle conversions from and to `BsonValue`s.
