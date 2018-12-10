package com.github.anicolaspp.rabbites.mapres


sealed trait Producer {

  def produce(message: String): Unit

}


object Producer {
  def apply(): Producer = new Producer{
    override def produce(message: String): Unit = {
      // send message to MapR-ES
    }
  }
}
