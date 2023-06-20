package io.github.jahrim.hexarc.vertx.components

/**
 * An adapter in the Hexagonal Architecture.
 *
 * An [[Adapter]] should enable a specific technology for communicating with a
 * specific [[Port]] of a [[VertxService]].
 *
 * @tparam P the type of [[Port]] to which this [[Adapter]] is attached.
 */
@FunctionalInterface
trait Adapter[P <: Port] extends ServiceComponent[AdapterContext[P]]
