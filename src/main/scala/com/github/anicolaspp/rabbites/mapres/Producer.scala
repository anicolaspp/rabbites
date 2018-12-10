package com.github.anicolaspp.rabbites.mapres

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

sealed trait Producer {
  def produce(message: String): Unit
}

object Producer {

  def apply(streamName: String, topic: String): Producer = new Producer {

    private val TOPIC = streamName + ":" + topic

    override def produce(message: String): Unit = {
      
      try {
        val producer = getProducer()
        val record = new ProducerRecord[String, String](TOPIC, message)

        producer.send(record)
        
      } catch {
        case t: Throwable => println(t)
      }
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
