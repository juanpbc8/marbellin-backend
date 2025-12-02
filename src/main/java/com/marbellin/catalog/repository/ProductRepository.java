package com.marbellin.catalog.repository;

import com.marbellin.catalog.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("""
            SELECT DISTINCT p FROM ProductEntity p
            LEFT JOIN p.variants v
            WHERE (
                :search IS NULL OR :search = '' OR
                LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR
                LOWER(v.sku) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            AND (COALESCE(:status, '') = '' OR p.status = :status)
            AND (:categoryId IS NULL OR p.category.id = :categoryId)
            """)
    Page<ProductEntity> findByFilters(
            @Param("search") String search,
            @Param("status") String status,
            @Param("categoryId") Long categoryId,
            Pageable pageable
    );
}
