package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt32, BsonValue}

/** [[BsonValue]] codec for [[Int]]. */
object IntCodec:
  /**
   * A given [[BsonDecoder]] for [[Int]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt32]].
   */
  given intDecoder: BsonDecoder[Int] = _.asInt32.getValue

  /** A given [[BsonEncoder]] for [[Int]]. */
  given intEncoder: BsonEncoder[Int] = BsonInt32(_)
