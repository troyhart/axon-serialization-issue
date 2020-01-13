package com.example.axonserializationissue.api

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(`as` = Content::class)
@JsonDeserialize(`as` = ContentDto::class)
interface Content {
  val id: String
  val description: String
  val category: ContentCategory
}
