package io.github.jahrim.hexarc.persistence.bson.codec

import org.bson.BsonValue

/**
 * A [[Conversion]] from a [[BsonValue]] to its corresponding typed value.
 *
 * @tparam T the type of the value.
 */
trait BsonDecoder[T] extends Conversion[BsonValue, T]:
  /**
   * Decode the specified [[BsonValue]] to the corresponding typed value.
   *
   * @param bson the specified [[BsonValue]].
   * @return the specified [[BsonValue]] as the corresponding typed value.
   * @note same as [[BsonDecoder.apply]].
   */
  def decode(bson: BsonValue): T = this(bson)
