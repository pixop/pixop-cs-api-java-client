package com.pixop.sdk.services.media.response;

import java.util.Collection;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class DeleteMediaResponse implements java.io.Serializable {

    private static final long serialVersionUID = 736120891908484104L;

    @JsonProperty("deletedVideos")
    private final Collection<DeletedMediaItem> deletedVideos;

    @JsonCreator
    public DeleteMediaResponse(@JsonProperty("deletedVideos")final Collection<DeletedMediaItem> deletedVideos) {
        this.deletedVideos = deletedVideos == null ? null : Collections.unmodifiableCollection(deletedVideos);
    }

    public Collection<DeletedMediaItem> getDeletedVideos() {
        return this.deletedVideos;
    }

    public static final class DeletedMediaItem implements java.io.Serializable {

        private static final long serialVersionUID = 199455804090015971L;

        @JsonProperty("videoId")
        private final String videoId;

        @JsonCreator
        public DeletedMediaItem(@JsonProperty("videoId")final String videoId) {
            this.videoId = videoId;
        }

        public String getVideoId() {
            return this.videoId;
        }

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DELETE-MEDIA-RESPONSE :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}