package com.github.anicolaspp.rabbites.mapres

import java.util.Properties

import com.github.anicolaspp.rabbites.syntax.Predef._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.util.Try

sealed trait Producer {
  def produce(message: String): Try[RecordMetadata]
}

object Producer {

  def apply(streamName: String, topic: String): Producer = new Producer {

    private val TOPIC = streamName + ":" + topic

    override def produce(message: String): Try[RecordMetadata] =
      new ProducerRecord[String, String](TOPIC, message).sendWith(producer)

    private lazy val producer = {
      val props = new Properties()
      props.setProperty("batch.size", "16384")
      props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.setProperty("block.on.buffer.full", "true")

      new KafkaProducer[String, String](props)
    }
  }
}