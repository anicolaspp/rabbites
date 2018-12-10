package com.github.anicolaspp.rabbites.mapres

import java.util.Properties
import java.util.concurrent.Future

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.util.Try

sealed trait Producer {
  def produce(message: String): Try[RecordMetadata]
}

object Producer {

  def apply(streamName: String, topic: String): Producer = new Producer {

    private val TOPIC = streamName + ":" + topic

    override def produce(message: String): Try[RecordMetadata] = Try {

      val producer = getProducer()
      val record = new ProducerRecord[String, String](TOPIC, message)

      //blocking here is not important since this entire process in running on a different thread.
      producer.send(record).get()
    }


    private def getProducer() = {
      val props = new Properties()
      props.setProperty("batch.size", "16384")
      props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.setProperty("block.on.buffer.full", "true")

      new KafkaProducer[String, String](props)
    }
  }
}