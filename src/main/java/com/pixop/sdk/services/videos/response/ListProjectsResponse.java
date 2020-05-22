package com.pixop.sdk.services.videos.response;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pixop.sdk.services.videos.response.ListProjectsResponse.Projects;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ListProjectsResponse extends AbstractHALJSONResponse<Projects> {

    private static final long serialVersionUID = -6782710326042932817L;

    @JsonCreator
    public ListProjectsResponse(@JsonProperty("pageSize")final int pageSize,
                                @JsonProperty("page")final int pageNumber,
                                @JsonProperty("_links")final NavigationLinks navigationLinks,
                                @JsonProperty("_embedded")final Projects contents) {
        super(pageSize,
              pageNumber,
              navigationLinks,
              contents);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("List-Projects-Response -----\n");
        sb.append(super.toString());
        sb.append("projects ---- total-found=").append(this.getContents().totalFound).append('\n');
        for (final ProjectSummary project: this.getContents().getProjects())
            sb.append(project.toString()).append('\n');
        sb.append("--------------------------\n");
        return sb.toString();
    }

    public static final class Projects implements java.io.Serializable {

        private static final long serialVersionUID = -2258708694142542497L;

        private final Collection<ProjectSummary> projects;
        @JsonProperty("totalFound")
        private final int totalFound;

        @JsonCreator
        public Projects(@JsonProperty("totalFound")final int totalFound,
                        @JsonProperty("projects")final Collection<ProjectSummary> projects) {
            this.totalFound = totalFound;
            this.projects = projects == null ? null : Collections.unmodifiableCollection(projects);
        }

        public int getTotalFound() {
            return this.totalFound;
        }

        public Collection<ProjectSummary> getProjects() {
            return this.projects;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ProjectSummary implements java.io.Serializable {

        private static final long serialVersionUID = -1344922933237768476L;

        @JsonProperty("_links")
        private final EntityLinks links;

        @JsonProperty("id")
        private final String id;
        @JsonProperty("name")
        private final String name;
        @JsonProperty("isSample")
        private final boolean sample;
        @JsonProperty("dateCreated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final Date dateCreated;
        @JsonProperty("timeLastUpdated")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private final Date timeLastUpdated;

        @JsonCreator
        public ProjectSummary(@JsonProperty("_links")final EntityLinks links,
                              @JsonProperty("id")final String id,
                              @JsonProperty("name")final String name,
                              @JsonProperty("isSample")final boolean sample,
                              @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                              @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastUpdated) {
            this.links = links;
            this.id = id;
            this.name = name;
            this.sample = sample;
            this.dateCreated = dateCreated;
            this.timeLastUpdated = timeLastUpdated;
        }

        public EntityLinks getLinks() {
            return this.links;
        }

        public String getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public boolean isSample() {
            return this.sample;
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
            sb.append("Project-Summary :: Id=").append(this.id);
            sb.append(",Name=").append(this.name);
            sb.append(", is-sample=").append(this.sample);
            sb.append(",date-created=").append(this.dateCreated);
            sb.append(",time-last-updated=").append(this.timeLastUpdated);
            return sb.toString();
        }

    }

}