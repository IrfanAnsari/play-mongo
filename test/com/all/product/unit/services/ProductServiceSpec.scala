package com.all.product.unit.services


import org.scalatest.mock.MockitoSugar

import org.mockito.Mockito._

import scala.concurrent.Future
import org.scalatestplus.play.PlaySpec
import scala.concurrent.ExecutionContext.Implicits.global

import org.scalatest.BeforeAndAfter
import com.all.product.helpers.AwaitHelper
import com.aal.product.daos.ProductDao
import com.aal.product.models.Product
import com.aal.product.services.ProductService

class ProductServiceSpec extends PlaySpec with MockitoSugar with BeforeAndAfter with AwaitHelper {

  val mockedProductDao: ProductDao = mock[ProductDao]

  val EXPECTED_TV_PRODUCT_ID: String = "tv_id"
  val EXPECTED_WM_PRODUCT_ID: String = "wm_id"

  val tvProduct = Product(Some(EXPECTED_TV_PRODUCT_ID), "LG", "LG 4K TV")
  val wmProduct = Product(Some(EXPECTED_WM_PRODUCT_ID), "BOSCH", "Hot")

  val productService: ProductService = new ProductService(mockedProductDao)

  after {
    reset(mockedProductDao)
  }

  "ProductService should" should {

    "create a document" in {
      when(mockedProductDao.create(tvProduct)).thenReturn(Future(EXPECTED_TV_PRODUCT_ID))

      val productId: String = await(productService.create(tvProduct))

      productId mustBe EXPECTED_TV_PRODUCT_ID
      verify(mockedProductDao).create(tvProduct)
    }

    "update a document" in {
      when(mockedProductDao.update(tvProduct)).thenReturn(Future(true))

      val documentUpdated: Boolean = await(productService.update(tvProduct))

      documentUpdated mustBe true
      verify(mockedProductDao).update(tvProduct)
    }

    "delete a document" in {
      when(mockedProductDao.delete(EXPECTED_TV_PRODUCT_ID)).thenReturn(Future(true))

      val documentDeleted: Boolean = await(productService.delete(EXPECTED_TV_PRODUCT_ID))

      documentDeleted mustBe true
      verify(mockedProductDao).delete(EXPECTED_TV_PRODUCT_ID)
    }

    "read a document" in {
      when(mockedProductDao.read(EXPECTED_TV_PRODUCT_ID)).thenReturn(Future(Some(tvProduct)))

      val product: Option[Product] = await(productService.read(EXPECTED_TV_PRODUCT_ID))

      product mustBe Some(tvProduct)
      verify(mockedProductDao).read(EXPECTED_TV_PRODUCT_ID)
    }

    "list all document" in {
      when(mockedProductDao.listAll()).thenReturn(Future(Seq(tvProduct, wmProduct)))

      val products: Seq[Product] = await(productService.listAll())

      products mustBe Seq(tvProduct, wmProduct)
      verify(mockedProductDao).listAll()
    }

  }

}
