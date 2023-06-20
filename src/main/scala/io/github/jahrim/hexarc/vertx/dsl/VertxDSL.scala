package io.github.jahrim.hexarc.vertx.dsl

import io.github.jahrim.hexarc.logging.LoggerFactory
import io.vertx.core.{Future, Verticle, Vertx}
import io.github.jahrim.hexarc.vertx.components.{
  Adapter,
  Port,
  ServiceComponent,
  ServiceComponentContext
}
import VertxDSLContexts.*

/**
 * DSL for deploying services with [[Vertx]].
 *
 * @example Creating a new service:
 * {{{
 *   val myService =
 *     service("MyLampService"){
 *       port(MyLampModel()){
 *         adapter(MyLampHttpAdapter())
 *         adapter(MyLampMqttAdapter())
 *       }
 *     }
 * }}}
 * @example Deploying a new service:
 * {{{
 *   val vertx = Vertx.vertx()
 *   deploy(vertx){ myService }
 * }}}
 */
object VertxDSL:
  export VertxDSLExtensions.*

  /**
   * The entry point of the [[VertxDSL]] for deploying a [[Verticle]] service.
   *
   * @param vertx   the specified [[Vertx]] instance.
   * @param service the specified [[Verticle]].
   * @return a future containing the deployment of the specified [[Verticle]] service,
   *         that completes when the deployment has completed.
   */
  def deploy(vertx: Vertx)(service: Verticle): Future[Deployment] =
    Deployment.deploy(vertx)(service)

  /** As [[deploy deploy(Vertx.vertx())(service)]]. */
  def deploy(service: Verticle): Future[Deployment] = deploy(Vertx.vertx())(service)

  /**
   * The entry point of the [[VertxDSL]] for creating a [[Verticle]] service.
   *
   * @param name          the specified name.
   * @param configuration the specified configuration.
   * @return a new [[Verticle]] service with the specified name and configuration.
   */
  def service(name: String)(configuration: ServiceBuildingContext ?=> Unit): Verticle =
    val context: ServiceBuildingContext = ServiceBuildingContext().name(name)
    configuration(using context)
    context.close

  /**
   * Declare a new [[Port]] with the specified implementation and configuration
   * for the current [[Verticle]] service.
   *
   * @param model         the specified implementation.
   * @param configuration the specified configuration.
   * @param context       the given context of the current [[Verticle]] service.
   * @tparam P the type of this [[Port]].
   */
  def port[P <: Port](model: P)(configuration: PortBuildingContext[P] ?=> Unit)(using
      context: ServiceBuildingContext
  ): Unit =
    configuration(using context.port(nameOf(model), model))

  /**
   * Declare a new [[Adapter]] with the specified implementation for the current [[Port]]
   * of the [[Verticle]] service.
   *
   * @param adapter the specified implementation.
   * @param context the given context of the current [[Port]] of the [[Verticle]] service.
   * @tparam P the type of the [[Port]] this [[Adapter]] is attached to.
   */
  def adapter[P <: Port](adapter: Adapter[P])(using context: PortBuildingContext[P]): Unit =
    context.adapter(nameOf(adapter), adapter)

  /**
   * @param component the specified [[ServiceComponent]].
   * @return the name of the specified [[ServiceComponent]].
   */
  private def nameOf[C <: ServiceComponentContext](component: ServiceComponent[C]): String =
    LoggerFactory.nameOf(component.getClass, false)
