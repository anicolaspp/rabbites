name := "rabbites"

version := "1.0.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "MapR Releases" at "http://repository.mapr.com/maven",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

resolvers += Resolver.mavenLocal

libraryDependencies ++= Seq(
  "com.rabbitmq" % "amqp-client" % "5.5.1",

  "com.mapr.streams" % "mapr-streams" % "6.0.1-mapr" % "provided",
  "org.apache.kafka" % "kafka-clients" % "1.1.1-mapr-1808" % "provided",
  "commons-logging" % "commons-logging" % "1.2" % "provided",
  "com.github.scopt" % "scopt_2.11" % "3.7.0"
//  "org.scala-lang" % "scala-library-all" % scalaVersion.value
)

assemblyJarName := s"${name.value}-${version.value}.jar"