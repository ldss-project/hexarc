package io.github.jahrim.hexarc.persistence.mongodb

import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.github.jahrim.hexarc.persistence.bson.PersistentDocumentCollection

import com.mongodb.client.model.{DeleteOptions, InsertManyOptions, InsertOneOptions, UpdateOptions}
import com.mongodb.client.{MongoClients, MongoCollection}
import com.mongodb.{ConnectionString, MongoClientSettings, ServerApi, ServerApiVersion}
import org.bson.conversions.Bson
import org.bson.{BsonBoolean, BsonDocument, BsonInt32}

import scala.jdk.CollectionConverters.{IterableHasAsScala, SeqHasAsJava}
import scala.util.Try

/** A [[PersistentDocumentCollection]] relying on a [[https://www.mongodb.com/ MongoDB]] instance. */
class MongoDBPersistentCollection private (collection: MongoCollection[BsonDocument])
    extends PersistentDocumentCollection:

  /**
   * As [[PersistentDocumentCollection.create]].
   *
   * @note Available retrieval options are the following:
   *       - bypassDocumentValidation: [[BsonBoolean]]:
   *         see [[InsertOneOptions.bypassDocumentValidation]].
   */
  override def create(document: Bson, options: Bson = emptyBson): Try[Unit] =
    Try {
      collection.insertOne(
        document,
        InsertOneOptions()
          .bypassDocumentValidation(
            options.get("bypassDocumentValidation", false)
          )
      )
    }

  /**
   * As [[PersistentDocumentCollection.createMany]].
   *
   * @note Available retrieval options are the following:
   *       - bypassDocumentValidation: [[BsonBoolean]]:
   *         see [[InsertManyOptions.bypassDocumentValidation]].
   *       - ordered: [[BsonBoolean]]:
   *         see [[InsertManyOptions.ordered]].
   */
  override def createMany(documents: Seq[Bson], options: Bson): Try[Unit] =
    Try {
      collection.insertMany(
        documents.map(bsonToBsonDocument).asJava,
        InsertManyOptions()
          .bypassDocumentValidation(options.get("bypassDocumentValidation", false))
          .ordered(options.get("ordered", false))
      )
    }

  /**
   * As [[PersistentDocumentCollection.read]].
   *
   * @note Available retrieval options are the following:
   *       - skip: [[BsonInt32]]:
   *         see [[MongoCollection.skip]].
   *       - limit: [[BsonInt32]]:
   *         see [[MongoCollection.limit]].
   *       - sort: [[BsonDocument]]:
   *         see [[MongoCollection.sort]].
   */
  override def read(
      filter: Bson = emptyBson,
      projection: Bson = emptyBson,
      options: Bson = emptyBson
  ): Try[Seq[Bson]] =
    Try {
      Seq.from(
        collection
          .find()
          .filter(filter)
          .projection(projection)
          .skip(options.get("skip", 0))
          .limit(options.get("limit", 0))
          .sort(options.get("sort", emptyBson))
          .asScala
      )
    }

  /**
   * As [[PersistentDocumentCollection.update]].
   *
   * @note Available retrieval options are the following:
   *       - bypassDocumentValidation: [[BsonBoolean]]:
   *         see [[UpdateOptions.bypassDocumentValidation]].
   */
  override def update(filter: Bson, update: Bson, options: Bson = emptyBson): Try[Unit] =
    Try {
      collection.updateMany(
        filter,
        update,
        UpdateOptions()
          .bypassDocumentValidation(options.get("bypassDocumentValidation", false))
      )
    }

  /** As [[PersistentDocumentCollection.delete]]. */
  override def delete(filter: Bson, options: Bson = emptyBson): Try[Unit] =
    Try { collection.deleteMany(filter, DeleteOptions()) }

/** Companion object of [[MongoDBPersistentCollection]]. */
object MongoDBPersistentCollection:

  // Add the Google DNS in case it is needed to solve MongoDB connection strings
  System.setProperty("java.naming.provider.url", "dns://8.8.8.8")

  /**
   * Create a new [[MongoDBPersistentCollection]] connected to the specified
   * collection within the specified database of the specified MongoDB instance.
   *
   * @param connection the connection to the specified MongoDB instance.
   * @param database   the specified database within the MongoDB instance.
   * @param collection the collection within the specified database.
   * @return a new [[MongoDBPersistentCollection]].
   */
  def apply(
      connection: String,
      database: String,
      collection: String
  ): Try[MongoDBPersistentCollection] =
    Try {
      MongoClients
        .create(
          MongoClientSettings
            .builder()
            .applyConnectionString(new ConnectionString(connection))
            .serverApi(
              ServerApi
                .builder()
                .version(ServerApiVersion.V1)
                .build()
            )
            .build()
        )
        .getDatabase(database)
        .getCollection(collection, classOf[BsonDocument])
        .toPersistentCollection
    }

  extension [Doc](self: MongoCollection[Doc]) {

    /** @return a new [[MongoDBPersistentCollection]] connected to this [[MongoCollection]]. */
    def toPersistentCollection: MongoDBPersistentCollection =
      new MongoDBPersistentCollection(self.withDocumentClass(classOf[BsonDocument]))
  }
