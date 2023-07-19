package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDouble, BsonValue}

/** [[BsonValue]] codec for [[Double]]. */
object DoubleCodec:
  /**
   * A given [[BsonDecoder]] for [[Double]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDouble]].
   */
  given doubleDecoder: BsonDecoder[Double] = _.asDouble.getValue

  /** A given [[BsonEncoder]] for [[Double]]. */
  given doubleEncoder: BsonEncoder[Double] = BsonDouble(_)
