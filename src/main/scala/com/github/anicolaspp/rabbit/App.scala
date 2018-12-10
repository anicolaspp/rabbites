package com.github.anicolaspp.rabbit

import com.github.anicolaspp.rabbit.receiver.ReceiverPool
import com.rabbitmq.client.ConnectionFactory


object App {

  private val QUEUE_NAME = "hello"

  def main(args: Array[String]): Unit = ReceiverPool(getChannelForHost("localhost"), QUEUE_NAME).start(5)

  def getChannelForHost(host: String) = {
    val factory = new ConnectionFactory()

    factory.setHost(host)
    val connection = factory.newConnection()

    connection.createChannel()
  }
}


