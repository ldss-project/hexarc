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

/** The initialization context of a [[Port]]. */
trait PortContext extends ServiceComponentContext:
  /** @return the context of the [[VertxService]] described by this [[Port]]. */
  def service: ServiceContext

/** Companion object of [[PortContext]]. */
object PortContext:
  /**
   * @param name    the name of the [[Port]].
   * @param service the context of the [[VertxService]] described by the [[Port]].
   * @return a new initialization context for a [[Port]].
   */
  def apply(name: String)(service: ServiceContext): PortContext =
    BasicPortContext(name, service)

  /** Basic implementation of a [[PortContext]]. */
  private case class BasicPortContext(
      override val name: String,
      override val service: ServiceContext
  ) extends PortContext:
    export service.vertx
