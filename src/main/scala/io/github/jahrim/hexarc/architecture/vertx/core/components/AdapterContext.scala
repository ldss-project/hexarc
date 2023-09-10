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
 * The initialization context of an [[Adapter Adapter]].
 *
 * @tparam P the type of [[Port]] to which the [[Adapter]] is attached.
 */
trait AdapterContext[P <: Port] extends ServiceComponentContext:
  /** @return the context of the [[Port]] this [[Adapter]] is attached to. */
  def port: PortContext

  /** @return the implementation of the [[Port]] this [[Adapter]] is attached to. */
  def api: P

/** Companion object of [[AdapterContext]]. */
object AdapterContext:
  /**
   * @param name the name of the [[Adapter]].
   * @param api  the [[Port]] to which the [[Adapter]] is attached.
   * @param port the context of the [[Port]] to which the [[Adapter]] is attached.
   * @tparam P the type of [[Port]] to which the [[Adapter]] is attached.
   * @return a new initialization context for an [[Adapter]].
   */
  def apply[P <: Port](name: String, api: P)(port: PortContext): AdapterContext[P] =
    BasicAdapterContext(name, api, port)

  /** Basic implementation of [[AdapterContext]]. */
  private case class BasicAdapterContext[P <: Port](
      override val name: String,
      override val api: P,
      override val port: PortContext
  ) extends AdapterContext[P]:
    export port.vertx
