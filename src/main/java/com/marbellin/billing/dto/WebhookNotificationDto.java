package com.marbellin.billing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WebhookNotificationDto(
        Data data,
        String type,
        String action,
        @JsonProperty("date_created")
        String dateCreated
) {
    public record Data(
            String id
    ) {
    }
}
