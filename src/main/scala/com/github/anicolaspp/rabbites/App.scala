package com.github.anicolaspp.rabbites

import com.github.anicolaspp.rabbites.receiver.ReceiverPool
import com.rabbitmq.client.ConnectionFactory


object App {

  private val QUEUE_NAME = "hello"

  def main(args: Array[String]): Unit = ReceiverPool(getChannelForHost("localhost"), QUEUE_NAME).start(5)

  private def getChannelForHost(host: String) = {
    val factory = new ConnectionFactory()

    factory.setHost(host)
    val connection = factory.newConnection()

    connection.createChannel()
  }
}



