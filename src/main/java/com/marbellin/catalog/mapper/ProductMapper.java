package com.marbellin.catalog.mapper;

import com.marbellin.catalog.dto.*;
import com.marbellin.catalog.entity.ProductEntity;
import com.marbellin.catalog.entity.ProductVariantEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {

    // --- LECTURA (Entity -> DTO) ---
    @Mapping(target = "variants", source = "variants")
    ProductDto toDto(ProductEntity entity);

    // Method auxiliar para convertir una variante entidad a DTO
    ProductVariantDto toVariantDto(ProductVariantEntity entity);

    // --- CREACIÓN (DTO -> Entity) ---
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "variants", source = "variants")
    // MapStruct usará el method de abajo
    ProductEntity toEntity(ProductCreateDto dto);

    // Method auxiliar para convertir variante DTO a Entidad
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    // Se setea en el servicio
    ProductVariantEntity toVariantEntity(ProductVariantCreateDto dto);


    // --- ACTUALIZACIÓN (DTO -> Entity) ---
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // Se gestiona en el servicio
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "variants", ignore = true)
    // IGNORAMOS variantes en el update simple
    void updateEntityFromDto(ProductUpdateDto dto, @MappingTarget ProductEntity entity);
}
