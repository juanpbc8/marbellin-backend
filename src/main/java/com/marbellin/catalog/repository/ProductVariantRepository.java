package com.marbellin.catalog.repository;

import com.marbellin.catalog.entity.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
    boolean existsBySku(String sku);
}
