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

import com.mongodb.client.model.InsertManyOptions
import org.bson.conversions.Bson

/** A query for creating the specified documents in [[https://www.mongodb.com/ MongoDB]]. */
case class CreateQuery private (documents: Seq[Bson], options: InsertManyOptions)

/** Companion object of [[CreateQuery]]. */
object CreateQuery:
  /**
   * @param document the specified document.
   * @param options  additional creation options.
   * @return a [[CreateQuery]] for creating the specified document with the
   *         specified creation options.
   */
  def apply(document: Bson, options: InsertManyOptions = InsertManyOptions()): CreateQuery =
    new CreateQuery(Seq(document), options)

  /**
   * @param documents the specified documents.
   * @param options   additional creation options.
   * @return a [[CreateQuery]] for creating the specified documents with the
   *         specified creation options.
   */
  def many(documents: Seq[Bson], options: InsertManyOptions = InsertManyOptions()): CreateQuery =
    new CreateQuery(documents, options)
