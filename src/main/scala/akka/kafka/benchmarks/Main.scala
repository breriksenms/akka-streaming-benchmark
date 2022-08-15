package akka.kafka.benchmarks

import akka.actor.ActorSystem
import akka.kafka.benchmarks.Timed.runPerfTest

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("benchmarks")
    runPerfTest("1-million-test", 1000000, ReactiveKafkaConsumerFixtures.plainSources(), ReactiveKafkaConsumerBenchmarks.consumePlain)
  }
}
