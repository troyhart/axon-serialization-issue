package com.example.axonserializationissue.dataloader;

import com.example.axonserializationissue.api.AddContent;
import com.example.axonserializationissue.api.ContentCategory;
import com.example.axonserializationissue.api.CreatePackage;
import com.example.axonserializationissue.api.PackageContentCategoriesUpdated;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Order(Integer.MAX_VALUE)
@Profile("SAMPLE_PACKAGE_DATA_LOADER")
public class SamplePackageDataLoader implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(SamplePackageDataLoader.class);

  private CommandGateway commandGateway;

  @Autowired
  SamplePackageDataLoader(CommandGateway commandGateway) {
    this.commandGateway = commandGateway;
  }

  @EventListener
  public void on(PackageContentCategoriesUpdated event) {
    LOGGER.info("##############  Package content categories updated: {}", event);
  }

  @Override
  public void run(String... args) throws Exception {
    Random random = new Random();

    List<String> newPackageIds = new ArrayList<>();
    for (int i = 1; i < 26; i++) {
      createPackage("Test Package", i);
    }
    for (String packageId : newPackageIds) {
      addContent(packageId, "Test content #1 for package " + packageId, random);
      addContent(packageId, "Test content #2 for package " + packageId, random);
      addContent(packageId, "Test content #3 for package " + packageId, random);
      addContent(packageId, "Test content #4 for package " + packageId, random);
    }
  }

  private void addContent(String packageId, String description, Random random) {
    // randomly select a category, but never pick the first one (ie. NONE)
    ContentCategory category = ContentCategory.values()[Math.max(1, random.nextInt(ContentCategory.values().length))];
    commandGateway.sendAndWait(new AddContent(packageId, UUID.randomUUID().toString(), description, category));
  }

  private String createPackage(String descriptionTemplate, int i) {
    String id = UUID.randomUUID().toString();
    commandGateway.sendAndWait(new CreatePackage(id, String.format("%s #%s", descriptionTemplate, i)));
    return id;
  }
}
