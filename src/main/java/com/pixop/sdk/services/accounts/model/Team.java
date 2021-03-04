package com.pixop.sdk.services.accounts.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Team implements java.io.Serializable {

    private static final long serialVersionUID = 7319300086291496950L;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date dateCreated;

    @JsonProperty("timeLastUpdated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timeLastModified;

    @JsonCreator
    public Team(@JsonProperty("id")final String id,
                @JsonProperty("name")final String name,
                @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastModified) {
        this.id = id;
        this.name = name;
        this.dateCreated = dateCreated;
        this.timeLastModified = timeLastModified;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
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
        sb.append("TEAM :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}