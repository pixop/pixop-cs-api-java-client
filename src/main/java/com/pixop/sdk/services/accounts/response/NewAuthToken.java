package com.pixop.sdk.services.accounts.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class NewAuthToken implements java.io.Serializable {

    private static final long serialVersionUID = -6607408557359679644L;

    @JsonProperty("userId")
    private final String userId;

    @JsonProperty("teamId")
    private final String teamId;

    @JsonProperty("jwtToken")
    private final String jwtTokenString;

    @JsonCreator
    public NewAuthToken(@JsonProperty("userId")final String userId,
                        @JsonProperty("teamId")final String teamId,
                        @JsonProperty("jwtToken")final String jwtTokenString) {
        this.userId = userId;
        this.teamId = teamId;
        this.jwtTokenString = jwtTokenString;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public String getJwtTokenString() {
        return this.jwtTokenString;
    }

}