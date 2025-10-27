package com.marbellin.customers.dto.admin;

import com.marbellin.customers.dto.shared.AddressDto;
import com.marbellin.customers.entity.enums.CustomerType;
import com.marbellin.customers.entity.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.List;

public record CustomerAdminResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        DocumentType documentType,
        String documentNumber,
        CustomerType customerType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<AddressDto> addresses
) {
}
