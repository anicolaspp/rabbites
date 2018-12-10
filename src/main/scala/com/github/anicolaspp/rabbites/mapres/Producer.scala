package com.github.anicolaspp.rabbites.mapres

import java.util.Properties

import org.apache.kafka.clients.producer.ProducerRecord


sealed trait Producer {

  def produce(message: String): Unit

}


object Producer {

  private lazy val TOPIC = "/user/mapr/streams/bridge:hello"

  def apply(): Producer = new Producer {
    override def produce(message: String): Unit = {
      
      try {
        val producer = getProducer()
        val record = new ProducerRecord[String, String](TOPIC, message)

        producer.send(record)

      } catch {
        case t: Throwable => println(t)
      }

    }


    import org.apache.kafka.clients.producer.KafkaProducer

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
