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

import io.github.jahrim.hexarc.architecture.vertx.core.*
import components.{PortContext, ServiceContext, VertxService}
import io.github.jahrim.hexarc.logging.LoggerFactory
import io.vertx.core.Verticle

/**
 * A [[DSLContext.Child]] for declaring a [[Verticle]] service to be deployed
 * within a [[DeploymentGroupDSLContext]].
 */
class ServiceDSLContext(using override protected val parent: DeploymentGroupDSLContext)
    extends DSLContext.Child[DeploymentGroupDSLContext]
    with NamedDSLContext:
  /**
   * Shorthand for declaring a [[components.Port Port]] within the [[Verticle]] service
   * configured by this specific [[ServiceDSLContext]].
   *
   * @tparam P the type of [[components.Port Port]].
   */
  protected type Port[P <: components.Port] = PortDSLContext[P]
  private var _ports: Seq[Port[?]] = Seq()

  name = "VertxService"
  this.parent.addService(this)

  /**
   * Close this [[ServiceDSLContext]], creating a [[Verticle]] service with the declared
   * configuration.
   *
   * @return the [[Verticle]] service configured within this [[ServiceDSLContext]].
   */
  private[vertx] def close: Verticle =
    VertxService(this.name, this.serviceConfiguration).toVerticle

  /**
   * Add the [[components.Port Port]] declared by the specified [[PortDSLContext]] to the
   * [[components.Port Port]]s of the [[Verticle]] service configured within this
   * [[ServiceDSLContext]].
   *
   * @param port the specified [[PortDSLContext]].
   * @return this.
   */
  private[vertx] def addPort(port: Port[?]): ServiceDSLContext =
    this._ports = this._ports :+ port
    this

  /**
   * The configuration of the [[VertxService]] created when this [[ServiceDSLContext]]
   * is closed.
   */
  private def serviceConfiguration(serviceContext: ServiceContext): Unit =
    var portContexts: Map[Any, PortContext] = Map()
    this._ports.foreach { port =>
      val model = port.model
      if !portContexts.contains(model) then
        val modelFullName = s"${serviceContext.name}-${LoggerFactory.nameOf(model.getClass, false)}"
        portContexts += model -> PortContext(modelFullName)(serviceContext)
        model.start(portContexts(model))
      port.close(portContexts(model))
    }
