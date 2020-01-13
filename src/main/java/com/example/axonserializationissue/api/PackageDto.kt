package com.example.axonserializationissue.api

import org.springframework.util.CollectionUtils
import java.time.Instant
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.collections.LinkedHashSet
import kotlin.streams.toList

data class PackageDto @JvmOverloads constructor(
    override var aggregateId: String = "",
    override val aggregateVersion: Long,
    override var description: String = "",
    override var contents: Set<Content> = Collections.emptySet(),
    override var createdAt: Instant = Instant.MIN,
    override var contentCategories: Set<ContentCategory> = Collections.emptySet()
) : Package {

  companion object {

    fun from(pkg: Package): PackageDto {
      var contents: Set<ContentDto> = Collections.emptySet()
      if (!CollectionUtils.isEmpty(pkg.contents)) {
        contents = LinkedHashSet(pkg.contents.stream().filter(Predicate { t:Content? -> t != null }).map { t: Content? -> ContentDto.Companion.from(t!!) }.toList())
      }
      return PackageDto(
          pkg.aggregateId,
          pkg.aggregateVersion,
          pkg.description,
          contents,
          pkg.createdAt,
          pkg.contentCategories)
    }
  }
}
