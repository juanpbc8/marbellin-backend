package com.marbellin.catalog.repository;

import com.marbellin.catalog.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
