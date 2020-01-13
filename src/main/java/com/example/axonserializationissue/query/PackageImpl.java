package com.example.axonserializationissue.query;

import com.example.axonserializationissue.api.Content;
import com.example.axonserializationissue.api.ContentCategory;
import com.example.axonserializationissue.api.Package;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Entity(name = "Package")
@Table(name = "package_record")
@TypeDef(
    name = "jsonb",
    typeClass = JsonBinaryType.class
)
public class PackageImpl implements Package {

  @Id
  private String aggregateId;

  @Column
  private long aggregateVersion;

  @Column
  private Instant createdAt;

  @Column
  private String description;

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private LinkedHashSet<ContentImpl> contents = new LinkedHashSet<>();

  @Type(type = "jsonb")
  @Column(columnDefinition = "jsonb")
  private PackageContentCategoryCounts packageContentCategoryCounts = new PackageContentCategoryCounts();

  public PackageImpl() {
  }

  public PackageImpl(String aggregateId, Instant createdAt) {
    this.aggregateId = aggregateId;
    this.createdAt = createdAt;
    this.aggregateVersion = -1;
  }

  @NotNull
  @Override
  public String getAggregateId() {
    return aggregateId;
  }

  @NotNull
  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public long getAggregateVersion() {
    return aggregateVersion;
  }

  public PackageImpl setAggregateVersion(long aggregateVersion) {
    this.aggregateVersion = aggregateVersion;
    return this;
  }

  @NotNull
  @Override
  public String getDescription() {
    return description;
  }

  public PackageImpl setDescription(String description) {
    this.description = description;
    return this;
  }

  @NotNull
  @Override
  public Set<ContentCategory> getContentCategories() {
    return packageContentCategoryCounts.getMapping().entrySet().stream().filter(es -> es.getValue() > 0)
        .map(Map.Entry::getKey).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  @NotNull
  @Override
  public Set<Content> getContents() {
    LinkedHashSet<? super ContentImpl> contents = this.contents;
    return (Set<Content>) contents;
  }

  public Content getContent(String contentId) {
    return contents.stream().filter(content -> content.getId().equals(contentId)).findAny().orElse(null);
  }

  @NotNull
  public PackageImpl addContent(Content content, Consumer<ContentCategory> addedCategoriesConsumer) {
    Assert.notNull(content, "null content");
    ContentImpl contentImpl = ContentImpl.from(content);
    contents.add(contentImpl);
    incrementAndAcceptOnNewCategory(content.getCategory(), addedCategoriesConsumer);
    return this;
  }

  private void incrementAndAcceptOnNewCategory(
      ContentCategory category, Consumer<ContentCategory> addedCategoriesConsumer
  ) {
    if (packageContentCategoryCounts.inc(category) == 1) addedCategoriesConsumer.accept(category);
  }

  private void decrementAndAcceptOnRemovedCategory(
      ContentCategory category, Consumer<ContentCategory> removedCategoriesConsumer
  ) {
    if (packageContentCategoryCounts.getMapping().get(category) > 0) {
      if (packageContentCategoryCounts.dec(category) == 0) removedCategoriesConsumer.accept(category);
    }
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("PackageImpl{");
    sb.append("aggregateId='").append(aggregateId).append('\'');
    sb.append(", aggregateVersion=").append(aggregateVersion);
    sb.append(", createdAt=").append(createdAt);
    sb.append(", description='").append(description).append('\'');
    sb.append(", contents=").append(contents);
    sb.append(", packageContentCategoryCounts=").append(packageContentCategoryCounts);
    sb.append(", contentCategories=").append(getContentCategories());
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PackageImpl)) return false;
    PackageImpl aPackage = (PackageImpl) o;
    return Objects.equals(aggregateId, aPackage.aggregateId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aggregateId);
  }
}
