package io.github.jahrim.hexarc.persistence.mongodb.language

import com.mongodb.client.result.{DeleteResult, InsertManyResult, UpdateResult}
import io.github.jahrim.hexarc.persistence.{PersistentCollection, QueryLanguage}
import org.bson.BsonDocument

/** The [[QueryLanguage]] of [[https://www.mongodb.com/ MongoDB]]. */
trait MongoDBQueryLanguage extends QueryLanguage:
  override type CreateQuery = queries.CreateQuery
  override type ReadQuery = queries.ReadQuery
  override type UpdateQuery = queries.UpdateQuery
  override type DeleteQuery = queries.DeleteQuery
  override type CreateQueryResult = InsertManyResult
  override type ReadQueryResult = Seq[BsonDocument]
  override type UpdateQueryResult = UpdateResult
  override type DeleteQueryResult = DeleteResult
