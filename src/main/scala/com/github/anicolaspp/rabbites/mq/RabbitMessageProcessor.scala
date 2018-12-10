package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.{Channel, DeliverCallback, Delivery}

import scala.util.{Failure, Success}

class RabbitMessageProcessor(producer: Producer, channel: Channel, id: Int) extends DeliverCallback {
  override def handle(consumerTag: String, delivery: Delivery): Unit = {
    val message = new String(delivery.getBody, "UTF-8")

    println(s" [x] [$id] Received $message")

    producer.produce(message) match {
      case Success(metadata) => {
        println(s" [x] [$id] TOPIC:${metadata.topic()}; OFFSET: ${metadata.offset()}")

        channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
      }
      case Failure(exception) => {
        println(s" [x] [$id] Error $exception")

        channel.basicReject(delivery.getEnvelope.getDeliveryTag, true)
      }
    }
  }
}

object RabbitMessageProcessor {
  def apply(producer: Producer, channel: Channel, id: Int) = new RabbitMessageProcessor(producer, channel, id)
}
