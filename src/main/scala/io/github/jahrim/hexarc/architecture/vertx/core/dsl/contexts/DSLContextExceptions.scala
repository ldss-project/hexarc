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
