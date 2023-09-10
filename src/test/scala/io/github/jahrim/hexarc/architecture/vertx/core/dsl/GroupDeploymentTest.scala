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

import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment.DeploymentMap
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples.NamedService.*
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples.IdProviderService.*

/** A test for deploying groups of services using the [[VertxDSL]]. */
class GroupDeploymentTest extends VertxDSLTest:
  private var deployments: DeploymentMap[String] = Map()

  private val idProviderServiceName = "IdProviderService"
  private def idProviderService: DeploymentGroup ?=> Service =
    new Service:
      name = idProviderServiceName

      new Port[IdProviderPort]:
        model = IdProviderModel()

        new Adapter(IdProviderLocalAdapter())

  private val namedServiceName = "NamedService"
  private def namedService: DeploymentGroup ?=> Service =
    new Service:
      name = namedServiceName

      new Port[NamedPort]:
        model = NamedModel()

        new Adapter(NamedLocalAdapter())

  before {
    val deploymentGroup =
      new DeploymentGroup(this.vertx):
        idProviderService
        namedService
        namedService
      end new

    deployments = deploymentGroup.deploy().await.get
  }

  describe("A deployment group") {
    describe("when deployed") {
      it("should create a deployment map containing the deployments of the deployed services") {
        assert(
          deployments.map(entry => entry._1 -> entry._2.size) ==
            Map(idProviderServiceName -> 1, namedServiceName -> 2)
        )
      }
      it("should distribute the requests among different replicas of the same service") {
        Iterator
          .iterate(Set.empty[String]) { _ + strictRequest[String]("namedService/name", None) }
          .dropWhile(serviceNames => serviceNames.size <= 1)
          .nextOption()
          .getOrElse { fail() }
      }
    }
  }

  after {
    deployments.flatMap(_._2).map(_.undeploy()).toSeq.unflatten.await.get
  }
