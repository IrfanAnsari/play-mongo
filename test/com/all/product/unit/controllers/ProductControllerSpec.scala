package com.all.product.unit.controllers

import org.mockito.Mockito._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json.Json
import org.scalatest.mock.MockitoSugar
import play.api.mvc.Results

import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._
import scala.Some
import play.api.mvc.Result
import com.aal.product.services.ProductService
import com.aal.product.models.Product
import com.aal.product.controllers.ProductController

class ProductControllerSpec extends PlaySpec with BeforeAndAfter with MockitoSugar with Results with OneAppPerSuite {

  val mockProductService = mock[ProductService]
  val tvProduct = Product(Some("tv_id"), "LG", "LG 4K TV")
  val wmProduct = Product(Some("wm_id"), "Bosch", "Bosch Washing machines")

  val productController: ProductController = new ProductController(mockProductService)

  after {
    reset(mockProductService)
  }

  "The product controller" should {

     "retrieve the resource for an existing id" in {
      //Given
      when(mockProductService.read(tvProduct._id.get)).thenReturn(Future(Some(tvProduct)))
      //When
      val response: Future[Result] = productController.get("tv_id").apply(FakeRequest())
      //Then
      status(response) mustEqual OK
      contentType(response) mustEqual Some("application/json")
      charset(response) mustEqual Some("utf-8")
      contentAsString(response) must equal(Json.toJson(tvProduct).toString())
    }

     "not retrieve the resource for non existent id" in {
      //Given
      when(mockProductService.read("non_existent_id")).thenReturn(Future(None))
      val id: String = "non_existent_id"
      //When
      val response: Future[Result] = productController.get(id).apply(FakeRequest())
      //Then
      status(response) mustEqual NOT_FOUND
      //contentType(response) mustEqual Some("application/json")
      charset(response) mustEqual Some("utf-8")
      contentAsString(response) must be("product with non_existent_id was not found")
      verify(mockProductService).read(id)
    }

    "create a resource" in {
      //Given
      val expectedProduct = Product(Some("test_id"), "test_name", "test_description")
      val jsonRequestBody = Json.toJson(Map("_id" -> "test_id", "name" -> "test_name", "description" -> "test_description"))
      when(mockProductService.create(expectedProduct)).thenReturn(Future("test_id"))
      val request = FakeRequest(POST, "/products").withJsonBody(jsonRequestBody)
      //When
      val response: Future[Result] = productController.create().apply(request)
      //Then
      status(response) mustEqual CREATED
      contentAsString(response) must be("")
      header("Location", response) must equal(Some("/products/test_id"))
      verify(mockProductService).create(expectedProduct)
    }

    "update a resource for a given id" in {
      //Given
      when(mockProductService.update(tvProduct)).thenReturn(Future(true))
      val request = FakeRequest(PUT, s"/products/${tvProduct._id.get}").withJsonBody(Json.toJson(tvProduct))
      //When
      val response = productController.update(tvProduct._id.get).apply(request)
      //Then
      status(response) mustEqual NO_CONTENT
      verify(mockProductService).update(tvProduct)
    }

    "delete a resource for a given id" in {
      //Given
      when(mockProductService.delete(tvProduct._id.get)).thenReturn(Future(true))
      val request = FakeRequest(DELETE, s"/products/${tvProduct._id.get}")
      //When
      val response = productController.delete(tvProduct._id.get).apply(request)
      //Then
      status(response) mustEqual NO_CONTENT
      verify(mockProductService).delete(tvProduct._id.get)
    }

    "list all the resources" in {
      //Given
      val products: Seq[Product] = Seq(tvProduct, wmProduct)
      when(mockProductService.listAll()).thenReturn(Future(products))
      val request = FakeRequest(GET, "/products")
      //When
      val response = productController.listAll().apply(request)
      //Then
      status(response) mustEqual OK
      contentAsJson(response) mustEqual Json.toJson(products.toList)
      verify(mockProductService).listAll()
    }
  }


}
