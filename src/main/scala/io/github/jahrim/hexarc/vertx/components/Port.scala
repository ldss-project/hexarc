package io.github.jahrim.hexarc.vertx.components

/**
 * A port in the Hexagonal Architecture.
 *
 * A [[Port]] is a perspective on the business logic of a [[VertxService]]
 * and it should describe a set of correlated use cases for that service.
 */
@FunctionalInterface
trait Port extends ServiceComponent[PortContext]
