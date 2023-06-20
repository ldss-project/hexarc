package io.github.jahrim.hexarc.logging

import org.slf4j.{Logger as Slf4jLogger, LoggerFactory as Slf4jLoggerFactory}

/** A factory for [[Slf4jLogger Slf4jLogger]]s. */
object LoggerFactory:
  /** The number of loggers that has been created. */
  private var LoggerCount: Int = 0

  /**
   * @param name   the specified name.
   * @param unique true if the name should be made unique, false otherwise.
   * @return a new [[Loggers Slf4jLogger]] with the specified name.
   */
  def fromName(name: String, unique: Boolean = true): Slf4jLogger =
    Slf4jLoggerFactory.getLogger(if unique then createUniqueName(name) else name)

  /**
   * @param clazz     the specified class.
   * @param canonical true if the canonical name of the class should used; false
   *                  if the simple name of the class should be used.
   * @param unique    true if the name should be made unique, false otherwise.
   * @return a new [[Loggers Slf4jLogger]] with the name of the specified class.
   */
  def fromClass(clazz: Class[_], canonical: Boolean = true, unique: Boolean = true): Slf4jLogger =
    fromName(nameOf(clazz, canonical), unique)

  /**
   * @param instance  the specified object.
   * @param canonical true if the canonical name of the class should used; false
   *                  if the simple name of the class should be used.
   * @param unique    true if the name should be made unique, false otherwise.
   * @return a new [[Loggers Slf4jLogger]] with the name of the class of the specified object.
   */
  def fromObject(instance: Any, canonical: Boolean = true, unique: Boolean = true): Slf4jLogger =
    fromClass(instance.getClass, canonical, unique)

  /**
   * @param clazz     the specified class.
   * @param canonical true if the canonical name of the class should returned; false
   *                  if the simple name of the class should be returned.
   * @return the logger name of the specified class.
   */
  def nameOf(clazz: Class[_], canonical: Boolean = true): String =
    Option(if canonical then clazz.getCanonicalName else clazz.getSimpleName)
      .filter(_.nonEmpty)
      .getOrElse { anonymousNameOf(clazz) }

  /**
   * @param clazz the specified anonymous class.
   * @return the logger name of the specified anonymous class.
   */
  private def anonymousNameOf(clazz: Class[_]): String =
    s"Anonymous${clazz.getInterfaces.lastOption.map(_.getSimpleName).getOrElse("Class")}"

  /**
   * @param name the specified name.
   * @return a new unique name derived from the specified name.
   */
  private def createUniqueName(name: String): String =
    LoggerFactory.LoggerCount = LoggerFactory.LoggerCount + 1
    s"{#${LoggerFactory.LoggerCount}} $name"
