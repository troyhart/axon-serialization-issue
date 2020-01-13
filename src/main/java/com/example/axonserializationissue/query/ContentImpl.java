package com.example.axonserializationissue.query;

import com.example.axonserializationissue.api.Content;
import com.example.axonserializationissue.api.ContentCategory;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.util.Assert;

import java.util.Objects;

@JsonSerialize(as = ContentImpl.class)
@JsonDeserialize(as = ContentImpl.class)
public class ContentImpl implements Content {
  private String id;
  private String description;
  private ContentCategory category;
  private String otherAttr;

  public ContentImpl() {
  }

  public ContentImpl(String id, String description, ContentCategory category) {
    Assert.hasText(id, "null/blank id");
    this.id = id;
    this.description = description;
    this.category = category;
  }

  static final ContentImpl from(Content content) {
    return new ContentImpl(content.getId(), content.getDescription(), content.getCategory());
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ContentCategory getCategory() {
    return category;
  }

  public void setCategory(ContentCategory category) {
    this.category = category;
  }

  @Override
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    Assert.hasText(description, "null/blank value");
    this.description = description;
  }

  public String getOtherAttr() {
    return otherAttr;
  }

  public void setOtherAttr(String otherAttr) {
    this.otherAttr = otherAttr;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ContentImpl{");
    sb.append("id='").append(id).append('\'');
    sb.append(", description='").append(description).append('\'');
    sb.append(", category=").append(category);
    sb.append(", otherAttr='").append(otherAttr).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ContentImpl)) return false;
    ContentImpl content = (ContentImpl) o;
    return Objects.equals(id, content.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
