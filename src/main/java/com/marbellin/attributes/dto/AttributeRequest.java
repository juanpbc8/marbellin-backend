package com.marbellin.attributes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AttributeRequest(
        @NotBlank
        @Size(max = 30)
        String name
) {
}
