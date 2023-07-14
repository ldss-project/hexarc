package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDouble, BsonValue}

import java.lang.Double as JavaDouble

/** [[BsonValue]] codec for [[JavaDouble]]. */
object JavaDoubleCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaDouble]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDouble]].
   */
  given bsonToJavaDouble: BsonDecoder[JavaDouble] = bson =>
    JavaDouble.valueOf(bson.asDouble.getValue)

  /** A given [[BsonEncoder]] for [[JavaDouble]]. */
  given javaDoubleToBson: BsonEncoder[JavaDouble] = double => BsonDouble(double.doubleValue)
