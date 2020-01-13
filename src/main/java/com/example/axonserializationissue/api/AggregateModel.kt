package com.example.axonserializationissue.api

interface AggregateModel {
  val aggregateId: String
  val aggregateVersion: Long
}
