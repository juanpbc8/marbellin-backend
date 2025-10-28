package com.marbellin.attributes.service;

import com.marbellin.attributes.dto.AttributeValueRequest;
import com.marbellin.attributes.dto.AttributeValueResponse;

import java.util.List;

public interface AttributeValueService {
    AttributeValueResponse create(Long attributeId, AttributeValueRequest request);

    AttributeValueResponse update(Long id, AttributeValueRequest request);

    void delete(Long id);

    List<AttributeValueResponse> getByAttributeId(Long attributeId);
}
