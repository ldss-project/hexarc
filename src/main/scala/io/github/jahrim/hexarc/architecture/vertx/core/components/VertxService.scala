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
package io.github.jahrim.hexarc.architecture.vertx.core.components

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
