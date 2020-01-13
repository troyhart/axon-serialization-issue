package com.example.axonserializationissue.query

import com.example.axonserializationissue.api.*
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.SequenceNumber
import org.axonframework.eventhandling.gateway.EventGateway
import org.axonframework.queryhandling.QueryUpdateEmitter
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.util.*

@Service
open class PackageStateProjector(
    private val queryUpdateEmitter: QueryUpdateEmitter, private val repository: PackageImplRepository, private val eventGateway: EventGateway
) {

  @EventHandler
  fun on(event: PackageCreated, @SequenceNumber aggregateVersion: Long) {
    save(PackageImpl(event.aggregateId, event.createdAt).setDescription(event.description), aggregateVersion)
  }

  @EventHandler
  fun on(event: ContentAdded, @SequenceNumber aggregateVersion: Long) {
    var pkg = repository.findById(event.aggregateId).orElseThrow { NoSuchElementException() }
    Assert.isNull(pkg.getContent(event.contentId), "content with given identifier already exists!")
    val content = ContentImpl(event.contentId, event.description, event.category)
    val categoriesAdded = HashSet<ContentCategory>()
    pkg.addContent(
        content
    ) { e: ContentCategory -> categoriesAdded.add(e) }
    pkg = save(pkg, aggregateVersion)
    if (!categoriesAdded.isEmpty() ) {
      eventGateway.publish(PackageContentCategoriesUpdated(pkg, categoriesAdded, emptySet()))
    }
  }

  fun save(pkg: PackageImpl, aggregateVersion: Long): PackageImpl {
    Assert.isTrue(pkg.aggregateVersion < aggregateVersion,
        "Invalid version given: $aggregateVersion; current state: $pkg")
    pkg.setAggregateVersion(aggregateVersion)
    queryUpdateEmitter
        .emit(PackageByAggregateId::class.java, { (aggregateId: String) -> aggregateId == pkg.aggregateId }, pkg)
    return repository.save(pkg)
  }
}
