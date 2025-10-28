package com.marbellin.catalog.mapper;

import com.marbellin.catalog.dto.admin.ProductImageAdminRequest;
import com.marbellin.catalog.dto.admin.ProductImageAdminResponse;
import com.marbellin.catalog.dto.web.ProductImageWebResponse;
import com.marbellin.catalog.entity.ProductImageEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    // Admin
    ProductImageEntity toEntity(ProductImageAdminRequest request);

    ProductImageAdminResponse toAdminResponse(ProductImageEntity entity);

    List<ProductImageAdminResponse> toAdminResponseList(List<ProductImageEntity> entities);

    // Web
    ProductImageWebResponse toWebResponse(ProductImageEntity entity);

    List<ProductImageWebResponse> toWebResponseList(List<ProductImageEntity> entities);
}
