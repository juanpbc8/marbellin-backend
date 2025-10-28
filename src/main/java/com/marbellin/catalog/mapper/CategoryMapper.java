package com.marbellin.catalog.mapper;

import com.marbellin.catalog.dto.admin.CategoryAdminCreateRequest;
import com.marbellin.catalog.dto.admin.CategoryAdminResponse;
import com.marbellin.catalog.dto.admin.CategoryAdminUpdateRequest;
import com.marbellin.catalog.dto.web.CategoryWebResponse;
import com.marbellin.catalog.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CategoryMapper {

    // Admin
    CategoryEntity toEntity(CategoryAdminCreateRequest request);

    CategoryAdminResponse toAdminResponse(CategoryEntity entity);

    List<CategoryAdminResponse> toAdminResponseList(List<CategoryEntity> entities);

    // Merge update (PATCH)
    void updateEntityFromRequest(CategoryAdminUpdateRequest request, @MappingTarget CategoryEntity entity);

    // Web
    CategoryWebResponse toWebResponse(CategoryEntity entity);

    List<CategoryWebResponse> toWebResponseList(List<CategoryEntity> entities);
}
