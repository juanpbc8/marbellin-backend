package com.marbellin.billing.mapper;

import com.marbellin.billing.dto.InvoiceDto;
import com.marbellin.billing.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    @Mapping(target = "type", expression = "java(entity.getType().name())")
    @Mapping(target = "currency", expression = "java(entity.getCurrency().name())")
    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    InvoiceDto toDto(InvoiceEntity entity);
}

