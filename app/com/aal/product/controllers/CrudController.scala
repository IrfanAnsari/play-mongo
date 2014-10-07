package com.aal.product.controllers


import play.api.libs.json.{Format, Json}
import play.api.mvc._
import com.aal.product.models.Model

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.aal.product.services.{ProductService, CrudService}


abstract class CrudController[M <: Model : Format](val crudService: CrudService[M]) extends Controller {

  def create[M] = Action.async {
    request =>
      withDocument(request) {
        document => crudService.create(document).map {
          doc => Created.withHeaders("Location" -> com.aal.product.controllers.routes.ProductController.get(doc).url) // Look how we can add the relative path to resource
        }
      }
  }

  def get[M](id: String) = Action.async {
    crudService.read(id).map {
      case Some(model) => Ok(Json.toJson(model))
      case None => NotFound(s"product with $id was not found")
    }
  }

  def update[M](id: String) = Action.async {
    request =>
      withDocument(request) {
        document =>
          crudService.update(document).map {
            doc =>
              NoContent
          }
      }
  }


  def delete[M](id: String) = Action.async {
    crudService.delete(id).map {
      result => NoContent
    }
  }


  def listAll[M](filters: (String, Any)*) = Action.async {
    crudService.listAll(filters: _*).map {
      models =>
        Ok(Json.toJson(models.toList))
    }
  }


  private def withDocument(request: Request[AnyContent])(f: M => Future[Result]): Future[Result] = {
    val json = request.body.asJson
    json match {
      case Some(j) => f(Json.fromJson[M](j).get)
      case None => Future.successful(BadRequest(s"Illegal Json : $json"))
    }
  }

}

class ProductController(override val crudService: ProductService) extends CrudController(crudService)
