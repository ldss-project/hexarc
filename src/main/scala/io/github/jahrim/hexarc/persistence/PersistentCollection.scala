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
package io.github.jahrim.hexarc.persistence

import scala.util.{Failure, Success, Try}

/**
 * A collection of documents whose content persist over different executions
 * of the same application.
 * The access to the collection is granted by the standard CRUD operations.
 *
 * @note when implementing this trait, a [[QueryLanguage]] should be provided
 *       as a mixin attached to the implementing class.
 */
trait PersistentCollection:
  self: QueryLanguage =>

  /**
   * Create new documents in this [[PersistentCollection]] as for
   * the specified query.
   *
   * @param query the specified query.
   * @return a [[Success]] containing the result of the query
   *         if the operation succeeded; a [[Failure]] otherwise.
   */
  def create(query: CreateQuery): Try[CreateQueryResult]

  /**
   * Read documents from this [[PersistentCollection]] as for the
   * specified query.
   *
   * @param query the specified query.
   * @return a [[Success]] containing the result of the query
   *         if the operation succeeded; a [[Failure]] otherwise.
   */
  def read(query: ReadQuery): Try[ReadQueryResult]

  /**
   * Update documents in this [[PersistentCollection]] as for
   * the specified query.
   *
   * @param query the specified query.
   * @return a [[Success]] containing the result of the query
   *         if the operation succeeded; a [[Failure]] otherwise.
   */
  def update(query: UpdateQuery): Try[UpdateQueryResult]

  /**
   * Delete documents in this [[PersistentCollection]] as for
   * the specified query.
   *
   * @param query the specified query.
   * @return a [[Success]] containing the result of the query
   *         if the operation succeeded; a [[Failure]] otherwise.
   */
  def delete(query: DeleteQuery): Try[DeleteQueryResult]
