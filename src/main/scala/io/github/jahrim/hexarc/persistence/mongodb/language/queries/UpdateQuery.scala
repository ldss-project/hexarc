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
import com.mongodb.client.model.UpdateOptions
import org.bson.conversions.Bson

/** A query for updating documents in [[https://www.mongodb.com/ MongoDB]]. */
case class UpdateQuery private (filter: Bson, update: Bson, options: UpdateOptions)

/** Companion object of [[UpdateQuery]]. */
object UpdateQuery:
  /**
   * @param filter  selects which documents will be updated.
   * @param update  defines how the documents should be updated.
   * @param options additional update options.
   * @return a [[UpdateQuery]] for updating the documents selected by the specified
   *         filter with the specified update, considering the specified update options.
   */
  def apply(
      filter: Bson = emptyBson,
      update: Bson = emptyBson,
      options: UpdateOptions = UpdateOptions()
  ): UpdateQuery =
    new UpdateQuery(filter, update, options)
