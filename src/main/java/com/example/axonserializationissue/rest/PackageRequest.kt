package com.example.axonserializationissue.rest

import com.example.axonserializationissue.api.CreatePackage
import org.springframework.util.Assert

data class PackageRequest @JvmOverloads constructor(
    var description: String = ""
) {

  fun toCreatePackageCommand(aggregateId: String): CreatePackage {
    Assert.hasText(aggregateId, "null/blank aggregateId")
    Assert.hasText(description, "null/blank description")
    return CreatePackage(aggregateId.trim(), description.trim())
  }
}
