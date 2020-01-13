package com.example.axonserializationissue.query

import com.example.axonserializationissue.api.Package
import com.example.axonserializationissue.api.PackageByAggregateId
import com.example.axonserializationissue.api.PackageDto
import org.axonframework.eventhandling.gateway.EventGateway
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Service
import java.util.*

@Service
open class PackageQueryHandler(
    private val repository: PackageImplRepository, private val eventGateway: EventGateway
) {

  @QueryHandler
  fun handle(query: PackageByAggregateId): Package? {
    val pkg: Optional<PackageImpl> = repository.findById(query.aggregateId)
    return if (pkg.isPresent) PackageDto.Companion.from(pkg.get()) else null
  }
}
