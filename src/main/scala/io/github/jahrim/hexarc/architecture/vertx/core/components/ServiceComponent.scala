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
package io.github.jahrim.hexarc.architecture.vertx.core.components

/**
 * A component of a [[VertxService]].
 *
 * @tparam C the type of context required for the initialization of this component.
 */
@FunctionalInterface
trait ServiceComponent[C <: ServiceComponentContext]:
  private var _context: Option[C] = None

  /**
   * Start this [[ServiceComponent]].
   *
   * @param context the context required for the initialization of this [[ServiceComponent]].
   * @note this method should be called by the [[VertxService]] which owns this [[ServiceComponent]].
   *
   *       When using the [[io.github.jahrim.hexarc.vertx.core.dsl.VertxDSL VertxDSL]], such
   *       configuration process will be handled automatically.
   */
  final def start(context: C): Unit =
    this._context = Some(context)
    context.log.info("Initializing...")
    this.init(context)
    context.log.info("Initialized.")

  /**
   * The context of this [[ServiceComponent]].
   *
   * @throws IllegalStateException if the component has not been initialized yet.
   *
   * @note this utility grants access to the context of this [[ServiceComponent]]
   *       outside of its [[init]] method, in order to avoid forwarding such context
   *       to all the methods of the [[ServiceComponent]].
   *
   *       However, the user should care of exploiting this utility only after this
   *       [[ServiceComponent]] has been initialized.
   */
  protected def context: C = this._context.getOrElse {
    throw new IllegalStateException(
      "Cannot access the context of this component before its initialization."
    )
  }

  /**
   * Initializes this [[ServiceComponent]].
   *
   * @param context the context required for the initialization of this [[ServiceComponent]].
   * @note this method is executed when this [[ServiceComponent]] is started, that is when
   *       the [[start]] method is called.
   *
   *       Subclasses defined by the user should override this method to configure the
   *       initialization of his specific [[ServiceComponent]]s.
   */
  protected def init(context: C): Unit
