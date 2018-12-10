package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.{CancelCallback, Channel, DeliverCallback, Delivery}

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
//      channel.basicConsume(queueName, false, deliverCallback(channel, receiverId, producer), _ => {})

      channel.basicConsume(queueName, false, new DeliverCallback {
        override def handle(consumerTag: String, delivery: Delivery): Unit = {
          val message = new String(delivery.getBody, "UTF-8")

                println(s" [x] [$id] Received $message")

                producer.produce(message)

                channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
        }
      }, new CancelCallback {
        override def handle(consumerTag: String): Unit = ???
      })
    }
//
//    private def deliverCallback(channel: Channel, id: Int, producer: Producer): DeliverCallback = (_, delivery) => {
//      val message = new String(delivery.getBody, "UTF-8")
//
//      println(s" [x] [$id] Received $message")
//
//      producer.produce(message)
//
//      channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
//    }
  }
}
