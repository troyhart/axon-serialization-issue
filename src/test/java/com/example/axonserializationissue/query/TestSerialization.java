package com.example.axonserializationissue.query;

import com.example.axonserializationissue.api.ContentCategory;
import com.example.axonserializationissue.api.Package;
import com.example.axonserializationissue.api.PackageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.axonframework.serialization.json.JacksonSerializer;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class TestSerialization {

  private static final Random RANDOM = new Random();

  private ObjectMapper mapper;
  private PackageImpl pkg;

  @Before
  public void setup() {

    mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new KotlinModule()).registerModule(new Jdk8Module()).registerModule(new ParameterNamesModule())
        .registerModule(new JavaTimeModule());

    // skip the first content category, but otherwise select a random one!
    int randomContentCategoryIndex = Math.max(1, RANDOM.nextInt(ContentCategory.values().length));
    ContentCategory randomContentCategory = ContentCategory.values()[randomContentCategoryIndex];

    pkg = new PackageImpl("my-custom-package-id", Instant.now());
    pkg.setDescription("My Package");
    pkg.addContent(new ContentImpl("my-custom-content-id", "bright shiny thing", randomContentCategory),
        contentCategory -> {
          System.out.println("Content category added: " + contentCategory);
        });
    System.out.println(String.format("Setup serialization/deserialization for:\n%s\n\n", pkg));
  }

  @Test
  public void test1() throws JsonProcessingException {
    String classDataString = mapper.writeValueAsString(pkg);
    System.out.println(String.format("Serialized Form:\n%s\n----------------", classDataString));
    assertEquals("Not equal after serialization/deserialization", PackageDto.Companion.from(pkg),
        mapper.readValue(classDataString, Package.class));
  }

  @Test
  public void test2() {

    JacksonSerializer jacksonSerializer = JacksonSerializer.builder().objectMapper(mapper).build();

    Object so = jacksonSerializer.serialize(PackageDto.Companion.from(pkg), PackageDto.class);
    System.out.println(so);
  }
}
