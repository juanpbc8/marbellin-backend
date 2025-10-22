package com.marbellin.attributes.repository;

import com.marbellin.attributes.entity.AttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttributeRepository extends JpaRepository<AttributeEntity, Integer> {
}
