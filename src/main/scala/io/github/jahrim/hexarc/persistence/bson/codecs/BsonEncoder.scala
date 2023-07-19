package io.github.jahrim.hexarc.persistence.bson.codecs

import org.bson.BsonValue

/**
 * An encoder from a typed value to its corresponding [[BsonValue]].
 *
 * @tparam T the type of the typed value.
 */
@FunctionalInterface
trait BsonEncoder[T]:
  /**
   * Encode the specified typed value into the corresponding [[BsonValue]].
   *
   * @param value the specified typed value.
   * @return the specified typed value as the corresponding [[BsonValue]].
   * @note same as [[BsonEncoder.apply]].
   */
  def encode(value: T): BsonValue

  /** Alias for [[encode]]. */
  def apply(value: T): BsonValue = this.encode(value)
