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
import org.bson.{BsonArray, BsonDocument, BsonElement, BsonValue}

import scala.annotation.targetName
import scala.jdk.CollectionConverters.SeqHasAsJava

/**
 * A DSL for creating [[BsonDocument]]s.
 *
 * @example Creating a [[BsonDocument]]: {{{
 *   import BsonDSL.{*, given}
 *   val document: BsonDocument = bson {
 *     "booleanField" :: true
 *     "stringField" :: "value"
 *     "intField" :: 10
 *     "longField" :: 10_000_000_000_000L
 *     "doubleField" :: 0.33D
 *     "dateField" :: java.time.Instant.now
 *     "arrayField1" :: array(1,2,3)
 *     "arrayField2" :* (1,2,3)
 *     "objectField1" :: bson { "subfield" :: 0 }
 *     "objectField2" :# { "subfield" :: 0 }
 *   }
 * }}}
 */
object BsonDSL:
  // Bson Codecs
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.BooleanCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.BsonCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.BsonValueCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.DoubleCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.GenericCodec.*
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.IntCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.JavaBooleanCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.JavaDoubleCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.JavaIntegerCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.JavaLongCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.LongCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.SeqCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.StringCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codecs.standard.InstantCodec.given
  export BsonExtension.{*, given}

  /** @return an empty [[BsonDocument]]. */
  def emptyBson: BsonDocument = bson {}

  /**
   * Create a new [[BsonDocument]] defined by the specified [[BsonSpecification]].
   *
   * @param specification the specified [[BsonSpecification]].
   * @return a new [[BsonDocument]] defined by the specified [[BsonSpecification]].
   */
  def bson(specification: BsonSpecification ?=> Unit): BsonDocument =
    val specificationContext: BsonSpecification = BsonSpecification()
    specification(using specificationContext)
    parse(specificationContext)

  /**
   * Create a new [[BsonArray]] containing the specified typed values,
   * given a proper [[BsonEncoder]] for their type.
   *
   * @param values the specified values.
   * @tparam T the type of the specified typed values.
   * @return a new [[BsonArray]] containing the specified values.
   */
  def array[T: BsonEncoder](values: T*): BsonArray = seqEncoder.encode(values).asArray

  extension (self: BsonDocument) {

    /**
     * Update this [[BsonDocument]] with the specified update
     * [[BsonSpecification]].
     *
     * @param specification the specified update [[BsonSpecification]].
     * @return a new [[BsonDocument]] obtained by applying the specified
     *         [[BsonSpecification]] to this [[BsonDocument]].
     * @note this method creates a new [[BsonDocument]], leaving this
     *       [[BsonDocument]] unchanged.
     */
    def update(specification: BsonSpecification ?=> Unit): BsonDocument =
      val copy: BsonDocument = self.clone()
      copy.putAll(bson { specification })
      copy
  }

  extension (key: String) {

    /**
     * Bind this key to the specified typed value within the given
     * [[BsonSpecification]], given a proper [[BsonEncoder]] for its type.
     *
     * @param value         the specified value.
     * @param specification the given [[BsonSpecification]].
     * @tparam T the type of the specified typed values.
     */
    @targetName("bindToValue")
    infix def ::[T: BsonEncoder](value: T)(using specification: BsonSpecification): Unit =
      specification.define(key, summon[BsonEncoder[T]].encode(value))

    /**
     * As [[::]] but binds this key to a [[BsonDocument]].
     * In particular,
     * {{{ bson { "object" :# { "field" :: 1 } } }}}
     * is a shortcut equivalent to
     * {{{ bson { "object" :: bson { "field" :: 1 } } }}}
     */
    @targetName("bindToDocument")
    infix def :#(specification: BsonSpecification ?=> Unit)(using BsonSpecification): Unit =
      key :: bson { specification }

    /**
     * As [[::]] but binds this key to a [[BsonArray]].
     * In particular,
     * {{{ bson { "array" :* (1,2,3) } }}}
     * is a shortcut equivalent to
     * {{{ bson { "field" :: array(1,2,3) } }}}
     */
    @targetName("bindToArray")
    infix def :*[T: BsonEncoder](values: T*)(using BsonSpecification): Unit =
      key :: array(values*)
  }

  /**
   * Parse the specified [[BsonSpecification]] producing a [[BsonDocument]].
   *
   * @param specification the specified [[BsonSpecification]].
   * @return the [[BsonDocument]] described by the specified [[BsonSpecification]].
   */
  private def parse(specification: BsonSpecification): BsonDocument =
    BsonDocument(specification.definitions.map(entry => BsonElement(entry._1, entry._2)).asJava)
