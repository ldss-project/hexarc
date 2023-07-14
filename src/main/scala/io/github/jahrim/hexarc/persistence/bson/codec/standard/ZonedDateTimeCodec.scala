package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonDateTime, BsonValue}

import java.time.{ZoneId, ZonedDateTime}
import java.util.Date

/** [[BsonValue]] codec for [[ZonedDateTime]]. */
object ZonedDateTimeCodec:
  /**
   * A given [[BsonDecoder]] for [[ZonedDateTime]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDateTime]].
   */
  given bsonToDate: BsonDecoder[ZonedDateTime] = bson =>
    ZonedDateTime.ofInstant(Date(bson.asDateTime.getValue).toInstant, ZoneId.of("Z"))

  /** A given [[BsonEncoder]] for [[ZonedDateTime]]. */
  given dateToBson: BsonEncoder[ZonedDateTime] = date => BsonDateTime(date.toInstant.toEpochMilli)
