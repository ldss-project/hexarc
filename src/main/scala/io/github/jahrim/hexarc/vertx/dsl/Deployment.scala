package io.github.jahrim.hexarc.vertx.dsl

import io.vertx.core.{Vertx, Future, Verticle}

/**
 * The deployment of a [[Verticle]].
 *
 * @param vertx the [[Vertx]] instance where the [[Verticle]] has been deployed.
 * @param deploymentId the identifier of the deployment, obtained when deploying a
 *                     [[Verticle]] in a [[Vertx]].
 */
case class Deployment(private val vertx: Vertx, private val deploymentId: String):
  /**
   * Undeploy the [[Verticle]] bound to this deployment.
   * @return a future that completes when the un-deployment has completed.
   */
  def undeploy(): Future[Void] = this.vertx.undeploy(this.deploymentId)

/** Companion object of [[Deployment]]. */
object Deployment:
  /**
   * Deploy the specified [[Verticle]] service on the specified [[Vertx]] instance.
   *
   * @param vertx   the specified [[Vertx]] instance.
   * @param service the specified [[Verticle]].
   * @return a future containing the deployment of the specified [[Verticle]] service,
   *         that completes when the deployment has completed.
   */
  def deploy(vertx: Vertx)(service: Verticle): Future[Deployment] =
    vertx.deployVerticle(service).map[Deployment](deploymentId => Deployment(vertx, deploymentId))
