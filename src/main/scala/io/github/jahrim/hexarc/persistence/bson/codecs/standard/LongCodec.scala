package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt64, BsonValue}

/** [[BsonValue]] codec for [[Long]]. */
object LongCodec:
  /**
   * A given [[BsonDecoder]] for [[Long]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt64]].
   */
  given longDecoder: BsonDecoder[Long] = _.asInt64.getValue

  /** A given [[BsonEncoder]] for [[Long]]. */
  given longEncoder: BsonEncoder[Long] = BsonInt64(_)
