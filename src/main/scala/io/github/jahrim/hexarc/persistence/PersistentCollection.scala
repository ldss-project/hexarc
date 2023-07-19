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
