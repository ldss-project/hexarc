package io.github.jahrim.hexarc.persistence.bson.codecs

import org.bson.{BsonDocument, BsonValue}

/**
 * A [[BsonEncoder]] from a typed value to its corresponding [[BsonDocument]].
 *
 * @tparam T the type of the value.
 *
 * @note the relationship between a [[BsonDocumentEncoder]] and a [[BsonEncoder]]
 *       is the same of the one between a [[BsonDocumentDecoder]] and a
 *       [[BsonDecoder]]. See [[BsonDocumentDecoder]] for more information.
 */
@FunctionalInterface
trait BsonDocumentEncoder[T] extends BsonEncoder[T]:

  override def encode(value: T): BsonValue = encodeToDocument(value)

  /** As [[encode]] but returns a [[BsonDocument]]. */
  protected def encodeToDocument(value: T): BsonDocument
