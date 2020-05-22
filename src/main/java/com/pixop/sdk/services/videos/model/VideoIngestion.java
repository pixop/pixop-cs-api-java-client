package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoIngestion implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ingestionState")
    private final VideoIngestionState ingestionState;

    @JsonProperty("metadata")
    private final VideoMetadata metadata;

    @JsonProperty("qualityAssessment")
    private final VideoQualityAssessment qualityAssessment;

    public VideoIngestion(@JsonProperty("ingestionState") final VideoIngestionState _ingestionState,
                          @JsonProperty("metadata") final VideoMetadata _metadata,
                          @JsonProperty("qualityAssessment") final VideoQualityAssessment _qualityAssessment) {
        ingestionState = _ingestionState;
        metadata = _metadata;
        qualityAssessment = _qualityAssessment;
    }

    public VideoIngestionState getIngestionState() {
        return ingestionState;
    }

    public VideoMetadata getMetadata() {
        return metadata;
    }

    public VideoQualityAssessment getQualityAssessment() {
        return qualityAssessment;
    }
}
