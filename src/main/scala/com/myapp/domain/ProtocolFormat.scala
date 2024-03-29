package com.myapp.domain

import com.myapp.domain.Protocol.UserData

object ProtocolFormat {

  // Implicit decider to decode data from string to UserData
  implicit def userDataDecoder(source: String): UserData = {
    val components = source.split(',')

    UserData(
      userId = components(0),
      data = components(1),
      weight = components(2).toDouble,
      int1 = components(3).toInt,
      int2 = BigInt(components(4)),
    )
  }
}
