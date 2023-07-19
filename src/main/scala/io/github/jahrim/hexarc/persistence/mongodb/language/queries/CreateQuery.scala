package io.github.jahrim.hexarc.persistence.mongodb.language.queries

import com.mongodb.client.model.InsertManyOptions
import org.bson.conversions.Bson

/** A query for creating the specified documents in [[https://www.mongodb.com/ MongoDB]]. */
case class CreateQuery private (documents: Seq[Bson], options: InsertManyOptions)

/** Companion object of [[CreateQuery]]. */
object CreateQuery:
  /**
   * @param document the specified document.
   * @param options  additional creation options.
   * @return a [[CreateQuery]] for creating the specified document with the
   *         specified creation options.
   */
  def apply(document: Bson, options: InsertManyOptions = InsertManyOptions()): CreateQuery =
    new CreateQuery(Seq(document), options)

  /**
   * @param documents the specified documents.
   * @param options   additional creation options.
   * @return a [[CreateQuery]] for creating the specified documents with the
   *         specified creation options.
   */
  def many(documents: Seq[Bson], options: InsertManyOptions = InsertManyOptions()): CreateQuery =
    new CreateQuery(documents, options)
