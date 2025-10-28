package com.marbellin.catalog.service;

import com.marbellin.catalog.dto.admin.ProductAdminCreateRequest;
import com.marbellin.catalog.dto.admin.ProductAdminResponse;
import com.marbellin.catalog.dto.admin.ProductAdminUpdateRequest;
import com.marbellin.catalog.dto.web.ProductWebResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // Admin
    ProductAdminResponse create(ProductAdminCreateRequest request);

    ProductAdminResponse update(Long id, ProductAdminUpdateRequest request);

    void delete(Long id);

    ProductAdminResponse findById(Long id);

    Page<ProductAdminResponse> findAll(Pageable pageable, String keyword);

    // Public Web
    Page<ProductWebResponse> findAllPublic(Pageable pageable, String keyword, Long categoryId);

    ProductWebResponse findPublicById(Long id);
}
