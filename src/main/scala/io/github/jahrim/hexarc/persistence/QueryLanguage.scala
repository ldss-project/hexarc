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
