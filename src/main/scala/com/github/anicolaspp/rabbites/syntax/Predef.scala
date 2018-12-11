package com.github.anicolaspp.rabbites.syntax

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Try

object Predef {

  implicit class RichProducerRecord[A, B](record: ProducerRecord[A, B]) {
    def sendWith(producer: KafkaProducer[A, B]) = Try { producer.send(record).get() }
  }

}
