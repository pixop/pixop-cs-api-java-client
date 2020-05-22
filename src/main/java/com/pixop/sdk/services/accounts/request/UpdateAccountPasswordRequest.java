package com.pixop.sdk.services.accounts.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author  Paul Cook
 * @version
 */
public final class UpdateAccountPasswordRequest implements java.io.Serializable {

    private static final long serialVersionUID = -5408277401030100053L;

    @JsonProperty("newPassword")
    private final String newPassword;

    public UpdateAccountPasswordRequest(final String newPassword) {
        this.newPassword = newPassword;
    }

}