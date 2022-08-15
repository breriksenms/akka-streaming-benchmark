/*
 * Copyright (C) 2014 - 2016 Softwaremill <https://softwaremill.com>
 * Copyright (C) 2016 - 2020 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.kafka.benchmarks

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.Control
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

case class ReactiveKafkaConsumerTestFixture[T](msgCount: Int,
                                               source: Source[T, Control],
                                               numberOfPartitions: Int)

object ReactiveKafkaConsumerFixtures {

  private def createConsumerSettings()(implicit actorSystem: ActorSystem) =

    ConsumerSettings(actorSystem, new ByteArrayDeserializer, new StringDeserializer)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  def plainSources()(implicit actorSystem: ActorSystem) =
    FixtureGen[ReactiveKafkaConsumerTestFixture[ConsumerRecord[Array[Byte], String]]](
      msgCount => {
        val settings = createConsumerSettings()
        val source = Consumer.plainSource(settings, Subscriptions.topics("vehiot-eventhub"))
        ReactiveKafkaConsumerTestFixture(msgCount, source, 32)
      }
    )

}
