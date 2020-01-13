package com.example.axonserializationissue.rest;

import com.example.axonserializationissue.api.*;
import com.example.axonserializationissue.api.Package;
import com.example.axonserializationissue.restutils.AggregateId;
import com.example.axonserializationissue.restutils.AggregateIdResponseSupport;
import com.example.axonserializationissue.restutils.DeferredResults;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.UUID;

@RestController()
@RequestMapping(path = "/packages")
public class PackageController {

  private QueryGateway queryGateway;
  private CommandGateway commandGateway;

  public PackageController(
      QueryGateway queryGateway, CommandGateway commandGateway
  ) {
    this.queryGateway = queryGateway;
    this.commandGateway = commandGateway;
  }

  @GetMapping(path = "/{aggregateId}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public DeferredResult<Package> findById(@PathVariable String aggregateId) {
    PackageByAggregateId q = new PackageByAggregateId(aggregateId);
    return DeferredResults.from(
        queryGateway.query(q, ResponseTypes.instanceOf(Package.class)).whenComplete(DeferredResults.completeQuery(q)));
  }

  @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public DeferredResult<AggregateId> createPackage(@RequestBody PackageRequest request) {
    CreatePackage command = request.toCreatePackageCommand(UUID.randomUUID().toString());
    return DeferredResults.from(commandGateway.send(command)
        .thenApply(aggregateId -> AggregateIdResponseSupport.aggregateIdMapping(aggregateId)));
  }

  @PostMapping(path = "/{aggregateId}/contents", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public DeferredResult<Void> addContent(@PathVariable String aggregateId, @RequestBody ContentRequest request) {
    String contentId = UUID.randomUUID().toString();
    AddContent command = request.toAddContentCommand(aggregateId, contentId);
    return DeferredResults.from(commandGateway.send(command));
  }
}
