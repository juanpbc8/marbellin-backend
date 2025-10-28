package com.marbellin.attributes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AttributeValueRequest(
        @NotBlank
        @Size(max = 50)
        String attributeValueName
) {
}
