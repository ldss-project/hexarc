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
package io.github.jahrim.hexarc.architecture.vertx.core.dsl.contexts

import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment
import io.github.jahrim.hexarc.architecture.vertx.core.dsl.Deployment.DeploymentMap
import io.vertx.core.{Future, Verticle, Vertx}

/**
 * A [[DSLContext.Root]] for deploying a group of [[Verticle]] services
 * on a [[Vertx]] instance.
 *
 * @param vertx the [[Vertx]] instance used to deploy the [[Verticle]] services.
 */
class DeploymentGroupDSLContext(vertx: Vertx = Vertx.vertx()) extends DSLContext.Root:
  /**
   * Shorthand for declaring a [[Verticle]] service within the deployment configured
   * by this specific [[DeploymentGroupDSLContext]].
   */
  protected type Service = ServiceDSLContext

  private var _services: Seq[Service] = Seq()

  /**
   * Deploy all the [[Verticle]] services declared within this [[DeploymentGroupDSLContext]].
   *
   * @return a [[Future]] containing the [[DeploymentMap DeploymentMap]] of the
   *         [[Verticle]] services deployed by this [[DeploymentGroupDSLContext]].
   *         The [[Future]] completes when all of the [[Deployment]]s have been completed.
   */
  def deploy(): Future[DeploymentMap[String]] = close

  /**
   * Close this [[DeploymentGroupDSLContext]], deploying all of the declared [[Verticle]] services.
   *
   * @return a [[Future]] containing the [[DeploymentMap DeploymentMap]] of the
   *         [[Verticle]] services deployed by this [[DeploymentGroupDSLContext]].
   *         The [[Future]] completes when all of the [[Deployment]]s have been completed.
   */
  private[vertx] def close: Future[DeploymentMap[String]] =
    Deployment.deployGroups(vertx)(this._services.groupMap(_.name)(_.close))

  /**
   * Add the [[Verticle]] service declared by the specified [[ServiceDSLContext]] to the group
   * of [[Verticle]] services to be deployed when this [[DeploymentGroupDSLContext]] will be closed.
   *
   * @param service the specified [[ServiceDSLContext]].
   * @return this.
   */
  private[vertx] def addService(service: Service): DeploymentGroupDSLContext =
    this._services = this._services :+ service
    this

/** Companion object of [[DeploymentGroupDSLContext]]. */
object DeploymentGroupDSLContext:
  /**
   * Deploy the [[Verticle]] service configured within the specified [[ServiceDSLContext]].
   *
   * @return a [[Future]] containing the [[Deployment]] of the [[Verticle]] service configured
   *         within the specified [[ServiceDSLContext]].
   *         The [[Future]] completes when the [[Deployment]] has been completed.
   */
  def deploySingle(vertx: Vertx = Vertx.vertx())(
      service: DeploymentGroupDSLContext ?=> ServiceDSLContext
  ): Future[Deployment] =
    new DeploymentGroupDSLContext(vertx) { service }
      .deploy()
      .map[Deployment](_.values.head.head)
