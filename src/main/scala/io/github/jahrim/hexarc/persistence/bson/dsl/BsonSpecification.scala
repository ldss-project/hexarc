package io.github.jahrim.hexarc.persistence.bson.dsl

import org.bson.{BsonValue, BsonDocument}

/** A specification for defining [[BsonDocument]]s. */
private[dsl] case class BsonSpecification(
    private var _definitions: Seq[(String, BsonValue)] = Seq()
):
  /** @return the definitions within this [[BsonSpecification]]. */
  def definitions: Seq[(String, BsonValue)] = this._definitions

  /**
   * Add a new definition to this [[BsonSpecification]].
   *
   * @param key   the identifier of the definition.
   * @param value the value of the definition.
   */
  def define(key: String, value: BsonValue): Unit =
    this._definitions = this._definitions :+ (key -> value)
