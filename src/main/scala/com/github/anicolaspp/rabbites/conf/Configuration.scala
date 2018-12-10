package com.github.anicolaspp.rabbites.conf

case class Configuration(rabbitEndPoint: String,
                         workers: Int,
                         queueName: String,
                         stream: String,
                         topic: String)

object Configuration {

  def parse(args: Seq[String]): Option[Configuration] = parser.parse(args, default)

  private lazy val default = Configuration(rabbitEndPoint = "", workers = 1, queueName = "", stream = "", topic = "")

  private lazy val parser = new scopt.OptionParser[Configuration]("rabbites") {
    head("rabbites")

    opt[String]('r', "rabbit-endpoint")
      .action((endpoint, conf) => conf.copy(rabbitEndPoint = endpoint))
      .required()
      .maxOccurs(1)
      .text("RabbitMQ endpoint to read from.")

    opt[Int]('w', "num-workers")
      .action((w, conf) => conf.copy(workers = w))
      .maxOccurs(1)
      .withFallback(() => 1)
      .text("Number of workers to connect, share, and process RabbitMQ messages.")

    opt[String]('q', "queue-name")
      .action((queue, conf) => conf.copy(queueName = queue))
      .required()
      .maxOccurs(1)
      .text("RabbitMQ queue name to read from.")

    opt[String]('s', "stream")
      .action((s, conf) => conf.copy(stream = s))
      .maxOccurs(1)
      .required()
      .text("Name of the MapR stream where messages are going to be written to.")

    opt[String]('t', "topic")
      .action((t, conf) => conf.copy(topic = t))
      .maxOccurs(1)
      .text("Name of the MapR stream where messages are going to be written to. If not provides, --queue-name will be used.")
  }
}