package com.marbellin.customers.mapper;

import com.marbellin.customers.dto.AddressCreateDto;
import com.marbellin.customers.dto.AddressDto;
import com.marbellin.customers.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(AddressEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    AddressEntity toEntity(AddressCreateDto dto);
}

