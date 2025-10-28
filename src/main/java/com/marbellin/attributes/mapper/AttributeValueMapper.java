package com.marbellin.attributes.mapper;

import com.marbellin.attributes.dto.AttributeValueRequest;
import com.marbellin.attributes.dto.AttributeValueResponse;
import com.marbellin.attributes.entity.AttributeValueEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

    AttributeValueEntity toEntity(AttributeValueRequest dto);

    AttributeValueResponse toResponse(AttributeValueEntity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AttributeValueRequest dto, @MappingTarget AttributeValueEntity entity);
}
