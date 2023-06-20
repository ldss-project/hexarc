package io.github.jahrim.hexarc.vertx.dsl

import io.github.jahrim.hexarc.vertx.components.*
import io.vertx.core.Verticle

/** A collection of the contexts of the [[VertxDSL]]. */
object VertxDSLContexts:
  /** A supplier of [[PortContext]]s. */
  private type LazyPortContext = ServiceContext => PortContext

  /** A supplier of [[AdapterContext]]s. */
  private type LazyAdapterContext = PortContext => AdapterContext[Port]

  /** The context where a [[VertxService]] is configured. */
  class ServiceBuildingContext:
    private[VertxDSLContexts] var _name: String = ""
    private[VertxDSLContexts] var _portsAndAdapters: Map[Port, Seq[Adapter[Port]]] = Map.empty
    private[VertxDSLContexts] var _portContexts: Map[Port, LazyPortContext] = Map.empty
    private[VertxDSLContexts] var _adapterContexts: Map[Adapter[Port], LazyAdapterContext] =
      Map.empty

    /**
     * Set the name of this [[VertxService]] to the specified name.
     *
     * @param name the specified name.
     * @return this.
     */
    def name(name: String): ServiceBuildingContext =
      this._name = name
      this

    /**
     * Add the specified [[Port]] to this [[VertxService]].
     *
     * @param name  the name of the specified [[Port]].
     * @param model the implementation of the specified [[Port]].
     * @tparam P the type of the specified [[Port]].
     * @return a context for configuring the specified [[Port]].
     */
    def port[P <: Port](name: String, model: P): PortBuildingContext[P] =
      if !this._portsAndAdapters.contains(model) then
        this._portsAndAdapters += model -> Seq()
        this._portContexts += model -> PortContext(name)
      PortBuildingContext(model, this)

    /**
     * Close this context.
     *
     * @return the [[VertxService]] configured within this context.
     */
    def close: Verticle = VertxService(this._name, this.serviceConfiguration).toVerticle

    /** The configuration process of the [[VertxService]] configured within this context. */
    private def serviceConfiguration: ServiceContext => Unit = serviceContext =>
      this._portsAndAdapters.foreach { (port, adapters) =>
        val portContext: PortContext = this._portContexts(port)(serviceContext)
        port.start(portContext)
        adapters.foreach { adapter =>
          adapter.start(this._adapterContexts(adapter)(portContext))
        }
      }

  /**
   * The context where a [[Port]] of a [[VertxService]] is configured.
   *
   * @param port           the implementation of this [[Port]].
   * @param serviceContext the context of the [[VertxService]] who owns this [[Port]].
   * @tparam P the type of this [[Port]].
   */
  class PortBuildingContext[P <: Port](port: P, serviceContext: ServiceBuildingContext):
    /**
     * Add the specified [[Adapter]] to this [[Port]].
     *
     * @param name    the name of the specified [[Adapter]].
     * @param adapter the implementation of the specified [[Adapter]].
     * @return this.
     */
    def adapter(name: String, adapter: Adapter[P]): PortBuildingContext[P] =
      serviceContext._portsAndAdapters.get(port).foreach { adapters =>
        val erasedAdapter: Adapter[Port] = adapter.asInstanceOf[Adapter[Port]]
        serviceContext._portsAndAdapters += port -> (adapters :+ erasedAdapter)
        serviceContext._adapterContexts += erasedAdapter -> AdapterContext(name, port)
      }
      this

    /**
     * Close this context.
     *
     * @return the context of the [[VertxService]] who owns this [[Port]].
     */
    def close: ServiceBuildingContext = serviceContext
