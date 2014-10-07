package com.aal.product.services

import scala.concurrent.Future
import com.aal.product.daos.{ProductDao, CrudDao}
import com.aal.product.models.Model


abstract class CrudService[M <: Model](val crudDao: CrudDao[M]) {

  def create(model: M): Future[String] = {
    crudDao.create(model)
  }

  def read(id: String): Future[Option[M]]= {
    crudDao.read(id)
  }

  def update(model: M): Future[Boolean] = {
    crudDao.update(model)
  }

  def delete(id: String): Future[Boolean] = {
    crudDao.delete(id)
  }

  def listAll(filters: (String, Any)*): Future[Seq[M]] = {
    crudDao.listAll(filters: _*)
  }
}


class ProductService(override val crudDao: ProductDao) extends CrudService(crudDao)
