package io.github.jahrim.hexarc.persistence

/** A language for querying [[PersistentCollection]]s. */
trait QueryLanguage:
  /** A query for creating documents. */
  type CreateQuery

  /** A query for reading documents. */
  type ReadQuery

  /** A query for updating documents. */
  type UpdateQuery

  /** A query for deleting documents. */
  type DeleteQuery

  /** The result of a [[CreateQuery]]. */
  type CreateQueryResult

  /** The result of a [[ReadQuery]]. */
  type ReadQueryResult

  /** The result of a [[UpdateQuery]]. */
  type UpdateQueryResult

  /** The result of a [[DeleteQuery]]. */
  type DeleteQueryResult
