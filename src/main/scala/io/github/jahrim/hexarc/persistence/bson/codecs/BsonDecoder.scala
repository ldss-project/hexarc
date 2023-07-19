package io.github.jahrim.hexarc.persistence.bson.codecs

import org.bson.BsonValue

/**
 * A decoder from a [[BsonValue]] to its corresponding typed value.
 *
 * @tparam T the type of the typed value.
 */
@FunctionalInterface
trait BsonDecoder[T]:
  /**
   * Decode the specified [[BsonValue]] to the corresponding typed value.
   *
   * @param bson the specified [[BsonValue]].
   * @return the specified [[BsonValue]] as the corresponding typed value.
   * @note same as [[BsonDecoder.apply]].
   */
  def decode(bson: BsonValue): T

  /** Alias for [[decode]]. */
  def apply(bson: BsonValue): T = this.decode(bson)
