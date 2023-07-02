package io.github.jahrim.hexarc.vertx.core.components

import io.github.jahrim.hexarc.logging.Logging
import io.vertx.core.{Verticle, AbstractVerticle, Promise, Vertx}

/**
 * A service in the Hexagonal Architecture.
 *
 * A [[VertxService]] manages the initialization of its [[ServiceComponent]]s, that is:
 *  - [[Port]]s : the possible perspectives on the business logic of the [[VertxService]].
 *  - [[Adapter]]s : the technologies enabled for interacting with the [[Port]]s of the [[VertxService]].
 *
 * @param name the name of the [[VertxService]].
 * @param onStart the configuration process for the [[ServiceComponent]]s of this [[VertxService]].
 * @param onStop the shutdown process for the [[ServiceComponent]]s of this [[VertxService]].
 */
class VertxService(
    private val name: String,
    private val onStart: ServiceContext => Unit = _ => {},
    private val onStop: ServiceContext => Unit = _ => {}
) extends ServiceComponent[ServiceContext]:

  /** @return this [[VertxService]] as a [[Verticle]]. */
  def toVerticle: Verticle = new AbstractVerticle {
    private val service: VertxService = VertxService.this

    override def start(startPromise: Promise[Void]): Unit =
      service.start(ServiceContext(service.name)(this.getVertx))
      startPromise.complete()

    override def stop(stopPromise: Promise[Void]): Unit =
      service.context.log.info("Stopping...")
      service.onStop(service.context)
      service.context.log.info("Stopped.")
      stopPromise.complete()
  }

  override protected def init(context: ServiceContext): Unit = onStart(context)
