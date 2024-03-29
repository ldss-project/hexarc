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

import io.github.jahrim.hexarc.logging.LoggerFactory
import io.github.jahrim.hexarc.architecture.vertx.core.*
import components.{Port, PortContext, AdapterContext}
import io.vertx.core.Verticle
import DSLContextExceptions.*

import scala.reflect.{ClassTag, classTag}

/**
 * A [[DSLContext.Child]] for declaring a [[Port]] for a [[Verticle]] service configured
 * within a [[ServiceDSLContext]].
 */
class PortDSLContext[P <: Port: ClassTag](using override protected val parent: ServiceDSLContext)
    extends DSLContext.Child[ServiceDSLContext]
    with NamedDSLContext:
  /**
   * Shorthand for declaring an [[components.Adapter Adapter]] within the [[Port]]
   * configured by this specific [[PortDSLContext]].
   */
  protected type Adapter = AdapterDSLContext[P]
  private var _model: Option[P] = None
  private var _adapters: Seq[Adapter] = Seq()

  name = LoggerFactory.nameOf(classTag[P].runtimeClass, false)
  this.parent.addPort(this)

  /**
   * Close this [[PortDSLContext]], initializing a [[Port]] with the declared configuration
   * within the [[Verticle]] service declared by the parent [[ServiceDSLContext]].
   *
   * @throws MissingFieldException if no implementation for the [[Port]] has been provided.
   */
  private[vertx] def close(portContext: PortContext): Unit =
    this._adapters.foreach { adapter =>
      val adapterFullName = s"${portContext.service.name}-${this.name}-${adapter.name}"
      adapter.close(AdapterContext(adapterFullName, this.model)(portContext))
    }

  /**
   * Add the [[components.Adapter Adapter]] declared by the specified [[AdapterDSLContext]]
   * to the [[components.Adapter Adapter]]s of the [[Port]] configured within this
   * [[PortDSLContext]].
   *
   * @param adapter the specified [[AdapterDSLContext]].
   * @return this.
   */
  private[vertx] def addAdapter(adapter: Adapter): PortDSLContext[P] =
    this._adapters = this._adapters :+ adapter
    this

  /**
   * Set the implementation of the [[Port]] configured within this [[PortDSLContext]]
   * to the specified implementation.
   *
   * @param model the specified implementation.
   */
  protected def model_=(model: P): Unit =
    this._model = Some(model)

  /**
   * @return the implementation of the [[Port]] configured within this [[PortDSLContext]].
   * @throws MissingFieldException if no implementation for the [[Port]] has been provided yet.
   */
  def model: P = this._model.getOrElse { throw MissingFieldException("model") }
