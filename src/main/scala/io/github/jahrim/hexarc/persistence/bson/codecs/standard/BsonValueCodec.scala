package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonValue

/** [[BsonValue]] codec for [[BsonValue]]. */
object BsonValueCodec:
  /** A given [[BsonDecoder]] for [[BsonValue]]. */
  given bsonValueDecoder[T <: BsonValue]: BsonDecoder[T] = _.asInstanceOf[T]

  /** A given [[BsonEncoder]] for [[BsonValue]]. */
  given bsonValueEncoder[T <: BsonValue]: BsonEncoder[T] = identity
