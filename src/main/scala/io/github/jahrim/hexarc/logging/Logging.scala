package io.github.jahrim.hexarc.logging

import org.slf4j.Logger as Slf4jLogger

/**
 * Mixin that enables a named [[Slf4jLogger Slf4jLogger]] within the child class.
 *
 * There are three variants of this mixin:
 *  - [[Logging.Canonical]]: the logger is named after the canonical name of the child class.
 *  - [[Logging.Simple]]: the logger is named after the simple name of the child class.
 *  - [[Logging.Named]]: the logger is named after a custom name specified by the user.
 *
 * Each object of the child class owns a uniquely named [[Slf4jLogger Slf4jLogger]].
 */
trait Logging:
  /** @return the logger for this object. */
  protected def logger: Slf4jLogger

  /** Alias of [[logger]]. */
  protected def log: Slf4jLogger = this.logger

/** Companion object of [[Logging]]. */
object Logging:
  /**
   * See [[Logging]].
   *
   * @param name the name of the logger.
   */
  trait Named(private val name: String) extends Logging:
    protected override val logger: Slf4jLogger = LoggerFactory.fromName(name)

  /** See [[Logging]]. */
  trait Canonical extends Logging:
    protected override val logger: Slf4jLogger = LoggerFactory.fromObject(this)

  /** See [[Logging]]. */
  trait Simple extends Logging:
    protected override val logger: Slf4jLogger = LoggerFactory.fromObject(this, canonical = false)
