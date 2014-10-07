package com.aal.product.daos

import play.api.libs.json.Format
import com.aal.product.models.Product

class ProductDao(val connection: MongoConnection, val connector: MongoConnector) extends MongoDao[Product] {
  override val collectionName: String = "example_products"
  override implicit val formatter: Format[Product] = Product.productFormat
}

