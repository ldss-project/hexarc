package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.BsonValue

/** [[BsonValue]] generic codec. */
object GenericCodec:

  extension (self: BsonValue) {

    /**
     * Map this [[BsonValue]] to the specified type, given
     * a proper [[BsonDecoder]].
     *
     * @tparam T the specified type.
     * @return the value of this [[BsonValue]] as a [[T]].
     */
    def as[T](using conversion: BsonDecoder[T]): T = conversion(self)
  }

  extension [T](self: T) {

    /**
     * Map this object to a corresponding [[BsonValue]], given
     * a proper [[BsonEncoder]].
     *
     * @return the value of the [[BsonValue]] as a [[T]].
     */
    def asBson(using conversion: BsonEncoder[T]): BsonValue = conversion(self)
  }
