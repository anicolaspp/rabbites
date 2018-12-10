package com.github.anicolaspp.rabbites.receiver

object IdCounter {
  private var currentId = 0

  def next() = synchronized {
    val nextId = currentId

    currentId += 1

    nextId
  }
}
