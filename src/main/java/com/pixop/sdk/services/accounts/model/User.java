package com.pixop.sdk.services.accounts.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class User implements java.io.Serializable {

    private static final long serialVersionUID = 2107717767923014760L;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("email")
    private final String email;

    @JsonProperty("emailConfirmed")
    private final boolean hasConfirmedEmail;

    @JsonProperty("authSignatureMethod")
    private final String authSignatureMethod;
    @JsonProperty("authSignaturePublicKey")
    private final String authPublicKey;
    @JsonProperty("authSignaturePublicKeyId")
    private final String authPublicKeyId;

    @JsonProperty("banned")
    private final boolean banned;
    @JsonProperty("bannedReason")
    private final String bannedReason;
    @JsonProperty("bannedDate")
    private final Date bannedDate;

    @JsonProperty("apiRequestSignatureSecret")
    private final String signatureSecret;
    @JsonProperty("apiRequestSignatureMethod")
    private final String signedRequestSignatureMethod;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date dateCreated;

    @JsonProperty("timeLastUpdated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timeLastModified;

    @JsonCreator
    public User(@JsonProperty("id")final String id,
                @JsonProperty("name")final String name,
                @JsonProperty("email")final String email,
                @JsonProperty("emailConfirmed")final boolean hasConfirmedEmail,

                @JsonProperty("authSignatureMethod")final String authSignatureMethod,
                @JsonProperty("authSignaturePublicKey")final String authPublicKey,
                @JsonProperty("authSignaturePublicKeyId")final String authPublicKeyId,

                @JsonProperty("banned")final boolean banned,
                @JsonProperty("bannedReason")final String bannedReason,
                @JsonProperty("bannedDate")final Date bannedDate,

                @JsonProperty("apiRequestSignatureSecret")final String signatureSecret,
                @JsonProperty("apiRequestSignatureMethod")final String signedRequestSignatureMethod,

                @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastModified) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.hasConfirmedEmail = hasConfirmedEmail;

        this.authSignatureMethod = authSignatureMethod;
        this.authPublicKey = authPublicKey;
        this.authPublicKeyId = authPublicKeyId;

        this.banned = banned;
        this.bannedReason = bannedReason;
        this.bannedDate = bannedDate;

        this.signatureSecret = signatureSecret;
        this.signedRequestSignatureMethod = signedRequestSignatureMethod;

        this.dateCreated = dateCreated;
        this.timeLastModified = timeLastModified;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public boolean isHasConfirmedEmail() {
        return this.hasConfirmedEmail;
    }

    public String getAuthSignatureMethod() {
        return this.authSignatureMethod;
    }

    public String getAuthPublicKey() {
        return this.authPublicKey;
    }

    public String getAuthPublicKeyId() {
        return this.authPublicKeyId;
    }

    public boolean isBanned() {
        return this.banned;
    }

    public String getBannedReason() {
        return this.bannedReason;
    }

    public Date getBannedDate() {
        return this.bannedDate;
    }

    public String getSignatureSecret() {
        return this.signatureSecret;
    }

    public String getSignedRequestSignatureMethod() {
        return this.signedRequestSignatureMethod;
    }

    public Date getDateCreated() {
        return this.dateCreated == null ? null : (Date)this.dateCreated.clone();
    }

    public Date getTimeLastModified() {
        return this.timeLastModified == null ? null : (Date)this.timeLastModified.clone();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("USER :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}
