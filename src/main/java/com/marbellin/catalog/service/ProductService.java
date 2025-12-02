package com.marbellin.catalog.service;

import com.marbellin.catalog.dto.ProductCreateDto;
import com.marbellin.catalog.dto.ProductDto;
import com.marbellin.catalog.dto.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto create(ProductCreateDto dto);

    ProductDto findById(Long id);

    Page<ProductDto> findAll(String search, String status, Long categoryId, Pageable pageable);

    ProductDto update(Long id, ProductUpdateDto dto);

    // NUEVO: Eliminar físicamente una variante específica (limpieza de errores)
    void deleteVariant(Long variantId);
}
