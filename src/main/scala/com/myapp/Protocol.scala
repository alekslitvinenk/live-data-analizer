package com.myapp

import scala.collection.mutable

object Protocol {

  type Chunk = mutable.Queue[UserData]

  case class UserData(userId: String, data: String, weight: Float, int1: Int, int2: Long)

  /*object UserData {
    def apply(source: String): UserData = {
      val components = source.split(',')
      new UserData(
        userId = components(0),
        data = components(1),
        weight = components(2).toFloat,
        int1 = components(3).toInt,
        int2 = components(4).toLong,
      )
    }
  }*/

  case class AggregatedResult(sumInt2: Long, usersCount: Int, usersTotals: List[UserTotals])

  case class UserTotals(userId: String, avgWeight: Float, latestInt1: Int) {
    override def toString: String =
      userId + "," + avgWeight + "," + latestInt1
  }
}