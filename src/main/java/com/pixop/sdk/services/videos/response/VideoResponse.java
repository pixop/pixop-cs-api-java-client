package com.pixop.sdk.services.videos.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pixop.sdk.services.videos.model.Video;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class VideoResponse implements java.io.Serializable {

    private static final long serialVersionUID = -3172775298124225488L;

    @JsonProperty("video")
    private final Video video;

    @JsonCreator
    public VideoResponse(@JsonProperty("video")final Video video) {
        this.video = video;
    }

    public Video getVideo() {
        return this.video;
    }

    @Override
    public String toString() {
        return "VIDEO-RESPONSE ::: " + this.video.toString();
    }

}