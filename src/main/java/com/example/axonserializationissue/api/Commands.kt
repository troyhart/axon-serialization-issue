package com.example.axonserializationissue.api

import org.axonframework.modelling.command.TargetAggregateIdentifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

interface Command {
  val aggregateId: String
}

interface PackageCommand : Command

data class CreatePackage(
    @TargetAggregateIdentifier
    override val aggregateId: String,
    val description: String
) : PackageCommand

data class AddContent(
    @TargetAggregateIdentifier
    override val aggregateId: String,
    val contentId: String,
    val description: String,
    val category: ContentCategory
) : PackageCommand
