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
