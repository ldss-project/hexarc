package io.github.jahrim.hexarc.persistence.bson.codecs.standard

import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDecoder, BsonEncoder}
import org.bson.{BsonArray, BsonValue}

import scala.jdk.CollectionConverters.{ListHasAsScala, SeqHasAsJava}

/** [[BsonValue]] codec for [[Seq]]. */
object SeqCodec:
  /**
   * A given [[BsonDecoder]] for [[Seq]].
   * @throws BsonInvalidOperationException if the [[BsonValue]] is not a [[BsonArray]].
   */
  given seqDecoder[T](using decoder: BsonDecoder[T]): BsonDecoder[Seq[T]] =
    bson => bson.asArray.getValues.asScala.toSeq.map(decoder.decode)

  /** A given [[BsonEncoder]] for [[Seq]]. */
  given seqEncoder[T](using encoder: BsonEncoder[T]): BsonEncoder[Seq[T]] =
    seq => BsonArray(seq.map(encoder.encode).asJava)
