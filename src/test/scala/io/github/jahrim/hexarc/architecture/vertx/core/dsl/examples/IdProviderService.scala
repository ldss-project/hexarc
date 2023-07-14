package io.github.jahrim.hexarc.architecture.vertx.core.dsl.examples

import io.github.jahrim.hexarc.architecture.vertx.core.components.*
import io.vertx.core.Future

/** An example of Hexagonal Architecture for an id provider. */
object IdProviderService:

  /** A provider of unique identifiers. */
  trait IdProviderPort extends Port:

    /**
     * @return a [[Future]] containing a new unique identifier generated
     *         by this provider.
     */
    def newId: Future[String]

  /** Business logic of an [[IdProviderPort]]. */
  class IdProviderModel extends IdProviderPort:
    private var _idCount: Int = -1

    override def init(context: PortContext): Unit =
      this._idCount = -1

    override def newId: Future[String] =
      Future.succeededFuture {
        context.log.info(s"Generating new id...")
        this._idCount += 1
        context.log.info(s"Generated new id {${this._idCount}}.")
        this._idCount.toString
      }

  /** An [[Adapter]] that enables local communication with an [[IdProviderPort IdProviderPort]]. */
  class IdProviderLocalAdapter extends Adapter[IdProviderPort]:
    override protected def init(context: AdapterContext[IdProviderPort]): Unit =
      context.vertx.eventBus().localConsumer("idProvider/newId").handler { message =>
        context.log.info("Receiving newId request...")
        context.api.newId.onSuccess(newId => message.reply(newId))
        context.log.info("Accepted newId request.")
      }
