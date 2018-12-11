package com.github.anicolaspp.rabbites

import com.github.anicolaspp.rabbites.conf.Configuration
import com.github.anicolaspp.rabbites.mapres.Producer
import com.github.anicolaspp.rabbites.mq.ReceiverPool
import com.rabbitmq.client.{Channel, ConnectionFactory}
import syntax.Predef._

object App {

  def main(args: Array[String]): Unit =
    Configuration
      .parse(args)
      .map(getReceiversPool)
      .foreach { case (pool, conf) => pool.start(conf.workers) }

  private def getReceiversPool(conf: Configuration): (ReceiverPool, Configuration) =
    (ReceiverPool(getChannelForHost(conf.rabbitEndPoint), conf.queueName, () => Producer(conf.stream, conf.topic)), conf)

  private def getChannelForHost(host: String): Channel =
    new ConnectionFactory()
      .host(host)
      .newConnection()
      .createChannel()
}

