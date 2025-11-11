package com.marbellin.billing.entity.enums;

public enum InvoiceStatus {
    EMITIDO,             // emitido correctamente
    ANULADO,             // anulado/comunicado a SUNAT
    PENDIENTE_ENVIO  // pendiente de envío a SUNAT
}
