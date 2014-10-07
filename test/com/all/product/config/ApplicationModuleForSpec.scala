package com.all.product.config

import com.softwaremill.macwire.Macwire
import com.aal.product.daos.{ProductDao, SingleMongoConnector, MongoConnection}
import com.aal.product.services.ProductService
import com.aal.product.controllers.ProductController

trait ApplicationModuleForSpec extends Macwire {
  lazy val mongoConnection = new MongoConnection("localhost", 12345, "example_test")
  lazy val connector = wire[SingleMongoConnector]
  lazy val productDao = wire[ProductDao]
  lazy val productService= wire[ProductService]
  lazy val productController = wire[ProductController]

}
