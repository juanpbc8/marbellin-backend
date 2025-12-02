package com.marbellin.orders.mapper;

import com.marbellin.orders.dto.OrderItemDto;
import com.marbellin.orders.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(target = "productId", expression = "java(entity.getProduct() != null ? entity.getProduct().getId() : null)")
    @Mapping(target = "productName", expression = "java(entity.getProduct() != null ? entity.getProduct().getName() : null)")
    @Mapping(target = "productImageUrl", expression = "java(entity.getProduct() != null ? entity.getProduct().getImageUrl() : null)")
    @Mapping(target = "variantSku", source = "variantSku")
    @Mapping(target = "subtotal", expression = "java(calculateSubtotal(entity))")
    OrderItemDto toDto(OrderItemEntity entity);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItemEntity toEntity(OrderItemDto dto);

    // Method Helper para el cálculo
    default BigDecimal calculateSubtotal(OrderItemEntity entity) {
        if (entity.getUnitPrice() == null || entity.getQuantity() == null) {
            return BigDecimal.ZERO;
        }
        return entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity()));
    }
}
