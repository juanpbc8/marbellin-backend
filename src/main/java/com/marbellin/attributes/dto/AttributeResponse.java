package com.marbellin.attributes.dto;

import java.util.List;

public record AttributeResponse(
        Long id,
        String name,
        List<AttributeValueResponse> values
) {
}
