package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt32, BsonValue}

import java.lang.Integer as JavaInteger

/** [[BsonValue]] codec for [[JavaInteger]]. */
object JavaIntegerCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaInteger]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt32]].
   */
  given bsonToJavaInteger: BsonDecoder[JavaInteger] = bson =>
    JavaInteger.valueOf(bson.asInt32.getValue)

  /** A given [[BsonEncoder]] for [[JavaInteger]]. */
  given javaIntegerToBson: BsonEncoder[JavaInteger] = integer => BsonInt32(integer.intValue)
