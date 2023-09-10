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
package io.github.jahrim.hexarc.architecture.vertx.core.dsl

import test.AbstractTest
import io.vertx.core.{Vertx, Verticle}
import io.vertx.core.eventbus.EventBus
import scala.util.{Success, Failure, Try}

/** An abstract test for the [[VertxDSL]]. */
abstract class VertxDSLTest extends AbstractTest:
  export io.github.jahrim.hexarc.architecture.vertx.core.dsl.VertxDSL.*

  protected val vertx: Vertx = Vertx.vertx()

  /**
   * Send a synchronous request to the [[EventBus]] of the [[Verticle]] services
   * deployed within this test.
   *
   * @param address the address to which the request is sent.
   * @param message the payload of the request.
   * @tparam M the type of payload of the response.
   * @return a [[Success]] containing the payload of the response if the request was
   *         successful, a [[Failure]] otherwise.
   */
  protected def blockingRequest[M](address: String, message: Option[Object]): Try[M] =
    this.vertx
      .eventBus()
      .request[M](address, message.orNull)
      .map(message => message.body)
      .await

  /** As [[blockingRequest]] but makes this test fail if the request fails. */
  protected def strictRequest[M](address: String, message: Option[Object]): M =
    this.blockingRequest[M](address, message).getOrElse { fail() }
