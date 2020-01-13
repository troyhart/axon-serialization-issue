package com.example.axonserializationissue.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.time.Instant

@JsonSerialize(`as` = Package::class)
@JsonDeserialize(`as` = PackageDto::class)
interface Package : AggregateModel {
  override val aggregateId: String
  override val aggregateVersion: Long
  val description: String
  val contents: Set<Content>
  val createdAt: Instant
  val contentCategories: Set<ContentCategory>
}
