package com.example.axonserializationissue.api

data class ContentDto @JvmOverloads constructor(
    override var id: String = "",
    override var description: String = "",
    override var category: ContentCategory = ContentCategory.NONE
) : Content {

  companion object {

    fun from(content: Content): ContentDto {
      return ContentDto(
          content.id,
          content.description,
          content.category)
    }
  }
}
