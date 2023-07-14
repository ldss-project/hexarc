package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDouble, BsonValue}

/** [[BsonValue]] codec for [[Double]]. */
object DoubleCodec:
  /**
   * A given [[BsonDecoder]] for [[Double]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDouble]].
   */
  given bsonToDouble: BsonDecoder[Double] = _.asDouble.getValue

  /** A given [[BsonEncoder]] for [[Double]]. */
  given doubleToBson: BsonEncoder[Double] = BsonDouble(_)
