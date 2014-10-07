package com.aal.product.daos


import play.api.libs.json.{JsObject, Json, JsValue, Format}
import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import reactivemongo.core.commands.LastError
import ExecutionContext.Implicits.global
import play.modules.reactivemongo.json.BSONFormats._
import com.aal.product.models.Model


trait MongoDao[M <: Model] extends CrudDao[M] {
  implicit val formatter: Format[M]

  val collectionName: String
  val connection: MongoConnection
  val connector: MongoConnector

  private lazy val collection = connector.getCollection(connection, collectionName)


  override def create(entity: M): Future[String] = {

    val id = BSONObjectID.generate.stringify

    val jsonEntityDocument: JsValue = Json.toJson(entity).as[JsObject]

    val jsonDocument = idMatcher(id).as[JsObject].deepMerge(jsonEntityDocument.as[JsObject])

    val insert: Future[LastError] = collection.insert(jsonDocument)

    insert.map[String] {
      case LastError(true, _, _, _, Some(doc), _, _) => {
        (jsonDocument \ "_id").as[String]
      }
      case _ => {
        throw new Exception("Error while creating the resource " + entity)
      }
    }
  }

  override def read(entityId: String): Future[Option[M]] = {
    collection.find(idMatcher(entityId)).one[M]
  }

  override def update(entity: M): Future[Boolean] = {
    if (None == entity._id)
      throw new MongoDAOException("Updates can only be performed on documents with an ID")
    val jsonDocument: JsValue = Json.toJson(entity).as[JsObject]
    collection.update(idMatcher(entity._id.get), jsonDocument).map(lastErrorToBoolean)
  }

  override def delete(entityId: String): Future[Boolean] = {
    collection.remove(idMatcher(entityId)).map(lastErrorToBoolean)
  }

  override def listAll(filters: (String, Any)*): Future[Seq[M]] = {
    collection.find(BSONDocument()).cursor[M].collect[Seq]()
  }

  override def exists(entityId: String): Future[Boolean] = {
    read(entityId).map(item => item match {
      case Some(model) => true
      case None => false
    })
  }

  private def idMatcher(entityId: String): JsObject = {
    Json.obj("_id" -> entityId)
  }


  private val lastErrorToBoolean: LastError => Boolean = {
    case LastError(true, _, _, _, Some(doc), _, _) => true
    case _ => false
  }


}
