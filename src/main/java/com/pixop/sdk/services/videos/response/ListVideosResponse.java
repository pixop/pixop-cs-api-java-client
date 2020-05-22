package com.pixop.sdk.services.videos.response;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pixop.sdk.services.videos.response.ListVideosResponse.Videos;


/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ListVideosResponse extends AbstractHALJSONResponse<Videos> {

    private static final long serialVersionUID = -6782710326042932817L;

    @JsonCreator
    public ListVideosResponse(@JsonProperty("pageSize")final int pageSize,
                              @JsonProperty("page")final int pageNumber,
                              @JsonProperty("_links")final NavigationLinks navigationLinks,
                              @JsonProperty("_embedded")final Videos contents) {
        super(pageSize,
              pageNumber,
              navigationLinks,
              contents);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("List-Videos-Response -----\n");
        sb.append(super.toString());
        sb.append("videos ---- total-found=").append(this.getContents().totalFound).append('\n');
        for (final VideoSummary video: this.getContents().getVideos())
            sb.append(video.toString()).append('\n');
        sb.append("--------------------------\n");
        return sb.toString();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Videos implements java.io.Serializable {

        private static final long serialVersionUID = -2258708694142542497L;

        private final Collection<VideoSummary> videos;
        @JsonProperty("totalFound")
        private final int totalFound;

        @JsonCreator
        public Videos(@JsonProperty("totalFound")final int totalFound,
                      @JsonProperty("videos")final Collection<VideoSummary> videos) {
            this.totalFound = totalFound;
            this.videos = videos == null ? null : Collections.unmodifiableCollection(videos);
        }

        public int getTotalFound() {
            return this.totalFound;
        }

        public Collection<VideoSummary> getVideos() {
            return this.videos;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class VideoSummary implements java.io.Serializable {

        private static final long serialVersionUID = -1344922933237768476L;

        @JsonProperty("_links")
        private final EntityLinks links;

        @JsonProperty("id")
        private final String id;
        @JsonProperty("parentId")
        private final String parentId;
        @JsonProperty("projectId")
        private final String projectId;
        @JsonProperty("name")
        private final String name;
        @JsonProperty("filename")
        private final String fileName;
        @JsonProperty("dateCreated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final Date dateCreated;
        @JsonProperty("timeLastUpdated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final Date timeLastUpdated;

        @JsonCreator
        public VideoSummary(@JsonProperty("_links")final EntityLinks links,
                            @JsonProperty("id")final String id,
                            @JsonProperty("parentId")final String parentId,
                            @JsonProperty("projectId")final String projectId,
                            @JsonProperty("name")final String name,
                            @JsonProperty("filename")final String fileName,
                            @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                            @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastUpdated) {
            this.links = links;
            this.id = id;
            this.parentId = parentId;
            this.projectId = projectId;
            this.name = name;
            this.fileName = fileName;
            this.dateCreated = dateCreated;
            this.timeLastUpdated = timeLastUpdated;
        }

        public EntityLinks getLinks() {
            return this.links;
        }

        public String getId() {
            return this.id;
        }

        public String getParentId() {
            return parentId;
        }

        public String getProjectId() {
            return projectId;
        }

        public String getName() {
            return this.name;
        }

        public String getFileName() {
            return this.fileName;
        }

        public Date getDateCreated() {
            return this.dateCreated;
        }

        public Date getTimeLastUpdated() {
            return this.timeLastUpdated;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Video-Summary :: Id=").append(this.id);
            sb.append(",parent-id=").append(this.parentId);
            sb.append(",project-id=").append(this.projectId);
            sb.append(",name=").append(this.name);
            sb.append(",file-name=").append(this.fileName);
            sb.append(",date-created=").append(this.dateCreated);
            sb.append(",time-last-updated=").append(this.timeLastUpdated);
            return sb.toString();
        }

    }

}