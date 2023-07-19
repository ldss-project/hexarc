package io.github.jahrim.hexarc.persistence.mongodb.language.queries

import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** A query for reading documents in [[https://www.mongodb.com/ MongoDB]]. */
case class ReadQuery private (aggregation: Bson*)

/** Companion object of [[ReadQuery]]. */
object ReadQuery:
  /**
   * @param filter     the specified filter.
   * @param projection the specified projection.
   * @return a new [[ReadQuery]] that read the fields selected by the
   *         specified projection of the documents selected by the specified
   *         filter.
   */
  def apply(filter: Bson = emptyBson, projection: Bson = emptyBson): ReadQuery =
    new ReadQuery(bson { "$match" :: filter }, bson { "$project" :: projection })

  /**
   * @param aggregation the aggregation pipeline for reading documents in
   *                    [[https://www.mongodb.com/ MongoDB]].
   * @return a new [[ReadQuery]] that read the fields as for the specified
   *         aggregation pipeline.
   */
  def pipeline(aggregation: Bson*): ReadQuery = new ReadQuery(aggregation*)
