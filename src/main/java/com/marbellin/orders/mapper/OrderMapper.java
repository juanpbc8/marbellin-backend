package com.marbellin.orders.mapper;

import com.marbellin.billing.mapper.InvoiceMapper;
import com.marbellin.billing.mapper.PaymentMapper;
import com.marbellin.customers.mapper.AddressMapper;
import com.marbellin.customers.mapper.CustomerMapper;
import com.marbellin.orders.dto.OrderDto;
import com.marbellin.orders.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {OrderItemMapper.class,
                CustomerMapper.class,
                AddressMapper.class,
                InvoiceMapper.class,
                PaymentMapper.class})
public interface OrderMapper {

    @Mapping(target = "status", expression = "java(entity.getStatus().name())")
    @Mapping(target = "deliveryType", expression = "java(entity.getDeliveryType().name())")
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "address", source = "shippingAddress")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "invoice", source = "invoice")
    @Mapping(target = "payments", source = "payments")
    OrderDto toDto(OrderEntity entity);
}

