package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pixop.sdk.common.util.ISODateToSDKDateSerializer;

/*
 * {
 *  "version": 14,
 *  "updatedAt": "2020-01-22T10:46:53.173Z",
 *  "etaSeconds": 0,
 *  "ingestionStatus": "DONE",
 *  "webVideoAvailable": true,
 *  "thumbnailsAvailable": true,
 *  "fullFramesAvailable": true,
 *  "videoMetadataAvailable": true,
 *  "videoMetadataCommitted": true,
 *  "processingProgressPercent": 100
 * }
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoIngestionState implements java.io.Serializable {

    private static final long serialVersionUID = 5489292996527754799L;

    private Integer version;
    private String updatedAt;
    private Integer etaSeconds;
    private String ingestionStatus;
    private Boolean webVideoAvailable;
    private Boolean thumbnailsAvailable;
    private Boolean fullFramesAvailable;
    private Boolean videoMetadataAvailable;
    private Boolean videoMetadataCommitted;
    private Integer processingProgressPercent;

    public VideoIngestionState() {
    }

    @JsonIgnore
    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(final Integer version) {
        this.version = version;
    }

    @JsonSerialize(using = ISODateToSDKDateSerializer.class)
    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Integer getEtaSeconds() {
        return this.etaSeconds;
    }

    public void setEtaSeconds(final Integer etaSeconds) {
        this.etaSeconds = etaSeconds;
    }

    public String getIngestionStatus() {
        return this.ingestionStatus;
    }

    public void setIngestionStatus(final String ingestionStatus) {
        this.ingestionStatus = ingestionStatus;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Boolean isWebVideoAvailable() {
        return this.webVideoAvailable;
    }

    public void setWebVideoAvailable(final Boolean webVideoAvailable) {
        this.webVideoAvailable = webVideoAvailable;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Boolean isThumbnailsAvailable() {
        return this.thumbnailsAvailable;
    }

    public void setThumbnailsAvailable(final Boolean thumbnailsAvailable) {
        this.thumbnailsAvailable = thumbnailsAvailable;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Boolean isFullFramesAvailable() {
        return this.fullFramesAvailable;
    }

    public void setFullFramesAvailable(final Boolean fullFramesAvailable) {
        this.fullFramesAvailable = fullFramesAvailable;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Boolean isVideoMetadataAvailable() {
        return this.videoMetadataAvailable;
    }

    public void setVideoMetadataAvailable(final Boolean videoMetadataAvailable) {
        this.videoMetadataAvailable = videoMetadataAvailable;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Boolean isVideoMetadataCommitted() {
        return this.videoMetadataCommitted;
    }

    public void setVideoMetadataCommitted(final Boolean videoMetadataCommitted) {
        this.videoMetadataCommitted = videoMetadataCommitted;
    }

    public Integer getProcessingProgressPercent() {
        return this.processingProgressPercent;
    }

    public void setProcessingProgressPercent(final Integer processingProgressPercent) {
        this.processingProgressPercent = processingProgressPercent;
    }

}