package com.pixop.sdk.services.accounts.model;

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
public final class TeamInvitation implements java.io.Serializable {

    private static final long serialVersionUID = 8563481430023125459L;

    @JsonProperty("id")
    private final String id;
    @JsonProperty("teamId")
    private final String teamId;
    @JsonProperty("userId")
    private final String userId;
    @JsonProperty("email")
    private final String email;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date dateCreated;

    @JsonProperty("timeLastUpdated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timeLastModified;

    @JsonCreator
    public TeamInvitation(@JsonProperty("id")final String id,
                          @JsonProperty("teamId")final String teamId,
                          @JsonProperty("userId")final String userId,
                          @JsonProperty("email")final String email,

                          @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                          @JsonProperty("JsonFormat")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastModified) {
        this.id = id;
        this.teamId = teamId;
        this.userId = userId;
        this.email = email;

        this.dateCreated = dateCreated;
        this.timeLastModified = timeLastModified;
    }

    public String getId() {
        return this.id;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public Date getDateCreated() {
        return this.dateCreated;
    }

    public Date getTimeLastModified() {
        return this.timeLastModified;
    }

}