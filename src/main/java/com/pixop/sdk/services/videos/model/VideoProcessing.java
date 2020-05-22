package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoProcessing implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("processingState")
    private final VideoProcessingState processingState;

    @JsonProperty("processingParameters")
    private final VideoProcessingParameters processingParameters;

    public VideoProcessing(@JsonProperty("processingState") final VideoProcessingState _processingState,
                           @JsonProperty("processingParameters") final VideoProcessingParameters _processingParameters) {
        processingState = _processingState;
        processingParameters = _processingParameters;
    }

    public VideoProcessingState getProcessingState() {
        return processingState;
    }

    public VideoProcessingParameters getProcessingParameters() {
        return processingParameters;
    }
}
