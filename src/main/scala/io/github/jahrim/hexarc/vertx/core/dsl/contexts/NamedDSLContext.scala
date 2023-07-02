package io.github.jahrim.hexarc.vertx.core.dsl.contexts

/** A [[DSLContext]] with a settable name. */
trait NamedDSLContext extends DSLContext:
  private var _name: String = ""

  /**
   * Set the name of this [[NamedDSLContext]] to the specified name.
   *
   * @param name the specified name.
   */
  protected def name_=(name: String): Unit = this._name = name

  /** @return the name of this [[NamedDSLContext]]. */
  def name: String = this._name
