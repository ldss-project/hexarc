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

import org.bson.{BsonValue, BsonDocument}

/**
 * A [[BsonDecoder]] from a [[BsonDocument]] to its corresponding typed value.
 *
 * @tparam T the type of the value.
 *
 * @note a [[BsonDocumentDecoder]] is more specific than a [[BsonDecoder]] as
 *       it requires the [[BsonValue]] to be a [[BsonDocument]].
 *       If the decoding process requires a [[BsonDocument]], a
 *       [[BsonDocumentDecoder]] can be implemented instead of casting the
 *       [[BsonValue]] provided when implementing a [[BsonDecoder]].
 */
@FunctionalInterface
trait BsonDocumentDecoder[T] extends BsonDecoder[T]:

  override def decode(bson: BsonValue): T = decodeFromDocument(bson.asDocument)

  /** As [[decode]] but treats the [[BsonValue]] as a [[BsonDocument]]. */
  protected def decodeFromDocument(bson: BsonDocument): T
