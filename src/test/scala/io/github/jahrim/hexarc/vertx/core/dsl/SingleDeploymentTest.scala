package io.github.jahrim.hexarc.vertx.core.dsl

import io.github.jahrim.hexarc.vertx.core.dsl.examples.ColoredLampService.*
import io.github.jahrim.hexarc.vertx.core.dsl.Deployment

/** A test for deploying a single service using the [[VertxDSL]]. */
class SingleDeploymentTest extends VertxDSLTest:
  private var deployment: Option[Deployment] = None

  before {
    deployment = Some(
      DeploymentGroup
        .deploySingle(this.vertx) {
          new Service:
            name = "ColoredLampService"

            private val lampModel = ColoredLampModel()

            new Port[LampSwitchPort]:
              name = "SwitchPort"
              model = lampModel

              new Adapter(LampSwitchHttpAdapter()):
                name = "Http"
              new Adapter(LampSwitchMqttAdapter()):
                name = "Mqtt"

            new Port[LampColorPort]:
              name = "ColorPort"
              model = lampModel

              new Adapter(LampColorHttpAdapter()):
                name = "Http"
              new Adapter(LampColorMqttAdapter()):
                name = "Mqtt"
        }
        .await
        .get
    )
  }

  describe("A lamp") {
    describe("when initialized") {
      it("should be off") {
        assert(!strictRequest[Boolean]("lamp/http/get/isOn", None))
      }
      it("should be white") {
        assert(strictRequest[String]("lamp/http/get/color", None) == "white")
      }
    }
    describe("when switched on") {
      it("should be on") {
        strictRequest("lamp/http/post/switchOn", None)
        assert(strictRequest[Boolean]("lamp/mqtt/publish/isOn", None))
      }
    }
    describe("when changing color to blue") {
      it("should be blue") {
        strictRequest("lamp/http/post/color", Some("blue"))
        assert(strictRequest[String]("lamp/mqtt/publish/getColor", None) == "blue")
      }
    }
  }

  after {
    deployment.foreach(_.undeploy().await.get)
  }
