package io.github.jahrim.hexarc.vertx.dsl

import io.vertx.core.{Vertx, Future as VertxFuture}

import scala.util.Try
import scala.concurrent.{Await, Promise, Future as ScalaFuture}
import scala.concurrent.duration.{Duration, SECONDS}

/** A collection of the extensions provided by the [[VertxDSL]]. */
object VertxDSLExtensions:

  extension [T](self: VertxFuture[T]) {

    /** @return this [[VertxFuture VertxFuture]] as a [[ScalaFuture ScalaFuture]]. */
    def toScalaFuture: ScalaFuture[T] =
      val promise: Promise[T] = Promise()
      self.onComplete {
        case x if x.succeeded() => promise.success(x.result())
        case x                  => promise.failure(x.cause())
      }
      promise.future

    /**
     * Await for the result of this [[VertxFuture VertxFuture]], for at most the specified duration.
     *
     * @param timeout the specified duration.
     * @return a [[Try]] containing the result of this [[VertxFuture VertxFuture]] when succeeded,
     *         or the exception thrown when failed.
     */
    def await(timeout: Duration): Try[T] =
      Try { Await.result(self.toScalaFuture, timeout) }

    /** As [[await await(Duration(30, SECONDS))]]. */
    def await: Try[T] = await(Duration(30, SECONDS))

  }
