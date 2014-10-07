package com.all.product.integration

import org.scalatest._
import java.util.logging.Logger
import de.flapdoodle.embed.mongo.{MongodExecutable, MongodStarter}
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.config.{RuntimeConfig, MongodConfig}
import com.github.simplyscala.MongodProps
import com.mongodb.{MongoClientException, MongoClient}
import java.util
import de.flapdoodle.embed.process.io.NullProcessor
import de.flapdoodle.embed.process.config.io.ProcessOutput
import scala.collection.JavaConversions._


trait EmbeddedMongoSpec extends WordSpec with BeforeAndAfterAll with ShouldMatchers with OneInstancePerTest with BeforeAndAfter {
  this: Suite =>

  private val LOGGER = Logger.getLogger("MongoD")

  private def runtime(): MongodStarter = MongodStarter.getInstance(runtimeConfig)

  private def mongodExec(port: Int, version: Version): MongodExecutable = runtime().prepare(new MongodConfig(version, port, true))

  protected def mongoStart(port: Int = 12345, version: Version = Version.V2_3_0): MongodProps = {
    val mongodExe = mongodExec(port, version)
    MongodProps(mongodExe.start(), mongodExe)
  }

  protected def mongoStop(mongodProps: MongodProps) = {
    Option(mongodProps).foreach(_.mongodProcess.stop())
    Option(mongodProps).foreach(_.mongodExe.stop())
  }

  val port = 12345
  val host = "localhost"

  private var mongoProps: MongodProps = null

  override protected def beforeAll() {
    mongoProps = mongoStart(port)
  }

  override protected def afterAll() {
    mongoStop(mongoProps)
  }

  before {
    cleanDatabase()
  }

  private lazy val client = new MongoClient(host, port)

  def cleanDatabase() {
    try {
      val names: util.List[String] = client.getDatabaseNames
      names.filter(_ != "admin").foreach(d => client.dropDatabase(d))
    } catch {
      case e: MongoClientException =>
    }
  }

  private def runtimeConfig = {
    val nullProcessor = new NullProcessor()
    val config = RuntimeConfig.getInstance(LOGGER)
    config.setProcessOutput(new ProcessOutput(nullProcessor, nullProcessor, nullProcessor))
    config
  }
}
