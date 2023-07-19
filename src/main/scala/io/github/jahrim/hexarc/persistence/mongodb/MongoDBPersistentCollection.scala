package io.github.jahrim.hexarc.persistence.mongodb

import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import com.mongodb.client.{MongoClients, MongoCollection}
import com.mongodb.{ConnectionString, MongoClientSettings, ServerApi, ServerApiVersion}
import io.github.jahrim.hexarc.persistence.mongodb.language.MongoDBQueryLanguage
import org.bson.BsonDocument

import scala.jdk.CollectionConverters.{IterableHasAsScala, SeqHasAsJava}
import scala.util.Try

/** A [[PersistentCollection]] relying on a [[https://www.mongodb.com/ MongoDB]] instance. */
class MongoDBPersistentCollection private (collection: MongoCollection[BsonDocument])
    extends PersistentCollection
    with MongoDBQueryLanguage:

  override def create(query: CreateQuery): Try[CreateQueryResult] =
    Try { collection.insertMany(query.documents.map(_.toBsonDocument).asJava, query.options) }

  override def read(query: ReadQuery): Try[ReadQueryResult] =
    Try { collection.aggregate(query.aggregation.asJava).asScala.toSeq }

  override def update(query: UpdateQuery): Try[UpdateQueryResult] =
    Try { collection.updateMany(query.filter, query.update, query.options) }

  override def delete(query: DeleteQuery): Try[DeleteQueryResult] =
    Try { collection.deleteMany(query.filter, query.options) }

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
          MongoClientSettings.builder
            .applyConnectionString(new ConnectionString(connection))
            .serverApi(ServerApi.builder.version(ServerApiVersion.V1).build())
            .build()
        )
        .getDatabase(database)
        .getCollection(collection)
        .toPersistentCollection
    }

  extension [Doc](self: MongoCollection[Doc]) {

    /** @return a new [[MongoDBPersistentCollection]] connected to this [[MongoCollection]]. */
    def toPersistentCollection: MongoDBPersistentCollection =
      new MongoDBPersistentCollection(self.withDocumentClass(classOf[BsonDocument]))
  }
