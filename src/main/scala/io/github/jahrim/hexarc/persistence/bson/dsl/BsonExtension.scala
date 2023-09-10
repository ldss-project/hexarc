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
package io.github.jahrim.hexarc.persistence.bson.dsl

import io.github.jahrim.hexarc.persistence.bson.codecs.BsonEncoder
import org.bson.{BsonValue, BsonDocument}

import scala.util.Try
import java.util.NoSuchElementException

/** Extension for [[BsonDocument]]s. */
object BsonExtension:
  extension (self: BsonDocument) {

    /**
     * Get the value bound to the specified path within this [[BsonDocument]].
     *
     * @param path the specified path.
     * @return a [[Some]] containing the value bound to the specified path if
     *         present; a [[None]] otherwise.
     */
    def apply(path: String): Option[BsonValue] = Some(self)(path)

    /**
     * As [[BsonDocument.apply]], but throws if no value is bound to the specified path.
     * @throws NoSuchElementException if no value is bound to the specified path.
     */
    def require(path: String): BsonValue = Some(self).require(path)
  }

  extension (self: Option[BsonValue]) {

    /**
     * Get the value bound to the specified path within this [[BsonValue]].
     *
     * @param path the specified path.
     * @return a [[Some]] containing the value bound to the specified path if
     *         present; a [[None]] otherwise.
     */
    def apply(path: String): Option[BsonValue] =
      val pathAsArray: Array[String | Int] =
        path.split('.').map(key => Try { key.toInt }.fold(e => key, i => i))
      pathAsArray
        .drop(1)
        .foldLeft(
          pathAsArray.headOption.flatMap(key => self.extract(key))
        )((acc, next) => acc.extract(next))

    /**
     * As [[BsonValue.apply]], but throws if no value is bound to the specified path.
     * @throws NoSuchElementException if no value is bound to the specified path.
     */
    def require(path: String): BsonValue =
      self(path).getOrElse(throw NoSuchElementException(s"Field '$path' missing in BsonDocument."))

    /**
     * Extract the value bound to the specified key.
     * @param key the specified key, which is either a name or an index.
     * @return the value bound to the specified key.
     */
    private def extract(key: String | Int): Option[BsonValue] =
      Try {
        self.flatMap(doc =>
          Option(key match
            case string: String => doc.asDocument.get(string)
            case int: Int       => doc.asArray.get(int)
          )
        )
      }.toOption.flatten
  }
