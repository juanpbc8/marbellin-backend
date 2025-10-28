package com.marbellin.catalog.service;

import com.marbellin.catalog.dto.admin.CategoryAdminCreateRequest;
import com.marbellin.catalog.dto.admin.CategoryAdminResponse;
import com.marbellin.catalog.dto.admin.CategoryAdminUpdateRequest;
import com.marbellin.catalog.dto.web.CategoryWebResponse;

import java.util.List;

public interface CategoryService {

    // Admin
    CategoryAdminResponse create(CategoryAdminCreateRequest request);

    CategoryAdminResponse update(Long id, CategoryAdminUpdateRequest request);

    void delete(Long id);

    CategoryAdminResponse findById(Long id);

    List<CategoryAdminResponse> findAllAsTree();

    // Public Web
    List<CategoryWebResponse> findAllRootCategories();

    List<CategoryWebResponse> findSubcategoriesByParent(Long parentId);
}
