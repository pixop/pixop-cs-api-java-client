package com.pixop.sdk.services.videos.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Project implements java.io.Serializable {

    private static final long serialVersionUID = 5664452012438807135L;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("isSample")
    private final boolean isSample;

    @JsonProperty("teamId")
    private final String teamId;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date dateCreated;

    @JsonProperty("timeLastUpdated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timeLastModified;

    @JsonCreator
    public Project(@JsonProperty("id")final String id,
                   @JsonProperty("name")final String name,
                   @JsonProperty("isSample")final boolean isSample,
                   @JsonProperty("teamId")final String teamId,
                   @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                   @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastModified) {
        this.id = id;
        this.name = name;
        this.isSample = isSample;
        this.teamId = teamId;

        this.dateCreated = dateCreated;
        this.timeLastModified = timeLastModified;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public boolean getIsSample() {
        return this.isSample;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public Date getTimeLastModified() {
        return this.timeLastModified;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PROJECT :: ID=").append(this.id);
        sb.append(",NAME=").append(this.name);
        sb.append(",IS-SAMPLE=").append(this.isSample);
        sb.append(",DATE-CREATED=").append(this.dateCreated);
        sb.append(",TIME-LAST-UPDATED=").append(this.timeLastModified);
        return sb.toString();
    }

}