/*
 * Copyright (C) 2014 - 2016 Softwaremill <https://softwaremill.com>
 * Copyright (C) 2016 - 2020 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.kafka.benchmarks

import java.nio.file.Paths
import java.util.concurrent.{ForkJoinPool, TimeUnit}

import com.codahale.metrics._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object Timed extends LazyLogging {
  private val benchmarkReportBasePath = Paths.get("target")

  implicit val ec = ExecutionContext.fromExecutor(new ForkJoinPool)

  def reporter(metricRegistry: MetricRegistry): ScheduledReporter =
    Slf4jReporter
      .forRegistry(metricRegistry)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .build()

  def csvReporter(metricRegistry: MetricRegistry): ScheduledReporter =
    CsvReporter
      .forRegistry(metricRegistry)
      .convertRatesTo(TimeUnit.SECONDS)
      .convertDurationsTo(TimeUnit.MILLISECONDS)
      .build(benchmarkReportBasePath.toFile)

  def runPerfTest[F](name: String, msgCount: Int, fixtureGen: FixtureGen[F], testBody: (F, Meter) => Unit): Unit = {
    val fixture = fixtureGen.generate(msgCount)
    val metrics = new MetricRegistry()
    val meter = metrics.meter(name)
    logger.info(s"Running benchmarks for $name")
    val now = System.nanoTime()
    testBody(fixture, meter)
    val after = System.nanoTime()
    val took = (after - now).nanos
    logger.info(s"Test $name took ${took.toMillis} ms")
    reporter(metrics).report()
    csvReporter(metrics).report()
  }
}
