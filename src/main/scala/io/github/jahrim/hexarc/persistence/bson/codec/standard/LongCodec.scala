package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonInt64, BsonValue}

/** [[BsonValue]] codec for [[Long]]. */
object LongCodec:
  /**
   * A given [[BsonDecoder]] for [[Long]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonInt64]].
   */
  given bsonToLong: BsonDecoder[Long] = _.asInt64.getValue

  /** A given [[BsonEncoder]] for [[Long]]. */
  given longToBson: BsonEncoder[Long] = BsonInt64(_)
