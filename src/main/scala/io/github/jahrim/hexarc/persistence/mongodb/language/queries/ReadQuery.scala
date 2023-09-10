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
package io.github.jahrim.hexarc.persistence.mongodb.language.queries

import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import org.bson.conversions.Bson

/** A query for reading documents in [[https://www.mongodb.com/ MongoDB]]. */
case class ReadQuery private (aggregation: Bson*)

/** Companion object of [[ReadQuery]]. */
object ReadQuery:
  /**
   * @param filter     the specified filter.
   * @param projection the specified projection.
   * @return a new [[ReadQuery]] that read the fields selected by the
   *         specified projection of the documents selected by the specified
   *         filter.
   */
  def apply(filter: Bson = emptyBson, projection: Bson = emptyBson): ReadQuery =
    new ReadQuery(bson { "$match" :: filter }, bson { "$project" :: projection })

  /**
   * @param aggregation the aggregation pipeline for reading documents in
   *                    [[https://www.mongodb.com/ MongoDB]].
   * @return a new [[ReadQuery]] that read the fields as for the specified
   *         aggregation pipeline.
   */
  def pipeline(aggregation: Bson*): ReadQuery = new ReadQuery(aggregation*)
