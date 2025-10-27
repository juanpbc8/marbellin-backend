package com.marbellin.customers.controller;

import com.marbellin.customers.dto.shared.AddressDto;
import com.marbellin.customers.dto.web.CustomerWebPatchRequest;
import com.marbellin.customers.dto.web.CustomerWebRequest;
import com.marbellin.customers.dto.web.CustomerWebResponse;
import com.marbellin.customers.service.AddressService;
import com.marbellin.customers.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/customers")
@RequiredArgsConstructor
@Tag(name = "Public - Customers", description = "Endpoints for customer actions on the public website")
public class PublicCustomerController {
    private final CustomerService customerService;
    private final AddressService addressService;

    // CUSTOMER PROFILE
    @Operation(summary = "Register a new customer (checkout or signup)")
    @PostMapping
    public ResponseEntity<CustomerWebResponse> register(
            @Valid @RequestBody CustomerWebRequest request
    ) {
        var created = customerService.registerCustomer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get customer profile by ID")
    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerWebResponse> getProfile(@PathVariable Long customerId) {
        return ResponseEntity.ok(customerService.getProfile(customerId));
    }

    @Operation(summary = "Update customer profile")
    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerWebResponse> updateProfile(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerWebRequest request
    ) {
        return ResponseEntity.ok(customerService.updateProfile(customerId, request));
    }

    @Operation(summary = "Partially update customer profile")
    @PatchMapping("/{customerId}")
    public ResponseEntity<CustomerWebResponse> patchProfile(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerWebPatchRequest request
    ) {
        return ResponseEntity.ok(customerService.patchProfile(customerId, request));
    }

    // ADDRESSES
    @Operation(summary = "Add new address to a customer")
    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<AddressDto> addAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressDto dto
    ) {
        var created = addressService.addAddress(customerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "List all addresses of a customer")
    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<AddressDto>> getAddresses(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddresses(customerId));
    }

    @Operation(summary = "Update an existing address of a customer")
    @PutMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressDto dto
    ) {
        return ResponseEntity.ok(addressService.updateAddress(customerId, addressId, dto));
    }

    @Operation(summary = "Delete an address of a customer")
    @DeleteMapping("/{customerId}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long customerId,
            @PathVariable Long addressId
    ) {
        addressService.deleteAddress(customerId, addressId);
        return ResponseEntity.noContent().build();
    }
}
