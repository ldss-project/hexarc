package io.github.jahrim.hexarc.logging

import org.slf4j.helpers.NOPLogger
import org.slf4j.Logger as Slf4jLogger

/** Utility for [[Slf4jLogger Slf4jLogger]]s. */
object Loggers:
  /** Default logger. */
  private val Default: Slf4jLogger = LoggerFactory.fromName("DefaultLogger", false)

  /** Placeholder no-operation logger. */
  private val NoOperationLogger: Slf4jLogger = NOPLogger.NOP_LOGGER

  /** @return the default logger. */
  def log: Slf4jLogger = Loggers.Default

  /** @return a placeholder no-operation logger. */
  def nop: Slf4jLogger = Loggers.NoOperationLogger
