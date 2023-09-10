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
