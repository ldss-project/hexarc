package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonString, BsonValue}

/** [[BsonValue]] codec for [[String]]. */
object StringCodec:
  /**
   * A given [[BsonDecoder]] for [[String]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonString]].
   */
  given stringDecoder: BsonDecoder[String] = _.asString.getValue

  /** A given [[BsonEncoder]] for [[String]]. */
  given stringEncoder: BsonEncoder[String] = BsonString(_)
