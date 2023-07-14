package io.github.jahrim.hexarc.persistence.bson

import io.github.jahrim.hexarc.persistence.PersistentCollection
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

import scala.util.Try

/** A [[PersistentCollection]] of [[Bson]] documents. */
trait PersistentDocumentCollection
    extends PersistentCollection[Bson, Bson, Bson, Bson, Bson, Bson, Bson, Bson]:
  override def create(document: Bson, options: Bson = emptyBson): Try[Unit]

  override def read(
      filter: Bson = emptyBson,
      projection: Bson = emptyBson,
      options: Bson = emptyBson
  ): Try[Seq[Bson]]

  override def update(filter: Bson, update: Bson, options: Bson = emptyBson): Try[Unit]

  override def delete(filter: Bson, options: Bson = emptyBson): Try[Unit]

  override def createMany(documents: Seq[Bson], options: Bson = emptyBson): Try[Unit] =
    super.createMany(documents, options)
