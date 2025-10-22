package com.marbellin.attributes.repository;

import com.marbellin.attributes.entity.AttributeValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeValueRepository extends JpaRepository<AttributeValueEntity, Integer> {
}
