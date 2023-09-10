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
package io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples

import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.vertx.core.Future

/** An example of Hexagonal Architecture for an id provider. */
object IdProviderService:

  /** A provider of unique identifiers. */
  trait IdProviderPort extends Port:

    /**
     * @return a [[Future]] containing a new unique identifier generated
     *         by this provider.
     */
    def newId: Future[String]

  /** Business logic of an [[IdProviderPort]]. */
  class IdProviderModel extends IdProviderPort:
    private var _idCount: Int = -1

    override def init(context: PortContext): Unit =
      this._idCount = -1

    override def newId: Future[String] =
      Future.succeededFuture {
        context.log.info(s"Generating new id...")
        this._idCount += 1
        context.log.info(s"Generated new id {${this._idCount}}.")
        this._idCount.toString
      }

  /** An [[Adapter]] that enables local communication with an [[IdProviderPort IdProviderPort]]. */
  class IdProviderLocalAdapter extends Adapter[IdProviderPort]:
    override protected def init(context: AdapterContext[IdProviderPort]): Unit =
      context.vertx.eventBus().localConsumer("idProvider/newId").handler { message =>
        context.log.info("Receiving newId request...")
        context.api.newId.onSuccess(newId => message.reply(newId))
        context.log.info("Accepted newId request.")
      }
