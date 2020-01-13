package com.example.axonserializationissue.command

import com.example.axonserializationissue.api.AddContent
import com.example.axonserializationissue.api.ContentAdded
import com.example.axonserializationissue.api.CreatePackage
import com.example.axonserializationissue.api.PackageCreated
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.util.Assert
import java.time.Instant
import java.util.function.Predicate

@Aggregate
class PackageAggregate() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(PackageAggregate::class.java)
  }

  @AggregateIdentifier
  private lateinit var aggregateId: String
  private lateinit var description: String
  private val contents = HashSet<ContentEntity>()

  @CommandHandler
  constructor(command: CreatePackage) : this() {
    AggregateLifecycle.apply(PackageCreated(command.aggregateId, command.description, Instant.now()))
    LOGGER.info("Package Created!")
  }

  @CommandHandler
  fun handle(command: AddContent) {
    Assert.isNull(getContent(command.contentId), "Content identifier not unique")
    AggregateLifecycle.apply(ContentAdded(command.aggregateId, command.contentId, command.description, command.category))
    LOGGER.info("Content Added!")
  }

  @EventSourcingHandler
  fun on(event: PackageCreated) {
    aggregateId = event.aggregateId
    description = event.description
  }

  @EventSourcingHandler
  fun on(event: ContentAdded) {
    contents.add(ContentEntity(event.contentId, event.description, event.category))
  }

  fun getContent(contentId: String): ContentEntity? {
    return contents.stream().filter(Predicate { it.id == contentId }).findAny().orElse(null)
  }
}
