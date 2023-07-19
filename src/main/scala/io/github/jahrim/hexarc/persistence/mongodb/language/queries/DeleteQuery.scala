package io.github.jahrim.hexarc.persistence.mongodb.language.queries

import com.mongodb.client.model.DeleteOptions
import org.bson.conversions.Bson

/** A query for deleting documents in [[https://www.mongodb.com/ MongoDB]]. */
case class DeleteQuery private (filter: Bson, options: DeleteOptions)

/** Companion object of [[DeleteQuery]]. */
object DeleteQuery:
  /**
   * @param filter  selects which documents will be deleted.
   * @param options additional deletion options.
   * @return a [[DeleteQuery]] for deleting the documents selected by the specified
   *         filter, considering the specified deletion options.
   */
  def apply(filter: Bson, options: DeleteOptions = DeleteOptions()): DeleteQuery =
    new DeleteQuery(filter, options)
