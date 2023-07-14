package io.github.jahrim.hexarc.persistence.bson.codec

import org.bson.BsonValue

/**
 * A [[Conversion]] from a typed value to its corresponding [[BsonValue]].
 *
 * @tparam T the type of the value.
 */
trait BsonEncoder[T] extends Conversion[T, BsonValue]:
  /**
   * Encode the specified typed value into the corresponding [[BsonValue]].
   *
   * @param value the specified typed value.
   * @return the specified typed value as the corresponding [[BsonValue]].
   * @note same as [[BsonEncoder.apply]].
   */
  def decode(value: T): BsonValue = this(value)
