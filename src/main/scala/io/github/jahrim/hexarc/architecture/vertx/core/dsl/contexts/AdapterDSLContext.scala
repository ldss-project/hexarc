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

import io.github.jahrim.hexarc.architecture.vertx.core.components.{Adapter, AdapterContext, Port}
import io.github.jahrim.hexarc.logging.LoggerFactory

/**
 * A [[DSLContext.Child]] for declaring an [[Adapter]] for a [[Port]] configured
 * within a [[PortDSLContext]].
 */
class AdapterDSLContext[P <: Port](adapter: Adapter[P])(using
    override protected val parent: PortDSLContext[P]
) extends DSLContext.Child[PortDSLContext[P]]
    with NamedDSLContext:
  name = LoggerFactory.nameOf(adapter.getClass, false)
  this.parent.addAdapter(this)

  /**
   * Close this [[AdapterDSLContext]], initializing an [[Adapter]] with the declared
   * configuration within the [[Port]] configured by the parent [[PortDSLContext]].
   */
  private[vertx] def close(adapterContext: AdapterContext[P]): Unit =
    adapter.start(adapterContext)
