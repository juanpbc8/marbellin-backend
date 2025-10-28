package com.marbellin.attributes.service;

import com.marbellin.attributes.dto.AttributeRequest;
import com.marbellin.attributes.dto.AttributeResponse;

import java.util.List;

public interface AttributeService {
    AttributeResponse create(AttributeRequest request);

    List<AttributeResponse> getAll();

    AttributeResponse getById(Long id);

    AttributeResponse update(Long id, AttributeRequest request);

    void delete(Long id);
}
