package com.pixop.sdk.services.accounts.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pixop.sdk.services.accounts.model.User;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UpdateUserResponse implements java.io.Serializable {

    private static final long serialVersionUID = -5408277401030100053L;

    @JsonProperty("user")
    private final User user;

    @JsonCreator
    public UpdateUserResponse(@JsonProperty("user")final User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

}