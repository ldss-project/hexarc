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
