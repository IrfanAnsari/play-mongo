package com.all.product.config

import com.softwaremill.macwire.{InstanceLookup, Macwire}

object GlobalForSpec extends play.api.GlobalSettings with Macwire {
  val instanceLookup = InstanceLookup(valsByClass(new ApplicationModuleForSpec {}))

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    instanceLookup.lookupSingleOrThrow(controllerClass)
  }
}
