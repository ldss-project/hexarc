package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonBoolean, BsonDocument, BsonValue}

/** [[BsonValue]] codec for [[Boolean]]. */
object BooleanCodec:
  /**
   * A given [[BsonDecoder]] for [[Boolean]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonBoolean]].
   */
  given bsonToBoolean: BsonDecoder[Boolean] = _.asBoolean.getValue

  /** A given [[BsonEncoder]] for [[Boolean]]. */
  given booleanToBson: BsonEncoder[Boolean] = BsonBoolean(_)
