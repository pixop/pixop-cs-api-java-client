package com.pixop.sdk.services.videos.response;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DeleteVideoResponse implements java.io.Serializable {

    private static final long serialVersionUID = 736120891908484104L;

    @JsonProperty("deletedVideos")
    private final Collection<DeletedVideoItem> deletedVideos;

    @JsonCreator
    public DeleteVideoResponse(@JsonProperty("deletedVideos")final Collection<DeletedVideoItem> deletedVideos) {
        this.deletedVideos = deletedVideos == null ? null : Collections.unmodifiableCollection(deletedVideos);
    }

    public Collection<DeletedVideoItem> getDeletedVideos() {
        return this.deletedVideos;
    }

    public static final class DeletedVideoItem implements java.io.Serializable {

        private static final long serialVersionUID = 199455804090015971L;

        @JsonProperty("videoId")
        private final String videoId;

        @JsonCreator
        public DeletedVideoItem(@JsonProperty("videoId")final String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return this.videoId;
        }

    }

}