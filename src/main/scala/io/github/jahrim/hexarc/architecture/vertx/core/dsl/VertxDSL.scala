package io.github.jahrim.hexarc.architecture.vertx.core.dsl

import io.github.jahrim.hexarc.architecture.vertx.core.components.VertxService
import io.github.jahrim.hexarc.architecture.vertx.core.components.VertxService
import io.vertx.core.Verticle

/**
 * A DSL for deploying [[Verticle]] services, following the best practices of
 * the Hexagonal Architecture.
 *
 * See [[VertxService]] for more information.
 *
 * @example Deploying a single service: {{{
 *    DeploymentGroup.deploySingle(Vertx.vertx()) {
 *      new Service:
 *        name = "LampService"
 *
 *        new Port[LampPort]:
 *          model = LampModel()
 *
 *          new Adapter(LampHttpAdapter())
 *          new Adapter(LampMqttAdapter())
 *    }
 * }}}
 */
object VertxDSL:
  export VertxDSLExtensions.*
  export Vocabulary.*

  /** The [[VertxDSL]] vocabulary. */
  private object Vocabulary:
    export io.github.jahrim.hexarc.architecture.vertx.core.dsl.contexts.{
      DeploymentGroupDSLContext as DeploymentGroup,
      ServiceDSLContext as Service,
      PortDSLContext as Port,
      AdapterDSLContext as Adapter
    }
