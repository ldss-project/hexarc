/*
MIT License

Copyright (c) 2023 Cesario Jahrim Gabriele

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
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
