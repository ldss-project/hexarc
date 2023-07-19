package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonValue, BsonDocument}
import org.bson.conversions.Bson

/** [[BsonValue]] codec for [[Bson]]. */
object BsonCodec:
  /**
   * A given [[BsonDecoder]] for [[Bson]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonDocument]].
   */
  given bsonDecoder: BsonDecoder[Bson] = _.asDocument

  /** A given [[BsonEncoder]] for [[Bson]]. */
  given bsonEncoder: BsonEncoder[Bson] = _.toBsonDocument
