package io.github.jahrim.hexarc.persistence.mongodb.language.queries

import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import com.mongodb.client.model.UpdateOptions
import org.bson.conversions.Bson

/** A query for updating documents in [[https://www.mongodb.com/ MongoDB]]. */
case class UpdateQuery private (filter: Bson, update: Bson, options: UpdateOptions)

/** Companion object of [[UpdateQuery]]. */
object UpdateQuery:
  /**
   * @param filter  selects which documents will be updated.
   * @param update  defines how the documents should be updated.
   * @param options additional update options.
   * @return a [[UpdateQuery]] for updating the documents selected by the specified
   *         filter with the specified update, considering the specified update options.
   */
  def apply(
      filter: Bson = emptyBson,
      update: Bson = emptyBson,
      options: UpdateOptions = UpdateOptions()
  ): UpdateQuery =
    new UpdateQuery(filter, update, options)
