/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
