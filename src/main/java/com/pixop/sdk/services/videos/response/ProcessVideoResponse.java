package com.pixop.sdk.services.videos.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Paul Cook
 * @version
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProcessVideoResponse implements java.io.Serializable {

    private static final long serialVersionUID = -2194384257203798794L;

    @JsonProperty("sourceVideoId")
    private final String sourceVideoId;

    @JsonProperty("processedVideoId")
    private final String processedVideoId;

    @JsonProperty("projectId")
    private final String projectId;

    @JsonProperty("checkProgressUrl")
    private final String checkProgressUrl;

    @JsonProperty("downloadMediaUrl")
    private final String downloadMediaUrl;

    @JsonCreator
    public ProcessVideoResponse(@JsonProperty("sourceVideoId")final String sourceVideoId,
                                @JsonProperty("processedVideoId")final String processedVideoId,
                                @JsonProperty("projectId")final String projectId,
                                @JsonProperty("checkProgressUrl")final String checkProgressUrl,
                                @JsonProperty("downloadMediaUrl")final String downloadMediaUrl) {
        this.sourceVideoId = sourceVideoId;
        this.processedVideoId = processedVideoId;
        this.projectId = projectId;
        this.checkProgressUrl = checkProgressUrl;
        this.downloadMediaUrl = downloadMediaUrl;
    }

    public String getSourceVideoId() {
        return this.sourceVideoId;
    }

    public String getProcessedVideoId() {
        return this.processedVideoId;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getCheckProgressUrl() {
        return this.checkProgressUrl;
    }

    public String getDownloadMediaUrl() {
        return this.downloadMediaUrl;
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