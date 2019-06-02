package com.myapp.common

import com.myapp.Protocol.UserData

object TestData {

  def stringToUserData1 = {

    val stringData = "0977dca4-9906-3171-bcec-87ec0df9d745,kFFzW4O8gXURgP8ShsZ0gcnNT5E=,0.18715484122922377,982761284,8442009284719321817"

    val userData = UserData(
      userId = "0977dca4-9906-3171-bcec-87ec0df9d745",
      data = "kFFzW4O8gXURgP8ShsZ0gcnNT5E=",
      weight = 0.18715484122922377f,
      int1 = 982761284,
      int2 = 8442009284719321817L
    )

    (stringData, userData)
  }
}
