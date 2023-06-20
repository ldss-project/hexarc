package io.github.jahrim.hexarc.vertx.dsl

import io.vertx.core.{Vertx, Future as VertxFuture}
import io.github.jahrim.hexarc.vertx.dsl.LampService.*
import io.github.jahrim.hexarc.vertx.dsl.Deployment
import io.github.jahrim.hexarc.vertx.dsl.VertxDSL.*
import io.vertx.core.eventbus.Message

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import test.AbstractTest

/** A test for the [[VertxDSL]] of [[HexArc]]. */
class VertxDSLTest extends AbstractTest:
  val vertx: Vertx = Vertx.vertx()
  var deployment: Deployment = _

  private def request[M](path: String, message: Object): VertxFuture[Message[M]] =
    vertx.eventBus().request(path, message)

  before {
    deployment = LampService.deployOn(vertx).await.getOrElse(throw new Error("Deployment failed"))
  }

  describe("A lamp") {
    describe("when initialized") {
      it("should be off") {
        val isOn =
          request[Boolean]("http/isOn", null)
            .map(message => message.body)
            .await
            .getOrElse { fail() }
        assert(!isOn)
      }
    }
    describe("when switched on") {
      it("should be on") {
        request("http/switchOn", null).await
        val isOn =
          request[Boolean]("mqtt/isOn", null)
            .map(message => message.body)
            .await
            .getOrElse { fail() }
        assert(isOn)
      }
    }
  }

  after { deployment.undeploy().await }
