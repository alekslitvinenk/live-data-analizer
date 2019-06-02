package com.myapp

import org.scalatest.{Assertion, Matchers, WordSpec}
import com.myapp.common.TestData._
import com.myapp.Protocol.UserData
import ProtocolFormat._

class ProtocolFormatSpec extends WordSpec with Matchers {

  val (str, userData) = stringToUserData1

  "ProtocolFormat" should {
    "convert line of data stream to UserData object" in {
      assertDecoding(str, userData)
    }
  }

  private def assertDecoding(str: String, expectedUserData: UserData): Assertion = {

    val decodedUserData: UserData = str
    decodedUserData should be(expectedUserData)
  }
}
