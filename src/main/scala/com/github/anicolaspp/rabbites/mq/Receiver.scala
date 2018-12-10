package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.{Channel, DeliverCallback}

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
      channel.basicConsume(queueName, false, deliverCallback(channel, receiverId, producer), _ => {})
    }

    private def deliverCallback(channel: Channel, id: Int, producer: Producer): DeliverCallback = (_, delivery) => {
      val message = new String(delivery.getBody, "UTF-8")

      println(" [x] Received '" + message + "'")

      try {
        //        doWork(message)

        producer.produce(message)

      } finally {
        println(s" Done $id")
        channel.basicAck(delivery.getEnvelope.getDeliveryTag, false)
      }
    }
  }
}
