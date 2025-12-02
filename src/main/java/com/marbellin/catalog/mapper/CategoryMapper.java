package com.marbellin.catalog.mapper;

import com.marbellin.catalog.dto.CategoryCreateDto;
import com.marbellin.catalog.dto.CategoryDto;
import com.marbellin.catalog.dto.CategoryUpdateDto;
import com.marbellin.catalog.entity.CategoryEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    CategoryDto toDto(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "products", ignore = true)
    CategoryEntity toEntity(CategoryCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(CategoryUpdateDto dto, @MappingTarget CategoryEntity entity);
}
