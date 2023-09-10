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
package io.github.jahrim.hexarc.persistence.mongodb.language

import com.mongodb.client.result.{DeleteResult, InsertManyResult, UpdateResult}
import io.github.jahrim.hexarc.persistence.{PersistentCollection, QueryLanguage}
import org.bson.BsonDocument

/** The [[QueryLanguage]] of [[https://www.mongodb.com/ MongoDB]]. */
trait MongoDBQueryLanguage extends QueryLanguage:
  override type CreateQuery = queries.CreateQuery
  override type ReadQuery = queries.ReadQuery
  override type UpdateQuery = queries.UpdateQuery
  override type DeleteQuery = queries.DeleteQuery
  override type CreateQueryResult = InsertManyResult
  override type ReadQueryResult = Seq[BsonDocument]
  override type UpdateQueryResult = UpdateResult
  override type DeleteQueryResult = DeleteResult
