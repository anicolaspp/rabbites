package com.github.anicolaspp.rabbites.mq

import com.github.anicolaspp.rabbites.mapres.Producer
import com.rabbitmq.client.Channel

trait ReceiverPool {
  def start(receivers: Int): Unit
}

object ReceiverPool {
  def apply(channel: Channel, queueName: String, producerSupplier: () => Producer): ReceiverPool =
    (receivers: Int) => (1 to receivers).foreach { _ => Receiver(channel, queueName).runWith(producerSupplier.apply()) }
}