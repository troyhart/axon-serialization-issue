package com.example.axonserializationissue.rest

import com.example.axonserializationissue.api.AddContent
import com.example.axonserializationissue.api.ContentCategory
import org.springframework.util.Assert

data class ContentRequest @JvmOverloads constructor(
    var description: String = "",
    var contentCategory: ContentCategory = ContentCategory.NONE
) {

  fun toAddContentCommand(aggregateId: String, contentId: String): AddContent {
    Assert.hasText(aggregateId, "null/blank aggregateId")
    Assert.hasText(contentId, "null/blank contentId")
    Assert.hasText(description, "null/blank description")
    return AddContent(aggregateId.trim(), contentId.trim(), description.trim(), contentCategory)
  }
}

