package com.pixop.sdk.services.accounts.response;

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
public final class AuthenticatedIdentity implements java.io.Serializable {

    private static final long serialVersionUID = 3642977742987565952L;

    @JsonProperty("user")
    private final User user;

    @JsonProperty("team")
    private final Team team;

    @JsonProperty("jwtToken")
    private final String jwtTokenString;

    @JsonProperty("exception")
    private final String exception;

    @JsonProperty("errorMessage")
    private final String errorMessage;

    @JsonCreator
    public AuthenticatedIdentity(@JsonProperty("user")final User user,
                                 @JsonProperty("team")final Team team,
                                 @JsonProperty("jwtToken")final String jwtTokenString,
                                 @JsonProperty("exception")final String exception,
                                 @JsonProperty("errorMessage")final String errorMessage) {
        this.user = user;
        this.team = team;
        this.jwtTokenString = jwtTokenString;
        this.exception = exception;
        this.errorMessage = errorMessage;
    }

    public User getUser() {
        return this.user;
    }

    public Team getTeam() {
        return this.team;
    }

    public String getJwtTokenString() {
        return this.jwtTokenString;
    }

    public String getException() {
        return this.exception;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AUTHENTICATED-IDENTITY :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}