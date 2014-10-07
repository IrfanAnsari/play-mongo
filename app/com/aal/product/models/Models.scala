package com.aal.product.models

import play.api.libs.json.Json


abstract class Model {
  def _id: Option[String]
}

case class Product(_id: Option[String], name: String, description: String) extends Model

object Product {
  implicit val productFormat = Json.format[Product]
}
