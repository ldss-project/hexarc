package io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples

import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.vertx.core.Future

/** An example of Hexagonal Architecture for an entity with a unique name. */
object NamedService:

  /** An entity with a unique name. */
  trait NamedPort extends Port:

    /** @return a [[Future]] containing the unique name of this entity. */
    def name: Future[String]

  /** Business logic of a [[NamedPort]]. */
  class NamedModel extends NamedPort:
    private val _name: String = NamedModel.newUniqueName

    override def init(context: PortContext): Unit = {}

    override def name: Future[String] =
      Future.succeededFuture {
        context.log.info(s"Reading name... ${this._name}...")
        this._name
      }

  /** Companion object of [[NamedModel]]. */
  object NamedModel:
    private var IdCount: Int = -1

    /** @return a new unique name for a [[NamedModel]]. */
    private def newUniqueName: String =
      NamedModel.synchronized { IdCount += 1; IdCount.toString }

  /** An [[Adapter]] that enables local communication with a [[NamedPort]]. */
  class NamedLocalAdapter extends Adapter[NamedPort]:
    override protected def init(context: AdapterContext[NamedPort]): Unit =
      context.vertx.eventBus().localConsumer("namedService/name").handler { message =>
        context.log.info("Receiving name request...")
        context.api.name.onSuccess(name => message.reply(name))
        context.log.info("Accepted name request.")
      }
