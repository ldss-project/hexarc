package io.github.jahrim.hexarc.persistence

import scala.util.{Failure, Success, Try}

/**
 * A collection of documents whose content persist over different executions
 * of the same application.
 * The access to the collection is granted by the standard CRUD operations.
 *
 * @tparam Document a type defining what is a document in this collection.
 * @tparam Filter a type defining how to select documents in this collection.
 * @tparam Projection a type defining how to select fields from the documents in this collection.
 * @tparam Update a type defining how to update documents in this collection.
 * @tparam CreateOptions a type defining what are the additional options for creating documents.
 * @tparam ReadOptions a type defining what are the additional options for reading documents.
 * @tparam UpdateOptions a type defining what are the additional options for updating documents.
 * @tparam DeleteOptions a type defining what are the additional options for deleting documents.
 */
trait PersistentCollection[
    Document,
    Filter,
    Projection,
    Update,
    CreateOptions,
    ReadOptions,
    UpdateOptions,
    DeleteOptions
]:
  /**
   * Create the specified document within this collection.
   *
   * @param document the specified document.
   * @param options  additional creation options specific to the
   *                 implementation of this collection.
   * @return a [[Success]] if the operation succeeded; a [[Failure]]
   *         otherwise.
   */
  def create(document: Document, options: CreateOptions): Try[Unit]

  /**
   * Retrieve the documents selected by the specified filter from
   * this collection. For those documents, only the fields selected
   * by the specified projection are retrieved.
   *
   * @param filter     the specified filter.
   * @param projection the specified projection.
   * @param options    additional retrieval options specific to the
   *                   implementation of this collection.
   * @return a [[Success]] containing the documents selected by
   *         the specified filter document if the operation succeeded;
   *         a [[Failure]] otherwise.
   */
  def read(filter: Filter, projection: Projection, options: ReadOptions): Try[Seq[Document]]

  /**
   * Update the documents selected by the specified filter with the
   * specified update.
   *
   * @param filter  the specified filter.
   * @param update  the specified update.
   * @param options additional update options specific to the
   *                implementation of this collection.
   * @return a [[Success]] if the operation succeeded; a [[Failure]]
   *         otherwise.
   */
  def update(filter: Filter, update: Update, options: UpdateOptions): Try[Unit]

  /**
   * Delete the documents selected by the specified filter from this
   * collection.
   *
   * @param filter  the specified filter.
   * @param options additional deletion options specific to the
   *                implementation of this collection.
   * @return a [[Success]] if the operation succeeded; a [[Failure]]
   *         otherwise.
   */
  def delete(filter: Filter, options: DeleteOptions): Try[Unit]

  /**
   * As [[create]] but creates many documents.
   *
   * @param documents the specified documents to create.
   * @param options   additional creation options specific to the
   *                  implementation of this collection.
   * @return a [[Success]] if all the documents were created successfully;
   *         a [[Failure]] otherwise.
   */
  def createMany(documents: Seq[Document], options: CreateOptions): Try[Unit] =
    Try { documents.map(create(_, options).get) }
