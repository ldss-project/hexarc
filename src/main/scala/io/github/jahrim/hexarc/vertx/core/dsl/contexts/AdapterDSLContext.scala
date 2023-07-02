package io.github.jahrim.hexarc.vertx.core.dsl.contexts

import io.github.jahrim.hexarc.logging.LoggerFactory
import io.github.jahrim.hexarc.vertx.core.components.{Adapter, AdapterContext, Port}

/**
 * A [[DSLContext.Child]] for declaring an [[Adapter]] for a [[Port]] configured
 * within a [[PortDSLContext]].
 */
class AdapterDSLContext[P <: Port](adapter: Adapter[P])(using
    override protected val parent: PortDSLContext[P]
) extends DSLContext.Child[PortDSLContext[P]]
    with NamedDSLContext:
  name = LoggerFactory.nameOf(adapter.getClass, false)
  this.parent.addAdapter(this)

  /**
   * Close this [[AdapterDSLContext]], initializing an [[Adapter]] with the declared
   * configuration within the [[Port]] configured by the parent [[PortDSLContext]].
   */
  private[vertx] def close(adapterContext: AdapterContext[P]): Unit =
    adapter.start(adapterContext)
