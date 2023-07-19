package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonBoolean, BsonDocument, BsonValue}

/** [[BsonValue]] codec for [[Boolean]]. */
object BooleanCodec:
  /**
   * A given [[BsonDecoder]] for [[Boolean]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonBoolean]].
   */
  given booleanDecoder: BsonDecoder[Boolean] = _.asBoolean.getValue

  /** A given [[BsonEncoder]] for [[Boolean]]. */
  given booleanEncoder: BsonEncoder[Boolean] = BsonBoolean(_)
