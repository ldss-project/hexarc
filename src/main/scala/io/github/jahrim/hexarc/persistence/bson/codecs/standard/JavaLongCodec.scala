package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt64, BsonValue}

import java.lang.Long as JavaLong

/** [[BsonValue]] codec for [[JavaLong]]. */
object JavaLongCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaLong]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt64]].
   */
  given javaLongDecoder: BsonDecoder[JavaLong] = bson => JavaLong.valueOf(bson.asInt64.getValue)

  /** A given [[BsonEncoder]] for [[JavaLong]]. */
  given javaLongEncoder: BsonEncoder[JavaLong] = long => BsonInt64(long.longValue)
