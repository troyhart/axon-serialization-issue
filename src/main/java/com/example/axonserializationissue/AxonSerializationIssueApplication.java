package com.example.axonserializationissue;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.json.JacksonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class AxonSerializationIssueApplication {

  public static void main(String[] args) {
    SpringApplication.run(AxonSerializationIssueApplication.class, args);
  }

//  @Bean
//  Serializer eventSerializer() {
//    // Axon event serializer configuration.... I'm attempting to configure axon's jackson serializer as the event serializer
//    // and attempting to configure it to handler kotlin and java time
//    return JacksonSerializer.builder().objectMapper(
//        new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//            .registerModule(new KotlinModule()).registerModule(new Jdk8Module())
//            .registerModule(new ParameterNamesModule()).registerModule(new JavaTimeModule())).build();
//  }

  @Bean
  public Docket api() {
    // @formatter:off
    return new Docket(DocumentationType.SWAGGER_2)
        .pathMapping("/")
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example"))
        .paths(PathSelectors.any())
        .build();
    // @formatter:on
  }

  @Bean
  public HttpMessageConverters customConverters() {
    HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
    return new HttpMessageConverters(additional);
  }
}
