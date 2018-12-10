package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.{CancelCallback, Channel}

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

      channel.basicConsume(queueName, false, deliveryProcessing(producer), new CancelCallback {
        override def handle(consumerTag: String): Unit = {}
      })
    }

    private def deliveryProcessing(producer: Producer) = RabbitMessageProcessor(producer, channel, id())
  }
}




