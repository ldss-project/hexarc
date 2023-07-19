package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDateTime, BsonValue}

import java.time.Instant

/** [[BsonValue]] codec for [[Instant]]. */
object InstantCodec:
  /**
   * A given [[BsonDecoder]] for [[Instant]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDateTime]].
   */
  given instantDecoder: BsonDecoder[Instant] = bson =>
    Instant.ofEpochMilli(bson.asDateTime.getValue)

  /** A given [[BsonEncoder]] for [[Instant]]. */
  given instantEncoder: BsonEncoder[Instant] = instant => BsonDateTime(instant.toEpochMilli)
