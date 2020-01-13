package com.example.axonserializationissue.query

import com.example.axonserializationissue.api.ContentCategory
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap
import kotlin.collections.LinkedHashSet
import kotlin.math.max

class PackageContentCategoryCounts {
  @JsonProperty
  private val categoryCounts: MutableMap<ContentCategory, Long> = LinkedHashMap()

  @JsonIgnore
  fun getMapping(): Map<ContentCategory, Long> {
    return Collections.unmodifiableMap(categoryCounts)
  }

  fun inc(category: ContentCategory): Long {
    if (categoryCounts[category] == null) {
      categoryCounts[category] = 0
    }
    categoryCounts[category] = categoryCounts[category]!!.inc()
    return categoryCounts[category]!!
  }

  /**
   * Decrement the current count for the given PIICategory and return the new count.
   * The current count is considered zero whenever (if ever) it is less than or
   * equal to zero or when the given category is not mapped to a count.
   *
   * @param category the category to decrement
   *
   * @return the new count for the given category, -1 when the count is already zero
   */
  fun dec(category: ContentCategory): Long {
    // short circuit when count is already zero; must return value < 0
    if (count(category) == 0L) return -1
    // decrement and assign
    categoryCounts[category] = categoryCounts[category]!!.dec()
    // remove category when count reaches zero
    if (count(category) == 0L) categoryCounts.remove(category)
    // return new category count, defaulting to zero
    return count(category)
  }

  private fun count(category: ContentCategory): Long {
    return max(categoryCounts.getOrDefault(category, 0), 0)
  }

  override fun toString(): String {
    return "PackageContentCategoryCounts(mapping=$categoryCounts)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PackageContentCategoryCounts) return false

    if (categoryCounts != other.categoryCounts) return false

    return true
  }

  override fun hashCode(): Int {
    return categoryCounts.hashCode()
  }
}
