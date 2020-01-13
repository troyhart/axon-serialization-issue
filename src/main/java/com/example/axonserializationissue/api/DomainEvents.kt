package com.example.axonserializationissue.api

import java.time.Instant

interface DomainEvent {
  val aggregateId: String
}

interface PackageEvent : DomainEvent

data class PackageCreated(
    override val aggregateId: String,
    val description: String,
    val createdAt: Instant
) : PackageEvent

data class ContentAdded(
    override val aggregateId: String,
    val contentId: String,
    val description: String,
    val category: ContentCategory
) : PackageEvent
