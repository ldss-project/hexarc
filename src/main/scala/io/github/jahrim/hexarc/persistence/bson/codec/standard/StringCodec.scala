package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonString, BsonValue}

/** [[BsonValue]] codec for [[String]]. */
object StringCodec:
  /**
   * A given [[BsonDecoder]] for [[String]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonString]].
   */
  given bsonToString: BsonDecoder[String] = _.asString.getValue

  /** A given [[BsonEncoder]] for [[String]]. */
  given stringToBson: BsonEncoder[String] = BsonString(_)
