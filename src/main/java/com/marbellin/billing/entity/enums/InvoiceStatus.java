package com.marbellin.billing.entity.enums;

public enum InvoiceStatus {
    ISSUED,             // emitido correctamente
    VOIDED,             // anulado/comunicado a SUNAT
    PENDING_SUBMISSION  // pendiente de envío a SUNAT
}
