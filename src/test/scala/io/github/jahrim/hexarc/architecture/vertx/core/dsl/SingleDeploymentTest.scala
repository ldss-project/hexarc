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

import io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples.ColoredLampService.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment

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
