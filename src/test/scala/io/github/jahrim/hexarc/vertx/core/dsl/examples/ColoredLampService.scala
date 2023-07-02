package io.github.jahrim.hexarc.vertx.core.dsl.examples

import io.github.jahrim.hexarc.vertx.core.components.*
import io.vertx.core.Future

/** An example of Hexagonal Architecture for a colored lamp. */
object ColoredLampService:
  /** A lamp that can be switched on or off. */
  trait LampSwitchPort extends Port:
    /**
     * Switch this lamp to the specified state.
     * @param on switch on this lamp if true; switch off this lamp if false.
     * @return a [[Future]] that completes when this lamp has been switched to
     *         the specified state.
     */
    def switch(on: Boolean): Future[Unit]

    /** @return a [[Future]] that is true if this lamp is on, false otherwise. */
    def isOn: Future[Boolean]

  /** A lamp whose color can be changed. */
  trait LampColorPort extends Port:
    /**
     * Change the color of this lamp to the specified color.
     *
     * @param color the specified color.
     * @return a [[Future]] that completes when the color of this lamp has been changed
     *         to the specified color.
     */
    def changeColor(color: String): Future[Unit]

    /** @return a [[Future]] containing the color of this lamp. */
    def color: Future[String]

  /** Business logic of a [[LampSwitchPort LampSwitchPort]] and a [[LampColorPort LampColorPort]]. */
  class ColoredLampModel extends LampSwitchPort with LampColorPort:
    private var _on: Boolean = false
    private var _color: String = "white"

    override def init(context: PortContext): Unit =
      this._on = false
      this._color = "white"

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

    override def changeColor(color: String): Future[Unit] =
      Future.succeededFuture {
        context.log.info(s"Changing color to $color...")
        this._color = color
        context.log.info(s"Changed color to $color.")
      }
    override def color: Future[String] =
      Future.succeededFuture {
        context.log.info(s"Reading color... ${this._color}...")
        this._color
      }

    private def stateToString(state: Boolean = this._on): String = if state then "on" else "off"

  /** A fake http [[Adapter]] for a [[LampSwitchPort LampSwitchPort]]. */
  class LampSwitchHttpAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit =
      def route[T](path: String) = context.vertx.eventBus().localConsumer[T](path)
      route[Nothing]("lamp/http/post/switchOn").handler { message =>
        context.log.info("Receiving switchOn request...")
        context.api.switch(on = true).onSuccess(_ => message.reply("200: OK"))
        context.log.info("Accepted switchOn request.")
      }
      route[Nothing]("lamp/http/post/switchOff").handler { message =>
        context.log.info("Receiving switchOff request...")
        context.api.switch(on = false).onSuccess(_ => message.reply("200: OK"))
        context.log.info("Accepted switchOff request.")
      }
      route[Nothing]("lamp/http/get/isOn").handler { message =>
        context.log.info("Receiving isOn request...")
        context.api.isOn.onSuccess(on => message.reply(on))
        context.log.info("Accepted isOn request.")
      }

  /** A fake mqtt [[Adapter]] for a [[LampSwitchPort LampSwitchPort]]. */
  class LampSwitchMqttAdapter extends Adapter[LampSwitchPort]:
    override def init(context: AdapterContext[LampSwitchPort]): Unit =
      def route[T](path: String) = context.vertx.eventBus().localConsumer[T](path)
      route[Nothing]("lamp/mqtt/publish/switchOn").handler { message =>
        context.log.info("Receiving switchOn request...")
        context.api.switch(on = true).onSuccess(_ => message.reply("OK_TOPIC"))
        context.log.info("Accepted switchOn request.")
      }
      route[Nothing]("lamp/mqtt/publish/switchOff").handler { message =>
        context.log.info("Receiving switchOff request...")
        context.api.switch(on = false).onSuccess(_ => message.reply("OK_TOPIC"))
        context.log.info("Accepted switchOff request.")
      }
      route[Nothing]("lamp/mqtt/publish/isOn").handler { message =>
        context.log.info("Receiving isOn request...")
        context.api.isOn.onSuccess(on => message.reply(on))
        context.log.info("Accepted isOn request.")
      }

  /** A fake http [[Adapter]] for a [[LampColorPort LampColorPort]]. */
  class LampColorHttpAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit =
      def route[T](path: String) = context.vertx.eventBus().localConsumer[T](path)
      route[String]("lamp/http/post/color").handler { message =>
        context.log.info("Receiving setColor request...")
        context.api.changeColor(message.body).onSuccess(_ => message.reply("200: OK"))
        context.log.info("Accepted setColor request.")
      }
      route[Nothing]("lamp/http/get/color").handler { message =>
        context.log.info("Receiving getColor request...")
        context.api.color.onSuccess(color => message.reply(color))
        context.log.info("Accepted getColor request.")
      }

  /** A fake mqtt [[Adapter]] for a [[LampColorPort LampColorPort]]. */
  class LampColorMqttAdapter extends Adapter[LampColorPort]:
    override def init(context: AdapterContext[LampColorPort]): Unit =
      def route[T](path: String) = context.vertx.eventBus().localConsumer[T](path)
      route[String]("lamp/mqtt/publish/setColor").handler { message =>
        context.log.info("Receiving setColor request...")
        context.api.changeColor(message.body).onSuccess(_ => message.reply("OK_TOPIC"))
        context.log.info("Accepted setColor request.")
      }
      route[Nothing]("lamp/mqtt/publish/getColor").handler { message =>
        context.log.info("Receiving getColor request...")
        context.api.color.onSuccess(color => message.reply(color))
        context.log.info("Accepted getColor request.")
      }
