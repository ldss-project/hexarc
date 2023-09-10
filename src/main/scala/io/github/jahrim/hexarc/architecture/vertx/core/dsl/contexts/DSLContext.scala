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

import DSLContext.*

/**
 * A context in a Domain Specific Language (DSL).
 *
 * There are two types of [[DSLContext]]s:
 *  - [[Root Root]]: a context that can be declared anywhere.
 *  - [[Child Child]]: a context that can only be declared within
 *    a parent context.
 */
trait DSLContext:
  /**
   * This [[DSLContext]] as a given.
   *
   * @note this given allows [[Child Child]] contexts to be created within a parent context,
   *       without having to explicitly declare such relationship.
   *       The relationship is implicitly inferred by the fact that the [[Child Child]] contexts
   *       have been created within that specific parent context, where this given is available.
   */
  protected given thisContext: this.type = this

/** Companion object of [[DSLContext]]. */
object DSLContext:
  /**
   * A root context.
   *
   * See [[DSLContext]] for more information.
   */
  trait Root extends DSLContext

  /**
   * A child context.
   *
   * See [[DSLContext]] for more information.
   *
   * @tparam P the type of the parent context.
   */
  trait Child[P <: DSLContext] extends DSLContext:
    /** @return the parent context of this [[Child Child]] context. */
    protected def parent: P
