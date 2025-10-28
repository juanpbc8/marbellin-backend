package com.marbellin.catalog.dto.admin;

public record ProductImageAdminResponse(
        Long id,
        String url,
        Byte position
) {
}
