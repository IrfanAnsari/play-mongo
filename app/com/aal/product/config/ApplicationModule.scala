package com.aal.product.config

import com.softwaremill.macwire.Macwire
import com.aal.product.services.ProductService

import com.aal.product.controllers.ProductController
import com.aal.product.daos.{ProductDao, SingleMongoConnector, MongoConnection}

trait ApplicationModule extends Macwire{
  lazy val mongoConnection = new MongoConnection("localhost", 27017, "test")
  lazy val connector = wire[SingleMongoConnector]
  lazy val productDao = wire[ProductDao]
  lazy val productService= wire[ProductService]
  lazy val productController = wire[ProductController]

}
