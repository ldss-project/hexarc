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
package io.github.jahrim.hexarc.architecture.vertx.core.dsl.contexts

/** Exceptions for [[DSLContext]]s. */
object DSLContextExceptions:
  /**
   * A general [[Exception]] signalling incomplete configuration inside a [[DSLContext]].
   * @param message the message of the [[Exception]].
   */
  class MissingConfigurationException(message: String)
      extends IllegalStateException(
        s"Missing configuration: $message"
      )

  /**
   * A [[MissingConfigurationException MissingConfigurationException]] thrown when a
   * [[DSLContext]] is closed before providing all of its required configuration fields.
   * @param fieldName the name of the missing field.
   */
  class MissingFieldException(fieldName: String)
      extends MissingConfigurationException(
        s"configuration field <$fieldName> must be provided before closing this context."
      )
