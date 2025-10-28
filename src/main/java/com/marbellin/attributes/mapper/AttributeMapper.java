package com.marbellin.attributes.mapper;

import com.marbellin.attributes.dto.AttributeRequest;
import com.marbellin.attributes.dto.AttributeResponse;
import com.marbellin.attributes.entity.AttributeEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = AttributeValueMapper.class)
public interface AttributeMapper {

    AttributeEntity toEntity(AttributeRequest dto);

    //    @Mapping(target = "values", source = "values")
    AttributeResponse toResponse(AttributeEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AttributeRequest dto, @MappingTarget AttributeEntity entity);
}
