# HexArc: Hexagonal Architecture Framework

[![GitHub Release](https://img.shields.io/github/v/tag/ldss-project/hexarc?label=Github&color=blue)](https://github.com/ldss-project/hexarc/releases)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.jahrim/hexarc?label=Maven%20Central&color=blue)](https://central.sonatype.com/artifact/io.github.jahrim/hexarc)
[![Test](https://github.com/ldss-project/hexarc/actions/workflows/continuous-testing.yml/badge.svg)](https://github.com/ldss-project/hexarc/actions/workflows/continuous-testing.yml)
[![Deployment](https://github.com/ldss-project/hexarc/actions/workflows/continuous-deployment.yml/badge.svg)](https://github.com/ldss-project/hexarc/actions/workflows/continuous-deployment.yml)
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fldss-project%2Fhexarc.svg)](https://fossa.com/)

---

Simple framework for the instantiation of services, following the best practices of the Hexagonal Architecture Pattern,
also known as Clean Architecture.

---

# Showcase

Here a brief showcase of the library.

## Architecture Module

The Architecture Module defines the components of the Hexagonal Architecture by means
of different technologies (e.g. Vertx) and provides DSLs to easily configure services
and their deployments.

In HexArc, a service is made by two types of components:
- `Port`s : the possible perspectives on the business logic of the service, 
            implemented by corresponding models.
- `Adapter`s : the technologies enabled for interacting with the `Port`s of the service.

To sum up, there are two basic clean rules:
- Enable **use cases** through `Port`s.
- Enable **technologies** through `Adapter`s.

### Defining the components of a service

Full example
[here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/examples/ColoredLampService.scala).

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.vertx.core.Future

object ColoredLampService:
  /** Ports. */
  trait LampSwitchPort extends Port:
    def switch(on: Boolean): Future[Unit]
    def isOn: Future[Boolean]

  trait LampColorPort extends Port:
    def changeColor(color: String): Future[Unit]
    def color: Future[String]

  /** Business logic. */
  class ColoredLampModel extends LampSwitchPort with LampColorPort:
    override def init(context: PortContext): Unit = ???
    override def switch(on: Boolean): Future[Unit] = ???
    override def isOn: Future[Boolean] = ???
    override def changeColor(color: String): Future[Unit] = ???
    override def color: Future[String] = ???

  /** Adapters. */
  class LampSwitchHttpAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit = ???

  class LampSwitchMqttAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit = ???

  class LampColorHttpAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit = ???

  class LampColorMqttAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit = ???
```

All the components of a service must implement a `init` method, which will be called
when the service is deployed, providing the proper execution context to every
component.

Most notably, an execution context of a service component may contain:
- `vertx`: the vertx instance where the service is running;
- `log`: a logger specific to that component;
- `api`: the `Port` exposed by that component, if the component is an `Adapter`.

Such context is also available outside the `init` method, but it cannot be safely
accessed before the component initialization.


### Deploying a single service using Vertx

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/SingleDeploymentTest.scala).

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.vertx.core.Vertx
import ColoredLampService.*

private val lampModel = ColoredLampModel()

DeploymentGroup.deploySingle(Vertx.vertx()) {     // Deploy on <Vertx.vertx()> a single
  new Service:                                    // service
    name = "ColoredLampService"                   // named "ColoredLampService" (for logging purposes)

    new Port[LampSwitchPort]:                     // with a port of type [LampSwitchPort]
      name = "SwitchPort"                         // named "SwitchPort"
      model = lampModel                           // implemented by <lampModel>

      new Adapter(LampSwitchHttpAdapter()):       // exposed by an adapter with implementation <LampSwitchHttpAdapter>
        name = "Http"                             // named "Http"
      new Adapter(LampSwitchMqttAdapter()):       // exposed by another adapter with implementation <LampSwitchMqttAdapter>
        name = "Mqtt"                             // named "Mqtt"

    new Port[LampColorPort]:                      // with another port of type [LampColorPort]
      name = "ColorPort"                          // named "SwitchPort"
      model = lampModel                           // implemented by <lampModel>

      new Adapter(LampColorHttpAdapter()):        // exposed by an adapter with implementation <LampColorHttpAdapter>
        name = "Http"                             // named "Http"
      new Adapter(LampColorMqttAdapter()):        // exposed by another adapter with implementation <LampSwitchMqttAdapter>
        name = "Mqtt"                             // named "Mqtt"
}
```

### Deploying multiple services using Vertx

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/architecture/vertx/core/dsl/GroupDeploymentTest.scala).

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.vertx.core.Vertx
import IdProviderService.*
import NamedService.*

// Supplier of IdProviderService
def idProviderService: DeploymentGroup ?=> Service =
  new Service:
    name = "IdProviderService"

    new Port[IdProviderPort]:
      model = IdProviderModel()

      new Adapter(IdProviderLocalAdapter())

// Supplier of NamedService
def namedService: DeploymentGroup ?=> Service =
  new Service:
    name = "NamedService"

    new Port[NamedPort]:
      model = NamedModel()

      new Adapter(NamedLocalAdapter())

new DeploymentGroup(Vertx.vertx()):  // Deploy on <Vertx.vertx()>
  idProviderService                  // an IdProviderService
  namedService                       // a NamedService
  namedService                       // and another NamedService
```

## Persistence Module

The Persistence Module defines the components required to make the business
logic of a service (aka a model) loosely-coupled with the data access layer.
Moreover, it provides DSLs to better manipulate data inside a service.

The main component of the Persistence Module is the `PersistenceCollection`,
which is best described as a `java.util.Collection` whose content persist
over different runs of an application.

A `PersistenceCollection` can be accessed and modified by means of CRUD
operations, namely `create`, `read`, `update` and `delete`.

Each `PersistenceCollection` must be bound to a `QueryLanguage`, which
defines the input and output types of the operations above.

### Create and use a PersistentCollection with MongoDB

```scala
import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.MongoDBPersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.language.MongoDBQueryLanguage
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.*
import org.bson.{BsonDocument, BsonString}

// Create a PersistentCollection based on MongoDB
val persistentCollection: PersistentCollection with MongoDBQueryLanguage =
  MongoDBPersistentCollection(
    connection = ???,
    database = ???,
    collection = ???
  ).get

// Insert One Document
persistentCollection.create(
  CreateQuery(
    document = BsonDocument("field", BsonString("value")),
    options = ???
  )
)

// Insert Many Documents
persistentCollection.create(
  CreateQuery.many(
    documents = Seq(???, ??? /*...*/),
    options = ???
  )
)

// Basic Read
persistentCollection.read(
  ReadQuery(
    filter = ???,
    projection = ???
  )
)

// Read with Aggregation Pipeline
persistentCollection.read(
  ReadQuery.pipeline(???, ??? /* ... */)
)

// Update many
persistentCollection.update(
  UpdateQuery(
    filter = ???,
    update = ???,
    options = ???
  )
)

// Delete many
persistentCollection.delete(
  DeleteQuery(
    filter = ???,
    options = ???
  )
)
```

Considering the `MongoDBQueryLanguage`, most queries consume `org.bson.conversion.Bson` types,
which can be created using the tools already provided by the
[Mongo Java Driver (Sync)](https://www.mongodb.com/docs/drivers/java/sync/current/),
included with this library.

### Connecting the Persistent Module to the Architecture Module

```scala
import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*
import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.MongoDBPersistentCollection
import io.github.jahrim.hexarc.persistence.mongodb.language.MongoDBQueryLanguage
import io.github.jahrim.hexarc.persistence.mongodb.language.queries.*
import ConsumerService.*

// Service Components Definition
object ConsumerService:
  trait ConsumerServicePort extends Port { /* ... */ }
  class ConsumerServiceModel(collection: PersistentCollection with MongoDBQueryLanguage) 
    extends ConsumerServicePort { /* ... use collection ... */ }
  trait ConsumerServiceLocalAdapter extends Port { /* ... */ }

// Service Deployment
DeploymentGroup.deploySingle(Vertx.vertx()) {
  new Service:                                    
    name = "ConsumerService"                 
    new Port[ConsumerServicePort]:
      model = ConsumerServiceModel(
        collection = MongoDBPersistentCollection(   // here you decide what database the model will rely on
          connection = ???,
          database = ???,
          collection = ???
        )
      )

      new Adapter(ConsumerServiceLocalAdapter())
}
```

Note how the model declares a dependency on a `PersistentCollection` that
can accept a query following the specifications of the `MongoDBQueryLanguage`,
but never declares a dependency strictly on MongoDB (e.g.
`MongoDBPersistentCollection`).

In this way, any `PersistentCollection` based on any database (e.g. MongoDB,
MySql, SqlServer...) can be served to the model, as long as it is capable of
interpreting a query written in the `MongoDBQueryLanguage` that will be used
by the model.

### Writing Bson documents faster

Full example [here](https://github.com/ldss-project/hexarc/blob/master/src/test/scala/io/github/jahrim/hexarc/persistence/bson/dsl/BsonDSLTest.scala).

Along with the other tools, HexArc also provides a DSL to write `org.bson.BsonDocument`s
much faster and in a much more declarative way with respect to standard Java.

```scala
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.BsonDocument

val document: BsonDocument = bson {
  "booleanField" :: true                      // declare a boolean
  "stringField" :: "value"                    // declare a string
  "intField" :: 10                            // declare an int32
  "longField" :: 10_000_000_000_000L          // declare an int64
  "doubleField" :: 0.33D                      // declare a double
  "dateField" :: java.time.Instant.now        // declare a date
  "arrayField1" :: array(1,2,3)               // declare an homogeneous array
  "arrayField2" :* (1,2,3)                    // shortcut syntax for declaring an homogeneous array
  "objectField1" :: bson {                    // declare an object
    "subfield" :: 0 
  }
  "objectField2" :# {                         // shortcut syntax for declaring an object
    "subfield" :: 0 
  }       
}

// Accessing an element via "apply" retrieves an Option[BsonValue]
val bsonBooleanOption: Option[BsonValue] = document("booleanField")

// BsonValues can be decoded via the method as[T]
val booleanOption: Option[Boolean] = bsonBooleanOption.map(_.as[Boolean])

// Accessing an element via "require" retrieves a BsonValue or throws a NoSuchElementException
// Note: you can access subfields with path notation.
val int: Int = document.require("objectField1.subfield").as[Int]
```

The DSL allows also usage of `given` `BsonEncoder`s and `BsonDecoder`s to make
`org.bson.BsonDocument`s interchangeable with custom Java objects.

```scala
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import org.bson.BsonDocument

case class CustomObject(subfield1: Int, subfield2: Long, subfield3: String)

// Define a BsonEncoder from CustomObject to BsonDocument
given BsonDocumentEncoder[CustomObject] = obj =>
  bson {
    "subfield1" :: obj.subfield1
    "subfield2" :: obj.subfield2
    "subfield3" :: obj.subfield3
  }

// Define a BsonDecoder from BsonDocument to CustomObject
given BsonDocumentDecoder[CustomObject] = bson =>
  CustomObject(
    bson.require("subfield1").as[Int],
    bson.require("subfield2").as[Long],
    bson.require("subfield3").as[String]
  )

// Use interchangeably
val document: BsonDocument = bson { 
  "objectField" :: CustomObject(10, 10L, "10")
}
val customObject: CustomObject =
  document.require("objectField").as[CustomObject]
```
