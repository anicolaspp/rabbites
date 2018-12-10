package com.github.anicolaspp.rabbites

import com.github.anicolaspp.rabbites.mapres.Producer
import com.github.anicolaspp.rabbites.mq.ReceiverPool
import com.rabbitmq.client.ConnectionFactory


object App {

  def main(args: Array[String]): Unit = {

    val conf = Configuration.parse(args).get


    ReceiverPool(getChannelForHost(conf.rabbitEndPoint), conf.queueName, Producer.apply).start(conf.workers)
  }

  private def getChannelForHost(host: String) = {
    val factory = new ConnectionFactory()

    factory.setHost(host)
    val connection = factory.newConnection()

    connection.createChannel()
  }
}

case class Configuration(rabbitEndPoint: String,
                         workers: Int,
                         queueName: String)

object Configuration {

  def parse(args: Seq[String]) = parser.parse(args, default)
  
  private lazy val default = Configuration(rabbitEndPoint = "", workers = 1, queueName = "")

  private lazy val parser = new scopt.OptionParser[Configuration]("rabbites") {
//    head("rabbites")

    opt[String]('r', "rabbit-endpoint")
      .action((endpoint, conf) => conf.copy(rabbitEndPoint = endpoint))
      .required()
      .maxOccurs(1)
      .text("RabbitMQ endpoint to read from.")

    opt[Int]('w', "num-workers")
      .action((w, conf) => conf.copy(workers = w))
      .maxOccurs(1)
      .withFallback(() => 1)
      .text("Number of workers to connect, share, and process RabbitMQ messages")

    opt[String]('q', "queue-name")
      .action((queue, conf) => conf.copy(queueName = queue))
      .required()
      .maxOccurs(1)
      .text("RabbitMQ queue name to read from.")
  }

}



