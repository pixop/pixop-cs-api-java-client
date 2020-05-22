package com.pixop.sdk.services.videos.model;
/*
 * {
 *  "version": 7,
 *  "updatedAt": "2020-01-22T10:45:18.693Z",
 *  "etaSeconds": 0,
 *  "processingStatus": "DONE",
 *  "processingProgressPercent": 100
 * }
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.pixop.sdk.common.util.ISODateToSDKDateSerializer;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoProcessingState implements java.io.Serializable {

    private static final long serialVersionUID = 5489292996527754799L;

    private Integer version;
    private String updatedAt;
    private Integer etaSeconds;
    private String processingStatus;
    private Double processingProgressPercent;

    public VideoProcessingState() {
    }

    @JsonIgnore
    public final Integer getVersion() {
        return this.version;
    }

    public final void setVersion(final Integer version) {
        this.version = version;
    }

    @JsonSerialize(using = ISODateToSDKDateSerializer.class)
    public final String getUpdatedAt() {
        return this.updatedAt;
    }

    public final void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public final Integer getEtaSeconds() {
        return this.etaSeconds;
    }

    public final void setEtaSeconds(final Integer etaSeconds) {
        this.etaSeconds = etaSeconds;
    }

    public final String getProcessingStatus() {
        return this.processingStatus;
    }

    public final void setProcessingStatus(final String processingStatus) {
        this.processingStatus = processingStatus;
    }

    public final Double getProcessingProgressPercent() {
        return this.processingProgressPercent;
    }

    public final void setProcessingProgressPercent(final Double processingProgressPercent) {
        this.processingProgressPercent = processingProgressPercent;
    }

}