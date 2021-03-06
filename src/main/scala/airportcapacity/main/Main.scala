package airportcapacity.main

import airportcapacity.db.{AppDatabase, DbConfig}
import airportcapacity.domain.AirportId
import airportcapacity.service._
import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.{ActorMaterializer, Materializer}
import com.outworkers.phantom.connectors.ContactPoint
import com.typesafe.config.ConfigFactory
import airportcapacity.utils.DurationExtensions._

import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext

trait Setup {
  import com.softwaremill.macwire._

  implicit val system: ActorSystem
  implicit val executor: ExecutionContext
  implicit val materializer: Materializer

  lazy val logger           = Logging(system, getClass)
  lazy val config           = ConfigFactory.load()
  lazy val airportConfig    = AirportConfig(config.getStringList("airport.ids").asScala.map(AirportId), config.getString("airport.file"))
  lazy val flightInfoConfig = FlightInfoConfig(config.getString("flight-info.open-sky-url"))

  lazy val cassandraConfig = DbConfig(hostname = config.getString("cassandra.hostname"),
                                      port = config.getInt("cassandra.port"),
                                      replicationStrategy = config.getString("cassandra.replication-strategy"),
                                      replicationFactor = config.getString("cassandra.replication-factor"),
                                      defaultConsistencyLevel = config.getString("cassandra.default-consistency-level"),
                                      keyspace = config.getString("cassandra.keyspace"))

  lazy val aggregatorConfig = AggregatorConfig(config.getDuration("aggregator.duration.collect-data").toFiniteDuration,
                                               config.getDuration("aggregator.duration.write-to-db").toFiniteDuration)

  lazy val connector = ContactPoint
    .apply(cassandraConfig.hostname, cassandraConfig.port)
    .keySpace(cassandraConfig.keyspace)
  lazy val db: AppDatabase                      = wire[AppDatabase]
  lazy val airportService: AirportService       = wire[AirportService]
  lazy val flightInfoService: FlightInfoService = wire[FlightInfoService]
  lazy val aggregatorService: AggregatorService = wire[AggregatorService]

}

object Main extends App with Setup {
  implicit val system       = ActorSystem()
  implicit val executor     = system.dispatcher
  implicit val materializer = ActorMaterializer()
  aggregatorService.source.runWith(aggregatorService.cassandraSink)
}
