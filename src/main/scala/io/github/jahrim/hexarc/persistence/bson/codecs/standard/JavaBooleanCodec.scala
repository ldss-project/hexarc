package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonBoolean, BsonValue}

import java.lang.Boolean as JavaBoolean

/** [[BsonValue]] codec for [[JavaBoolean]]. */
object JavaBooleanCodec:
  /**
   * A given [[BsonDecoder]] for [[JavaBoolean]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonBoolean]].
   */
  given javaBooleanDecoder: BsonDecoder[JavaBoolean] = bson =>
    JavaBoolean.valueOf(bson.asBoolean.getValue)

  /** A given [[BsonEncoder]] for [[JavaBoolean]]. */
  given javaBooleanEncoder: BsonEncoder[JavaBoolean] = boolean => BsonBoolean(boolean.booleanValue)
