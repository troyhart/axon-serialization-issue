package com.example.axonserializationissue.command

import com.example.axonserializationissue.api.Content
import com.example.axonserializationissue.api.ContentCategory

class ContentEntity(
    override val id: String,
    override var description: String,
    override var category: ContentCategory
) : Content {

  override fun toString(): String {
    return "ContentEntity(id='$id', description='$description', category=$category)"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ContentEntity) return false

    if (id != other.id) return false

    return true
  }

  override fun hashCode(): Int {
    return id.hashCode()
  }
}
