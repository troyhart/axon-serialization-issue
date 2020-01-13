package com.example.axonserializationissue.query;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageImplRepository extends JpaRepository<PackageImpl, String> {
}
