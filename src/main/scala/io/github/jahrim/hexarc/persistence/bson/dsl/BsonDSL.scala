package io.github.jahrim.hexarc.persistence.bson.dsl

import io.github.jahrim.hexarc.persistence.bson.codec.BsonEncoder
import org.bson.conversions.Bson
import org.bson.{BsonArray, BsonDocument, BsonElement, BsonValue}

import scala.annotation.targetName
import scala.jdk.CollectionConverters.SeqHasAsJava

/**
 * A DSL for creating [[Bson]] documents.
 *
 * @example Creating a [[Bson]] document: {{{
 *   import BsonDSL.{*, given}
 *   val myBson: Bson = bson {
 *     "booleanField" :: true
 *     "stringField" :: "value"
 *     "intField" :: 10
 *     "longField" :: 10_000_000_000_000L
 *     "doubleField" :: 0.33D
 *     "dateField" :: ZonedDateTime.parse("2023-12-25T12:00:00Z")
 *     "arrayField" :: array(1, false, "string")
 *     "documentField" :: bson { "subfield" :: 0 }
 *   }
 * }}}
 */
object BsonDSL:
  // Bson Codecs
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.BooleanCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.DoubleCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.GenericCodec.*
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.IntCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.JavaBooleanCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.JavaDoubleCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.JavaIntegerCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.JavaLongCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.LongCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.SeqCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.StringCodec.given
  export io.github.jahrim.hexarc.persistence.bson.codec.standard.ZonedDateTimeCodec.given
  export BsonExtension.{*, given}

  /** @return an empty [[Bson]] document. */
  def emptyBson: Bson = bson {}

  /**
   * Create a new [[Bson]] document defined by the specified [[BsonSpecification]].
   *
   * @param specification the specified [[BsonSpecification]].
   * @return a new [[Bson]] document defined by the specified [[BsonSpecification]].
   */
  def bson(specification: BsonSpecification ?=> Unit): Bson =
    val specificationContext: BsonSpecification = BsonSpecification()
    specification(using specificationContext)
    parse(specificationContext)

  /**
   * Create a new [[BsonArray]] containing the specified [[BsonValue]]s.
   *
   * @param values the specified [[BsonValue]]s.
   * @return a new [[BsonArray]] containing the specified [[BsonValue]]s.
   * @note this method allows to convert an heterogeneous vararg sequence
   *       of values into a sequence of [[BsonValue]]s.
   *       While attempting to do the same with an heterogeneous [[Seq]]
   *       (e.g. of [[Any]]s), the conversion will fail unless a proper
   *       [[BsonEncoder]] is provided (e.g. a [[BsonEncoder]] for
   *       [[Any]]s).
   */
  def array(values: BsonValue*): BsonValue = values.toSeq

  extension (self: Bson) {

    /**
     * Update this [[Bson]] document with the specified update
     * [[BsonSpecification]].
     *
     * @param specification the specified update [[BsonSpecification]].
     * @return a new [[Bson]] document obtained by applying the specified
     *         [[BsonSpecification]] to this [[Bson]] document.
     * @note this method creates a new [[Bson]] document, leaving this
     *       [[Bson]] document unchanged.
     */
    def update(specification: BsonSpecification ?=> Unit): Bson =
      val copy: Bson = self.clone()
      copy.putAll(bson {
        specification
      })
      copy
  }

  extension (key: String) {

    /**
     * Bind this key to the specified value within the given
     * [[BsonSpecification]].
     *
     * @param value         the specified value.
     * @param specification the given [[BsonSpecification]].
     */
    @targetName("bindToValue")
    infix def ::(value: BsonValue)(using specification: BsonSpecification): Unit =
      specification.define(key, value)
  }

  /**
   * Parse the specified [[BsonSpecification]] producing a [[Bson]] document.
   *
   * @param specification the specified [[BsonSpecification]].
   * @return the [[Bson]] document described by the specified [[BsonSpecification]].
   */
  private def parse(specification: BsonSpecification): Bson =
    BsonDocument(specification.definitions.map(entry => BsonElement(entry._1, entry._2)).asJava)
