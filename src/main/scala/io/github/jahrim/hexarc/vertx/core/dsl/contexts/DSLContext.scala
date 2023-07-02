package io.github.jahrim.hexarc.vertx.core.dsl.contexts

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
