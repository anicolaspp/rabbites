package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.{Channel, DeliverCallback, Delivery}

import scala.util.{Failure, Success}

sealed trait Receiver {
  def id(): Int

  def runWith(producer: Producer): Unit
}

object Receiver {

  def apply(channel: Channel, queueName: String): Receiver = new Receiver {

    private lazy val receiverId = IdCounter.next()

    override def id(): Int = receiverId

    override def runWith(producer: Producer): Unit = {
      println(s" [x] Receiver ${id()} started")

      channel.basicQos(1)

      channel.queueDeclare(queueName, false, false, false, null)

      channel.basicConsume(queueName, false, callBack(producer, channel, id()), (_: String) => {})
    }
    
    private def callBack(producer: Producer, channel: Channel, id: Int): DeliverCallback = (_: String, delivery: Delivery) => {
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
}




