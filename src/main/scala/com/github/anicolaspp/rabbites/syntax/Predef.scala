package com.github.anicolaspp.rabbites.syntax

import com.rabbitmq.client.ConnectionFactory
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Try

object Predef {

  implicit class RichProducerRecord[A, B](record: ProducerRecord[A, B]) {
    def sendWith(producer: KafkaProducer[A, B]) = Try { producer.send(record).get() }
  }

  implicit class RichFactory(factory: ConnectionFactory) {
    def host(host: String): ConnectionFactory = {
      factory.setHost(host)

      factory
    }
  }
}
