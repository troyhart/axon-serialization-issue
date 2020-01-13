package com.example.axonserializationissue.restutils

import java.util.*

object AggregateIdResponseSupport {
  /**
   * A singleton map to carry an aggregate's identifier.
   *
   * @param idValue the value of the aggregate's identifier
   *
   * @return a singleton map with `key=aggregateId` and `value={idValue}`
   */
  @JvmStatic
  fun aggregateIdMapping(idValue: Any): AggregateId {
    return AggregateId(idValue.toString())
  }
}
