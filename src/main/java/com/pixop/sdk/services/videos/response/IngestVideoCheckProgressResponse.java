package com.pixop.sdk.services.videos.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pixop.sdk.services.videos.model.VideoIngestionState;
import com.pixop.sdk.services.videos.model.VideoProcessingState;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class IngestVideoCheckProgressResponse implements java.io.Serializable {

    private static final long serialVersionUID = -2194384257203798794L;

    @JsonProperty("videoId")
    private final String videoId;

    @JsonProperty("projectId")
    private final String projectId;

    @JsonProperty("ingestionState")
    private final VideoIngestionState ingestionState;

    @JsonCreator
    public IngestVideoCheckProgressResponse(@JsonProperty("videoId")final String videoId,
                                            @JsonProperty("projectId")final String projectId,
                                            @JsonProperty("processingState")final VideoIngestionState ingestionState) {
        this.videoId = videoId;
        this.projectId = projectId;
        this.ingestionState = ingestionState;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public VideoIngestionState getIngestionState() {
        return this.ingestionState;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PROCESS-VIDEO-RESPONSE :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}