package io.github.jahrim.hexarc.architecture.vertx.core.components

import io.vertx.core.Vertx

/** The initialization context of a [[VertxService]]. */
trait ServiceContext extends ServiceComponentContext

/** Companion object of [[ServiceContext]]. */
object ServiceContext:
  /**
   * @param name  the name of the [[VertxService]].
   * @param vertx the [[Vertx]] instance where the [[VertxService]] is running.
   * @return a new initialization context of a [[VertxService]].
   */
  def apply(name: String)(vertx: Vertx): ServiceContext = BasicServiceContext(name, vertx)

  /** Basic implementation of a [[ServiceContext]]. */
  private case class BasicServiceContext(
      override val name: String,
      override val vertx: Vertx
  ) extends ServiceContext
