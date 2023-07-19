package io.github.jahrim.hexarc.persistence.bson.codecs

import org.bson.{BsonValue, BsonDocument}

/**
 * A [[BsonDecoder]] from a [[BsonDocument]] to its corresponding typed value.
 *
 * @tparam T the type of the value.
 *
 * @note a [[BsonDocumentDecoder]] is more specific than a [[BsonDecoder]] as
 *       it requires the [[BsonValue]] to be a [[BsonDocument]].
 *       If the decoding process requires a [[BsonDocument]], a
 *       [[BsonDocumentDecoder]] can be implemented instead of casting the
 *       [[BsonValue]] provided when implementing a [[BsonDecoder]].
 */
@FunctionalInterface
trait BsonDocumentDecoder[T] extends BsonDecoder[T]:

  override def decode(bson: BsonValue): T = decodeFromDocument(bson.asDocument)

  /** As [[decode]] but treats the [[BsonValue]] as a [[BsonDocument]]. */
  protected def decodeFromDocument(bson: BsonDocument): T
