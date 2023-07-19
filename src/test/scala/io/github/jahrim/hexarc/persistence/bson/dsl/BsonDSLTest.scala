package io.github.jahrim.hexarc.persistence.bson.dsl

import test.AbstractTest
import io.github.jahrim.hexarc.persistence.bson.codecs.{BsonDocumentDecoder, BsonDocumentEncoder}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSL.{*, given}
import io.github.jahrim.hexarc.persistence.bson.dsl.BsonDSLTest.{*, given}
import org.bson.*

import java.time.{Instant, ZonedDateTime}

/** Template test. */
class BsonDSLTest extends AbstractTest:
  val boolean: Boolean = true
  val string: String = "string"
  val (int, int0, int1, int2): (Int, Int, Int, Int) = (1, 10, 20, 30)
  val long: Long = 10_000_000_000_000L
  val double: Double = 0.33
  val christmas: Instant = ZonedDateTime.parse("2023-12-25T12:00:00Z").toInstant
  val typedSequence: Seq[Int] = Seq(int0, int1, int2)
  val nestedObject: CustomObject = CustomObject(int, long, string)

  val booleanUpdate: Boolean = false
  val intUpdate: Int = 40
  val typedSequenceUpdate: Seq[Int] = typedSequence.drop(1)
  val objectUpdate: CustomObject = CustomObject(int, int, int.toString)

  val testBson: BsonDocument = bson {
    "booleanField" :: boolean
    "stringField" :: string
    "intField" :: int
    "longField" :: long
    "doubleField" :: double
    "dateField" :: christmas
    "typedSequenceField" :: typedSequence
    "objectField" :: nestedObject
  }

  extension [T](self: Option[T]) { def getOrFail: T = self.getOrElse(fail()) }

  before {}

  describe("An empty bson") {
    it("should have no fields") {
      assert(emptyBson.entrySet.size == 0)
    }
    it("should have an empty json representation") {
      assert(emptyBson.toJson == "{}")
    }
  }

  describe("A bson created using a specification") {
    it("should contain the same fields as the corresponding bson created without a specification") {
      val bsonWithoutSpecification: BsonDocument =
        BsonDocument()
          .append("booleanField", BsonBoolean(boolean))
          .append("stringField", BsonString(string))
          .append("intField", BsonInt32(int))
          .append("longField", BsonInt64(long))
          .append("doubleField", BsonDouble(double))
          .append("dateField", BsonDateTime(christmas.toEpochMilli))
          .append(
            "typedSequenceField",
            BsonArray(
              java.util.Arrays.asList(
                BsonInt32(int0),
                BsonInt32(int1),
                BsonInt32(int2)
              )
            )
          )
          .append(
            "objectField",
            BsonDocument()
              .append("subfield1", BsonInt32(int))
              .append("subfield2", BsonInt64(long))
              .append("subfield3", BsonString(string))
          )

      val bsonWithSpecification: BsonDocument = bson {
        "booleanField" :: boolean
        "stringField" :: string
        "intField" :: int
        "longField" :: long
        "doubleField" :: double
        "dateField" :: christmas
        "typedSequenceField" :* (int0, int1, int2)
        "objectField" :# {
          "subfield1" :: int
          "subfield2" :: long
          "subfield3" :: string
        }
      }
      assert(testBson == bsonWithoutSpecification)
      assert(testBson == bsonWithSpecification)
    }

    describe("when considering basic types") {
      it("should contain the basic fields defined within the specification") {
        assert(testBson("booleanField").getOrFail.as[Boolean] == boolean)
        assert(testBson("stringField").getOrFail.as[String] == string)
        assert(testBson("intField").getOrFail.as[Int] == int)
        assert(testBson("longField").getOrFail.as[Long] == long)
        assert(testBson("doubleField").getOrFail.as[Double] == double)
        assert(testBson("dateField").getOrFail.as[Instant] == christmas)
      }
      it("should not contain non-existing fields") {
        assert(testBson("nonExistingField").isEmpty)
      }
      it("should not contain non-existing indexes") {
        assert(testBson("0").isEmpty)
      }
    }

    describe("when considering arrays") {
      it("should contain the array fields defined within the specification") {
        assert(testBson("typedSequenceField").getOrFail.as[Seq[Int]] == typedSequence)
      }
      it("should contain the array indexes defined within the specification") {
        assert(testBson("typedSequenceField.0").getOrFail.as[Int] == typedSequence(0))
        assert(testBson("typedSequenceField.1").getOrFail.as[Int] == typedSequence(1))
        assert(testBson("typedSequenceField.2").getOrFail.as[Int] == typedSequence(2))
      }
      it("should not contain non-existing indexes") {
        assert(testBson("typedSequenceField.3").isEmpty)
      }
    }

    describe("when considering objects") {
      it("should contain the object fields defined within the specification") {
        assert(testBson("objectField").getOrFail.as[CustomObject] == nestedObject)
      }
      it("should contain the object sub-fields defined within the specification") {
        assert(testBson("objectField.subfield1").getOrFail.as[Int] == int)
        assert(testBson("objectField.subfield2").getOrFail.as[Long] == long)
        assert(testBson("objectField.subfield3").getOrFail.as[String] == string)
      }
      it("should not contain non existing sub-fields") {
        assert(testBson("objectField.nonExistingField").isEmpty)
      }
    }

    describe("when updated through another specification") {
      val updatedBson: BsonDocument = testBson.update {
        "newField" :: objectUpdate
        "booleanField" :: booleanUpdate
        "typedSequence" :: typedSequenceUpdate
        "objectField" :# { "subfield1" :: long }
      }

      it("should reflect the updates defined within such specification") {
        assert(updatedBson("newField").getOrFail.as[CustomObject] == objectUpdate)
        assert(updatedBson("booleanField").getOrFail.as[Boolean] == booleanUpdate)

        assert(updatedBson("typedSequence").getOrFail.as[Seq[Int]] == typedSequenceUpdate)
        assert(updatedBson("typedSequence.0").getOrFail.as[Int] == typedSequenceUpdate(0))
        assert(updatedBson("typedSequence.1").getOrFail.as[Int] == typedSequenceUpdate(1))
        assert(updatedBson("typedSequence.2").isEmpty)

        assert(updatedBson("objectField.subfield1").getOrFail.as[Long] == long)
        assert(updatedBson("objectField.subfield2").isEmpty)
        assert(updatedBson("objectField.subfield3").isEmpty)
      }

      it("should keep the other fields unchanged") {
        assert(testBson("stringField").getOrFail.as[String] == string)
        assert(testBson("intField").getOrFail.as[Int] == int)
        assert(testBson("longField").getOrFail.as[Long] == long)
        assert(testBson("doubleField").getOrFail.as[Double] == double)
        assert(testBson("dateField").getOrFail.as[Instant] == christmas)
      }
    }

    describe("when requiring a field") {
      it("should provide such field if present") {
        assert(testBson.require("booleanField").as[Boolean] == boolean)
      }
      it("should throw a NoSuchElementException if not present") {
        assertThrows[NoSuchElementException] {
          testBson.require("nonExistingField")
        }
      }
    }
  }

  after {}

/** Companion object of [[BsonDSLTest]]. */
object BsonDSLTest:
  /** An example custom object. */
  case class CustomObject(subfield1: Int, subfield2: Long, subfield3: String)

  /** [[BsonDocumentEncoder]] for [[CustomObject]]s. */
  given BsonDocumentEncoder[CustomObject] = obj =>
    bson {
      "subfield1" :: obj.subfield1
      "subfield2" :: obj.subfield2
      "subfield3" :: obj.subfield3
    }

  /** [[BsonDocumentDecoder]] for [[CustomObject]]s. */
  given BsonDocumentDecoder[CustomObject] = bson =>
    CustomObject(
      bson.require("subfield1").as[Int],
      bson.require("subfield2").as[Long],
      bson.require("subfield3").as[String]
    )
