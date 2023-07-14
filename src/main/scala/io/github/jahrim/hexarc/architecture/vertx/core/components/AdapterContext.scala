package io.github.jahrim.hexarc.architecture.vertx.core.components

/**
 * The initialization context of an [[Adapter Adapter]].
 *
 * @tparam P the type of [[Port]] to which the [[Adapter]] is attached.
 */
trait AdapterContext[P <: Port] extends ServiceComponentContext:
  /** @return the context of the [[Port]] this [[Adapter]] is attached to. */
  def port: PortContext

  /** @return the implementation of the [[Port]] this [[Adapter]] is attached to. */
  def api: P

/** Companion object of [[AdapterContext]]. */
object AdapterContext:
  /**
   * @param name the name of the [[Adapter]].
   * @param api  the [[Port]] to which the [[Adapter]] is attached.
   * @param port the context of the [[Port]] to which the [[Adapter]] is attached.
   * @tparam P the type of [[Port]] to which the [[Adapter]] is attached.
   * @return a new initialization context for an [[Adapter]].
   */
  def apply[P <: Port](name: String, api: P)(port: PortContext): AdapterContext[P] =
    BasicAdapterContext(name, api, port)

  /** Basic implementation of [[AdapterContext]]. */
  private case class BasicAdapterContext[P <: Port](
      override val name: String,
      override val api: P,
      override val port: PortContext
  ) extends AdapterContext[P]:
    export port.vertx
