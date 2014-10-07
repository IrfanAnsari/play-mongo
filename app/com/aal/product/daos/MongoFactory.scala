package com.aal.product.daos

import reactivemongo.api.{MongoDriver, DefaultDB}
import play.modules.reactivemongo.json.collection.JSONCollection
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global


trait MongoConnector {
  def getDb(connection: MongoConnection): DefaultDB
  def getCollection(connection: MongoConnection, collectionName: String): JSONCollection
}

class SingleMongoConnector extends MongoConnector {
  override def getDb(connection: MongoConnection): DefaultDB = MongoFactory.getDb(connection)

  override def getCollection(connection: MongoConnection, collectionName: String): JSONCollection = MongoFactory.getCollection(connection, collectionName)
}


object MongoFactory{

  private lazy val driver = new MongoDriver()

  def getDb(connection: MongoConnection): DefaultDB = {
    driver.connection(List(s"${connection.host}:${connection.port}"))(connection.dbName)
  }

  def getCollection(connection: MongoConnection, collectionName: String): JSONCollection = {
    getDb(connection).collection[JSONCollection](collectionName)
  }

}
