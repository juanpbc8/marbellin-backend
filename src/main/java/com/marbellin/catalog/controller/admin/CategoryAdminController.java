package com.marbellin.catalog.controller.admin;

import com.marbellin.catalog.dto.admin.CategoryAdminCreateRequest;
import com.marbellin.catalog.dto.admin.CategoryAdminResponse;
import com.marbellin.catalog.dto.admin.CategoryAdminUpdateRequest;
import com.marbellin.catalog.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Tag(name = "Admin - Categories", description = "Endpoints for managing product categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @Operation(summary = "Create a new category")
    @PostMapping
    public ResponseEntity<CategoryAdminResponse> create(@Valid @RequestBody CategoryAdminCreateRequest request) {
        return ResponseEntity.ok(categoryService.create(request));
    }

    @Operation(summary = "Update an existing category (partial update)")
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryAdminResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryAdminUpdateRequest request
    ) {
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @Operation(summary = "Delete a category by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get category detail by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryAdminResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @Operation(summary = "List all categories as a hierarchical tree")
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryAdminResponse>> findAllTree() {
        return ResponseEntity.ok(categoryService.findAllAsTree());
    }
}
