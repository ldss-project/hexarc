package io.github.jahrim.hexarc.persistence.bson.codec.standard

import io.github.jahrim.hexarc.persistence.bson.codec.{BsonDecoder, BsonEncoder}
import org.bson.{BsonArray, BsonValue}

import scala.jdk.CollectionConverters.{ListHasAsScala, SeqHasAsJava}

/** [[BsonValue]] codec for [[Seq]]. */
object SeqCodec:
  /** A given [[BsonDecoder]] for untyped [[Seq]]. */
  given bsonToSeq: BsonDecoder[Seq[BsonValue]] = bson => bson.asArray.getValues.asScala.toSeq

  /** A given [[BsonEncoder]] for untyped [[Seq]]. */
  given seqToBson: BsonEncoder[Seq[BsonValue]] = seq => BsonArray(seq.asJava)

  /** A given [[BsonDecoder]] for typed [[Seq]]. */
  given bsonToGenericSeq[T](using conversion: BsonDecoder[T]): BsonDecoder[Seq[T]] =
    bson => bsonToSeq(bson).map(conversion)

  /** A given [[BsonEncoder]] for typed [[Seq]]. */
  given genericSeqToBson[T](using conversion: BsonEncoder[T]): BsonEncoder[Seq[T]] =
    seq => seqToBson(seq.map(conversion))
