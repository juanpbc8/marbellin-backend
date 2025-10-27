package com.marbellin.customers.service.impl;

import com.marbellin.common.exception.ConflictException;
import com.marbellin.common.exception.ResourceNotFoundException;
import com.marbellin.customers.dto.admin.CustomerAdminPatchRequest;
import com.marbellin.customers.dto.admin.CustomerAdminRequest;
import com.marbellin.customers.dto.admin.CustomerAdminResponse;
import com.marbellin.customers.dto.web.CustomerWebPatchRequest;
import com.marbellin.customers.dto.web.CustomerWebRequest;
import com.marbellin.customers.dto.web.CustomerWebResponse;
import com.marbellin.customers.entity.CustomerEntity;
import com.marbellin.customers.entity.enums.CustomerType;
import com.marbellin.customers.mapper.CustomerMapper;
import com.marbellin.customers.repository.CustomerRepository;
import com.marbellin.customers.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    // WEB CONTEXT (public site)
    @Override
    public CustomerWebResponse registerCustomer(CustomerWebRequest request) {
        // Validaciones de unicidad
        if (customerRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already in use: " + request.email());
        }
        if (customerRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new ConflictException("Document already in use: " + request.documentNumber());
        }

        CustomerEntity entity = customerMapper.toEntity(request);
        // Flujo web todo cliente es persona natural:
        if (entity.getCustomerType() == null) {
            entity.setCustomerType(CustomerType.NATURAL);
        }
        entity = customerRepository.save(entity);
        return customerMapper.toWebResponse(entity);
    }

    @Override
    public CustomerWebResponse getProfile(Long customerId) {
        CustomerEntity entity = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + customerId));
        return customerMapper.toWebResponse(entity);
    }

    @Override
    public CustomerWebResponse updateProfile(Long customerId, CustomerWebRequest request) {
        CustomerEntity entity = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + customerId));

        // Unicidad de email si cambió
        if (!entity.getEmail().equalsIgnoreCase(request.email())
                && customerRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already in use: " + request.email());
        }

        // Unicidad de documento si cambió
        if (!entity.getDocumentNumber().equalsIgnoreCase(request.documentNumber())
                && customerRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new ConflictException("Document already in use: " + request.documentNumber());
        }

        // MapStruct: puedes crear un método update-from-request si prefieres (patcher).
        customerMapper.updateFromWebRequest(request, entity);

        return customerMapper.toWebResponse(entity);
    }

    @Override
    public CustomerWebResponse patchProfile(Long id, CustomerWebPatchRequest request) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + id));
        customerMapper.updateFromWebPatch(request, entity);
        return customerMapper.toWebResponse(entity);
    }

    // ADMIN
    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<CustomerAdminResponse> getAllCustomers(Pageable pageable) {
        Page<CustomerEntity> page = customerRepository.findAll(pageable);
        return customerMapper.toAdminResponseList(page.getContent());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public CustomerAdminResponse getCustomerById(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + id));
        return customerMapper.toAdminResponse(entity);
    }

    @Override
    public CustomerAdminResponse createCustomer(CustomerAdminRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email already in use: " + request.email());
        }
        if (customerRepository.existsByDocumentNumber(request.documentNumber())) {
            throw new ConflictException("Document already in use: " + request.documentNumber());
        }

        CustomerEntity entity = customerMapper.toEntity(request);
        entity = customerRepository.save(entity);
        return customerMapper.toAdminResponse(entity);
    }

    @Override
    public CustomerAdminResponse updateCustomer(Long id, CustomerAdminRequest request) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + id));

        // Validar unicidad excluyendo este id
        if (customerRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("Email already in use: " + request.email());
        }
        if (customerRepository.existsByDocumentNumberAndIdNot(request.documentNumber(), id)) {
            throw new ConflictException("Document already in use: " + request.documentNumber());
        }

        customerMapper.updateFromAdminRequest(request, entity);
        return customerMapper.toAdminResponse(entity);
    }

    @Override
    public CustomerAdminResponse patchCustomer(Long id, CustomerAdminPatchRequest request) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + id));
        customerMapper.updateFromAdminPatch(request, entity);
        return customerMapper.toAdminResponse(entity);
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: id=" + id));
        customerRepository.delete(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<CustomerAdminResponse> searchCustomers(String query) {
        // Elegimos una paginación “sana” si luego quieres sobrecargar con Pageable en el controller.
        Page<CustomerEntity> page = customerRepository.search(query == null ? "" : query.trim(), Pageable.ofSize(20));
        return customerMapper.toAdminResponseList(page.getContent());
    }
}
