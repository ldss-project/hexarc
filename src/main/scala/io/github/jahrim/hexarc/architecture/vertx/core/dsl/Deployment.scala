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

import io.vertx.core.{Future, Verticle, Vertx}
import VertxDSLExtensions.unflatten

/**
 * The deployment of a [[Verticle]].
 *
 * @param vertx the [[Vertx]] instance where the [[Verticle]] has been deployed.
 * @param deploymentId the identifier of the deployment, obtained when deploying a
 *                     [[Verticle]] in a [[Vertx]].
 */
case class Deployment(private val vertx: Vertx, private val deploymentId: String):
  /**
   * Undeploy the [[Verticle]] bound to this [[Deployment]].
   * @return a future that completes when the un-deployment has completed.
   */
  def undeploy(): Future[Void] = this.vertx.undeploy(this.deploymentId)

/** Companion object of [[Deployment]]. */
object Deployment:
  /**
   * A map from group identifiers to the [[Deployment]]s of the [[Verticle]] services belonging
   * to those groups.
   *
   * @tparam K the type of the group identifiers.
   */
  type DeploymentMap[K] = Map[K, Seq[Deployment]]

  /**
   * Deploy the specified [[Verticle]] service on the specified [[Vertx]] instance.
   *
   * @param vertx   the specified [[Vertx]] instance.
   * @param service the specified [[Verticle]] service.
   * @return a [[Future]] containing the [[Deployment]] of the specified [[Verticle]] service.
   *         The [[Future]] that completes when the [[Deployment]] has been completed.
   */
  def deploy(vertx: Vertx)(service: Verticle): Future[Deployment] =
    vertx.deployVerticle(service).map[Deployment](deploymentId => Deployment(vertx, deploymentId))

  /**
   * Deploy the specified [[Verticle]] services on the specified [[Vertx]] instance.
   *
   * @param vertx    the specified [[Vertx]] instance.
   * @param services the specified [[Verticle]] services.
   * @return a [[Future]] containing the [[Deployment]]s of the specified [[Verticle]] services.
   *         The [[Future]] completes when all of the [[Deployment]]s have been completed.
   */
  def deployAll(vertx: Vertx)(services: Verticle*): Future[Seq[Deployment]] =
    services.map(service => Deployment.deploy(vertx)(service)).unflatten

  /**
   * Deploy the specified groups of [[Verticle]] services on the specified [[Vertx]] instance.
   *
   * @param vertx  the specified [[Vertx]] instance.
   * @param groups a map from group identifiers to group of [[Verticle]] services.
   * @tparam K the type of the group identifiers.
   * @return a [[Future]] containing a map from group identifiers to the [[Deployment]]s of the
   *         corresponding group of [[Verticle]] services.
   *         The [[Future]] completes when all of the [[Deployment]]s have been completed.
   */
  def deployGroups[K](vertx: Vertx)(groups: Map[K, Seq[Verticle]]): Future[DeploymentMap[K]] =
    groups
      .map(entry => Deployment.deployAll(vertx)(entry._2*).map[(K, Seq[Deployment])](entry._1 -> _))
      .toSeq
      .unflatten
      .map[DeploymentMap[K]](Map.from(_))
