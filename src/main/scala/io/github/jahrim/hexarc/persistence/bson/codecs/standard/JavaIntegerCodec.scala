package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt32, BsonValue}

import java.lang.Integer as JavaInteger

/** [[BsonValue]] codec for [[JavaInteger]]. */
object JavaIntegerCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaInteger]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt32]].
   */
  given javaIntegerDecoder: BsonDecoder[JavaInteger] = bson =>
    JavaInteger.valueOf(bson.asInt32.getValue)

  /** A given [[BsonEncoder]] for [[JavaInteger]]. */
  given javaIntegerEncoder: BsonEncoder[JavaInteger] = integer => BsonInt32(integer.intValue)
