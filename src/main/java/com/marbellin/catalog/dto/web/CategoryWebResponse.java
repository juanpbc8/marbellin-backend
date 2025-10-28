package com.marbellin.catalog.dto.web;

import java.util.List;

public record CategoryWebResponse(
        Long id,
        String name,
        List<CategoryWebResponse> subCategories
) {
}
