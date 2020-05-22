package com.pixop.sdk.services.media.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UploadMediaResponse implements java.io.Serializable {

    private static final long serialVersionUID = 9177186021859756942L;

    @JsonProperty("projectId")
    private final String projectId;

    @JsonProperty("videoId")
    private final String videoId;

    @JsonProperty("checkIngestProgressUrl")
    private final String checkIngestProgressServiceEndpointUrl;

    @JsonProperty("processVideoUrl")
    private final String processVideoServiceEndpointUrl;

    @JsonCreator
    public UploadMediaResponse(@JsonProperty("projectId")final String projectId,
                               @JsonProperty("videoId")final String videoId,
                               @JsonProperty("checkIngestProgressUrl")final String checkIngestProgressServiceEndpointUrl,
                               @JsonProperty("processVideoUrl")final String processVideoServiceEndpointUrl) {
        this.projectId = projectId;
        this.videoId = videoId;
        this.checkIngestProgressServiceEndpointUrl = checkIngestProgressServiceEndpointUrl;
        this.processVideoServiceEndpointUrl = processVideoServiceEndpointUrl;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public String getCheckIngestProgressServiceEndpointUrl() {
        return this.checkIngestProgressServiceEndpointUrl;
    }

    public String getProcessVideoServiceEndpointUrl() {
        return this.processVideoServiceEndpointUrl;
    }

}