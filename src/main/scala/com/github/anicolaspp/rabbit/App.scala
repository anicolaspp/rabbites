package com.github.anicolaspp.rabbit

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Connection
import com.rabbitmq.client.Channel


object App {

  private val QUEUE_NAME = "hello"

  def main(args: Array[String]): Unit = {
    val factory = new ConnectionFactory()

    factory.setHost("localhost")

    val connection = factory.newConnection()

    val channel = connection.createChannel()

    channel.queueDeclare(QUEUE_NAME, false, false, false, null)

    (1 to 1000).foreach {i =>
      val message = s"Message [$i]"

      channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"))

      println(" [x] Sent '" + message + "'")
    }

    
    channel.close()

    connection.close()
  }

}
