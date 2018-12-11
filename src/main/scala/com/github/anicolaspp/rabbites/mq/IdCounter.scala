package com.github.anicolaspp.rabbites.mq

object IdCounter {
  private var currentId = 0

  def next(): Int = synchronized {
    val nextId = currentId

    currentId += 1

    nextId
  }
}
