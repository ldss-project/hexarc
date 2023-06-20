package io.github.jahrim.hexarc.vertx.components

import io.vertx.core.Vertx
import io.github.jahrim.hexarc.logging.{LoggerFactory, Logging}
import org.slf4j.Logger

/** The initialization context of a [[ServiceComponent]]. */
trait ServiceComponentContext extends Logging:
  protected override val logger: Logger = LoggerFactory.fromName(this.name)
  override def log: Logger = this.logger

  /** @return the name of the [[ServiceComponent]]. */
  def name: String

  /** @return the [[Vertx]] instance where the [[ServiceComponent]] is running. */
  def vertx: Vertx
