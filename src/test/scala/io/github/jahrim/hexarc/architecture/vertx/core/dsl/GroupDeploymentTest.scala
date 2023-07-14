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
