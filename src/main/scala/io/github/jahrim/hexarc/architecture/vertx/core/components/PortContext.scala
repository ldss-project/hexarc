package io.github.jahrim.hexarc.architecture.vertx.core.components

/** The initialization context of a [[Port]]. */
trait PortContext extends ServiceComponentContext:
  /** @return the context of the [[VertxService]] described by this [[Port]]. */
  def service: ServiceContext

/** Companion object of [[PortContext]]. */
object PortContext:
  /**
   * @param name    the name of the [[Port]].
   * @param service the context of the [[VertxService]] described by the [[Port]].
   * @return a new initialization context for a [[Port]].
   */
  def apply(name: String)(service: ServiceContext): PortContext =
    BasicPortContext(name, service)

  /** Basic implementation of a [[PortContext]]. */
  private case class BasicPortContext(
      override val name: String,
      override val service: ServiceContext
  ) extends PortContext:
    export service.vertx
