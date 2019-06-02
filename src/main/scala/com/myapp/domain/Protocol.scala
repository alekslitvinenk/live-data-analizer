package com.myapp.domain

import scala.collection.mutable

object Protocol {

  type Chunk = mutable.Queue[UserData]

  case class UserData(userId: String, data: String, weight: Double, int1: Int, int2: BigInt)

  case class AggregatedUserData(sumInt2: BigInt, usersCount: Int, usersTotals: List[UserTotals])

  case class UserTotals(userId: String, avgWeight: Double, latestInt1: Int) {
    override def toString: String =
      userId + "," + avgWeight + "," + latestInt1
  }
}
