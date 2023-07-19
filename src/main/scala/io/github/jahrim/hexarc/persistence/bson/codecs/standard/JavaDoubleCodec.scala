package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDouble, BsonValue}

import java.lang.Double as JavaDouble

/** [[BsonValue]] codec for [[JavaDouble]]. */
object JavaDoubleCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaDouble]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDouble]].
   */
  given javaDoubleDecoder: BsonDecoder[JavaDouble] = bson =>
    JavaDouble.valueOf(bson.asDouble.getValue)

  /** A given [[BsonEncoder]] for [[JavaDouble]]. */
  given javaDoubleEncoder: BsonEncoder[JavaDouble] = double => BsonDouble(double.doubleValue)
