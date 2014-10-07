package com.all.product.integration
import com.aal.product.models.Model

case class FakeDocument(anInt: Int, aString: String, _id: Option[String] = None) extends Model
