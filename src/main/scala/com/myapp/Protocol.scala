package com.myapp

import scala.collection.mutable

object Protocol {

  type Chunk = mutable.Queue[UserData]

  case class UserData(userId: String, data: String, weight: Float, int1: Int, int2: Long)

  case class AggregatedUserData(sumInt2: Long, usersCount: Int, usersTotals: List[UserTotals])

  case class UserTotals(userId: String, avgWeight: Float, latestInt1: Int) {
    override def toString: String =
      userId + "," + avgWeight + "," + latestInt1
  }
}