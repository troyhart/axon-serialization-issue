package com.example.axonserializationissue.rest;


import org.axonframework.eventsourcing.eventstore.EventStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/eventstream")
public class EventStreamController {
  private static final Logger LOGGER = LoggerFactory.getLogger(EventStreamController.class);

  private EventStore eventStore;

  @Autowired
  public EventStreamController(EventStore eventStore) {
    this.eventStore = eventStore;
  }

  @GetMapping(path = "/{aggregateId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Object> getStream(@PathVariable String aggregateId) {
    LOGGER.info("Querying event stream for aggregate: {}", aggregateId);
    // TODO: implement ability to specify page number and page size. Presently default to last 10....
    //    Optional<Long> lastSequence = eventStore.lastSequenceNumberFor(aggregateId);
    //    return (lastSequence.isPresent()) ?
    // get the last 10
    //        eventStore.readEvents(aggregateId, Math.max(lastSequence.get() - 10, 0)).asStream()
    //            .collect(Collectors.toList())
    return eventStore.readEvents(aggregateId).asStream().collect(Collectors.toList());
  }
}
