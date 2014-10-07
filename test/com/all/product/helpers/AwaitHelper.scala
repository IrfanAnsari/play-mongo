package com.all.product.helpers

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

trait AwaitHelper {
  def await[T](future: Future[T]) = Await.result(future, Duration.create(10, TimeUnit.SECONDS))

}
