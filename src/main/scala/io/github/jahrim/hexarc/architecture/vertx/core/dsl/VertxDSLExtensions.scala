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
package io.github.jahrim.hexarc.architecture.vertx.core.dsl

import io.vertx.core.Future as VertxFuture

import scala.concurrent.duration.{Duration, SECONDS}
import scala.concurrent.{Await, Promise, Future as ScalaFuture}
import scala.jdk.CollectionConverters.{ListHasAsScala, SeqHasAsJava}
import scala.util.Try

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
     * Await for the result of this [[VertxFuture VertxFuture]], for
     * at most the specified duration.
     *
     * @param timeout the specified duration.
     * @return a [[Try]] containing the result of this [[VertxFuture VertxFuture]]
     *         when succeeded, or the exception thrown when failed.
     */
    def await(timeout: Duration): Try[T] =
      Try { Await.result(self.toScalaFuture, timeout) }

    /** As [[await await(Duration(30, SECONDS))]]. */
    def await: Try[T] = await(Duration(30, SECONDS))

  }

  extension [T](self: Seq[VertxFuture[T]]) {

    /**
     * Un-flatten this sequence of [[VertxFuture VertxFuture]]s into a
     * [[VertxFuture VertxFuture]] of a sequence containing their results.
     *
     * @return a single [[VertxFuture VertxFuture]] containing the results of the
     *         [[VertxFuture VertxFuture]]s in this sequence.
     *         The [[VertxFuture VertxFuture]] completes when all of the
     *         [[VertxFuture VertxFuture]]s in the sequence have been completed.
     */
    def unflatten: VertxFuture[Seq[T]] =
      VertxFuture.all(self.asJava).map(_.list[T]().asScala.toSeq)
  }
