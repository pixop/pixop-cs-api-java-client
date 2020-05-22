package com.pixop.sdk.services.videos.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class AddVideoResponse implements java.io.Serializable {

    private static final long serialVersionUID = 9177186021859756942L;

    @JsonProperty("projectId")
    private final String projectId;

    @JsonProperty("videoId")
    private final String videoId;

    @JsonProperty("uploadMediaUrl")
    private final String uploadMediaServiceEndpointUrl;

    @JsonProperty("uploadLightMediaUrl")
    private final String uploadLightMediaServiceEndpointUrl;

    @JsonProperty("processVideoUrl")
    private final String processVideoServiceEndpointUrl;

    @JsonCreator
    public AddVideoResponse(@JsonProperty("projectId")final String projectId,
                            @JsonProperty("videoId")final String videoId,
                            @JsonProperty("uploadMediaUrl")final String uploadMediaServiceEndpointUrl,
                            @JsonProperty("uploadLightMediaUrl")final String uploadLightMediaServiceEndpointUrl,
                            @JsonProperty("processVideoUrl")final String processVideoServiceEndpointUrl) {
        this.projectId = projectId;
        this.videoId = videoId;
        this.uploadMediaServiceEndpointUrl = uploadMediaServiceEndpointUrl;
        this.uploadLightMediaServiceEndpointUrl = uploadLightMediaServiceEndpointUrl;
        this.processVideoServiceEndpointUrl = processVideoServiceEndpointUrl;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getVideoId() {
        return this.videoId;
    }

    public String getUploadMediaServiceEndpointUrl() {
        return this.uploadMediaServiceEndpointUrl;
    }

    public String getUploadLightMediaServiceEndpointUrl() {
        return this.uploadLightMediaServiceEndpointUrl;
    }

    public String getProcessVideoServiceEndpointUrl() {
        return this.processVideoServiceEndpointUrl;
    }

}