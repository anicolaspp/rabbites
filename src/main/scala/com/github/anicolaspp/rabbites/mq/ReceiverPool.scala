package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.Channel


sealed trait ReceiverPool {
  def start(receivers: Int): Unit
}

object ReceiverPool {
  def apply(channel: Channel, queueName: String, producer: Producer): ReceiverPool = new ReceiverPool {
    override def start(receivers: Int): Unit =
      (1 to receivers).foreach { _ => Receiver(channel, queueName).runWith(producer) }
  }
}