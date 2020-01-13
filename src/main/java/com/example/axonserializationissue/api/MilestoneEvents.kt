package com.example.axonserializationissue.api

interface MilestoneEvent

interface PackageMilestoneEvent : MilestoneEvent {
  val currentPackageState: Package
}

data class PackageContentCategoriesUpdated(
    override val currentPackageState: Package,
    val categoriesAdded: Set<ContentCategory>,
    val categoriesRemoved: Set<ContentCategory>
): PackageMilestoneEvent
