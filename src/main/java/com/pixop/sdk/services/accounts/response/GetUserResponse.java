package com.pixop.sdk.services.accounts.response;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pixop.sdk.services.accounts.model.Team;
import com.pixop.sdk.services.accounts.model.User;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class GetUserResponse implements java.io.Serializable {

    private static final long serialVersionUID = -5408277401030100053L;

    @JsonProperty("user")
    private final User user;

    @JsonProperty("team")
    private final Team team;

    @JsonProperty("otherTeams")
    private final Collection<Team> otherTeams;

    @JsonCreator
    public GetUserResponse(@JsonProperty("user")final User user,
                           @JsonProperty("team")final Team team,
                           @JsonProperty("otherTeams")final Collection<Team> otherTeams) {
        this.user = user;
        this.team = team;
        this.otherTeams = otherTeams;
    }

    public User getUser() {
        return this.user;
    }

    public Team getTeam() {
        return this.team;
    }

    public Collection<Team> getOtherTeams() {
        return this.otherTeams;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("GET-USER :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}