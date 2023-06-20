package io.github.jahrim.hexarc.vertx.dsl

import io.github.jahrim.hexarc.vertx.components.*
import io.github.jahrim.hexarc.vertx.dsl.VertxDSL.*
import io.vertx.core.{Future, Vertx, Verticle}

/** Example of an Hexagonal Architecture for a simple lamp. */
object LampService:

  /**
   * @param serviceName the specified name.
   * @return a new [[LampService]] with the specified name.
   */
  def apply(serviceName: String = "MyLampService"): Verticle =
    service(serviceName) {
      port(LampModel()) {
        adapter(LampHttpAdapter())
        adapter(LampMqttAdapter())
      }
    }

  /**
   * Deploy a new [[LampService]] with the specified name on the specified [[Vertx]].
   *
   * @param serviceName the specified name.
   * @param vertx       the specified vertx.
   * @return a future containing the deployment of the newly created [[LampService]],
   *         that completes when the deployment has completed.
   */
  def deployOn(vertx: Vertx, serviceName: String = "MyLampService"): Future[Deployment] =
    deploy(vertx) { LampService(serviceName) }

  /** A lamp that can be switched on or off. */
  trait LampPort extends Port:
    /**
     * Switch this lamp to the specified state.
     * @param on switch on this lamp if true; switch off this lamp if false.
     * @return a future that completes when the lamp has been switched to the specified state.
     */
    def switch(on: Boolean): Future[Unit]

    /** @return a future that is true if the lamp is on, false otherwise. */
    def isOn: Future[Boolean]

  /** Business logic of a [[LampPort]]. */
  class LampModel extends LampPort:
    private var _on: Boolean = false

    override def init(context: PortContext): Unit =
      this._on = false

    override def switch(on: Boolean): Future[Unit] =
      Future.succeededFuture {
        context.log.info(s"Turning ${stateToString(on)}...")
        this._on = on
        context.log.info(s"Turned ${stateToString(on)}.")
      }
    override def isOn: Future[Boolean] =
      Future.succeededFuture {
        context.log.info(s"Reading state... ${stateToString()}")
        this._on
      }

    private def stateToString(state: Boolean = this._on): String = if state then "on" else "off"

  /** Fake http adapter for a [[LampPort]]. */
  class LampHttpAdapter extends Adapter[LampPort]:
    override def init(context: AdapterContext[LampPort]): Unit =
      /* Fake http configuration. Replace with an actual http server configuration. */
      def route(path: String) = context.vertx.eventBus().localConsumer(path)
      route("http/switchOn").handler { message =>
        context.log.info("Receiving switchOn request...")
        context.api.switch(on = true).onSuccess(_ => message.reply("200: OK"))
        context.log.info("Accepted switchOn request.")
      }
      route("http/switchOff").handler { message =>
        context.log.info("Receiving switchOff request...")
        context.api.switch(on = false).onSuccess(_ => message.reply("200: OK"))
        context.log.info("Accepted switchOff request.")
      }
      route("http/isOn").handler { message =>
        context.log.info("Receiving isOn request...")
        context.api.isOn.onSuccess(on => message.reply(on))
        context.log.info("Accepted isOn request.")
      }

  /** Fake mqtt adapter for a [[LampPort]]. */
  class LampMqttAdapter extends Adapter[LampPort]:
    override def init(context: AdapterContext[LampPort]): Unit =
      /* Fake mqtt configuration. Replace with an actual mqtt server configuration. */
      def route(path: String) = context.vertx.eventBus().localConsumer(path)
      route("mqtt/switchOn").handler { message =>
        context.log.info("Receiving switchOn request...")
        context.api.switch(on = true).onSuccess(_ => message.reply("OK_TOPIC"))
        context.log.info("Accepted switchOn request.")
      }
      route("mqtt/switchOff").handler { message =>
        context.log.info("Receiving switchOff request...")
        context.api.switch(on = false).onSuccess(_ => message.reply("OK_TOPIC"))
        context.log.info("Accepted switchOff request.")
      }
      route("mqtt/isOn").handler { message =>
        context.log.info("Receiving isOn request...")
        context.api.isOn.onSuccess(on => message.reply(on))
        context.log.info("Accepted isOn request.")
      }
