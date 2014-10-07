package com.all.product.integration


import play.api.libs.json.{Json, Format}
import com.all.product.helpers.AwaitHelper
import com.aal.product.daos.{SingleMongoConnector, MongoConnection, MongoConnector, MongoDao}


class MongoDaoSpec extends EmbeddedMongoSpec with AwaitHelper {

  val fakeDocument = FakeDocument(1, "Fake Document")

  val mongoDao = new MongoDao[FakeDocument] {
    override val connector: MongoConnector = new SingleMongoConnector
    override val connection: MongoConnection = MongoConnection("localhost", port, "test")
    override val collectionName: String = "test"
    override implicit val formatter: Format[FakeDocument] = Json.format[FakeDocument]
  }

  "Mongo Dao" should {
    "create and then read a fake document" in {
      val id = await(mongoDao.create(fakeDocument))
      val result = await(mongoDao.read(id)).get

      result.anInt should equal(fakeDocument.anInt)
      result.aString should equal(fakeDocument.aString)
      result._id should equal(Some(id))
    }


    "read an empty collection" in {
      await(mongoDao.listAll()) should equal(Seq())
    }

    "list multiple documents in collection" in {
      val documents = Seq(FakeDocument(1, "document1"), FakeDocument(2, "document2"))
      val expectedDocuments = documents.map(document => FakeDocument(document.anInt, document.aString, Some(await(mongoDao.create(document)))))
      await(mongoDao.listAll()) should equal(expectedDocuments)
    }

    "update a document" in {
      val id = await(mongoDao.create(fakeDocument))
      val changedDocument = FakeDocument(2, "changedDocument", Some(id))

      await(mongoDao.update(changedDocument)) should equal(true)
      await(mongoDao.read(id)).get should equal(changedDocument)

    }

    "delete a document" in {
      val id = await(mongoDao.create(fakeDocument))

      await(mongoDao.delete(id)) should equal(true)
      await(mongoDao.read(id)) should be(None)

    }

    "check for an existence of a document" in {
      val id = await(mongoDao.create(fakeDocument))
      await(mongoDao.exists(id)) should be(true)

    }

    "check for a non Existence of a document" in {
      val nonExistentId = "non_existent_id"
      await(mongoDao.exists(nonExistentId)) should be(false)

    }
  }
}
