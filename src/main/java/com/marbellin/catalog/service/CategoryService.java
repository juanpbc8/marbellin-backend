package com.marbellin.catalog.service;

import com.marbellin.catalog.dto.CategoryCreateDto;
import com.marbellin.catalog.dto.CategoryDto;
import com.marbellin.catalog.dto.CategoryUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Long parentId, Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto create(CategoryCreateDto dto);

    CategoryDto update(Long id, CategoryUpdateDto dto);
}
