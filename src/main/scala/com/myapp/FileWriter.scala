package com.myapp

import com.myapp.Protocol.AggregatedUserData

trait FileWriter {
  def createFile(aggregatedUserData: AggregatedUserData): Unit
}
