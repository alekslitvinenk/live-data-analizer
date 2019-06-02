package com.myapp.io

import com.myapp.domain.Protocol.AggregatedUserData

trait FileWriter {
  def createFile(aggregatedUserData: AggregatedUserData): Unit
}
