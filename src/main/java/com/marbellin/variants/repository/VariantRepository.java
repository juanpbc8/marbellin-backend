package com.marbellin.variants.repository;

import com.marbellin.variants.entity.VariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<VariantEntity, Long> {
}
