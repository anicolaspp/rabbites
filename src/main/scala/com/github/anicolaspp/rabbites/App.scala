package com.github.anicolaspp.rabbites

import com.github.anicolaspp.rabbites.conf.Configuration
import com.github.anicolaspp.rabbites.mapres.Producer
import com.github.anicolaspp.rabbites.mq.ReceiverPool
import com.rabbitmq.client.ConnectionFactory

object App {

  def main(args: Array[String]): Unit =
    Configuration
      .parse(args)
      .map(getPool)
      .foreach { case (pool, conf) => pool.start(conf.workers) }
  
  private def getPool(conf: Configuration): (ReceiverPool, Configuration) =
    (ReceiverPool(getChannelForHost(conf.rabbitEndPoint), conf.queueName, () => Producer(conf.stream, conf.topic)), conf)

  private def getChannelForHost(host: String) = {
    val factory = new ConnectionFactory()

    factory.setHost(host)
    val connection = factory.newConnection()

    connection.createChannel()
  }
}

