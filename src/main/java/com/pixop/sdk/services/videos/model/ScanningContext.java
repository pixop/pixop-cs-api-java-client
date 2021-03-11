package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScanningContext implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("scanningType")
    private ScanningType scanningType;

    @JsonProperty("interlacedFieldOrder")
    private InterlacedFieldOrder interlacedFieldOrder;

    @JsonCreator
    public ScanningContext(@JsonProperty("scanningType") final String _scanningType,
                           @JsonProperty("interlacedFieldOrder") final String _interlacedFieldOrder) {
        scanningType = ScanningType.getMatching(_scanningType);
        interlacedFieldOrder = InterlacedFieldOrder.getMatching(_interlacedFieldOrder);
    }

    public ScanningType getScanningType() {
        return scanningType;
    }

    public void setScanningType(final ScanningType _scanningType) {
        scanningType = _scanningType;
    }

    public InterlacedFieldOrder getInterlacedFieldOrder() {
        return interlacedFieldOrder;
    }

    public void setInterlacedFieldOrder(final InterlacedFieldOrder _interlacedFieldOrder) {
        interlacedFieldOrder = _interlacedFieldOrder;
    }
}
