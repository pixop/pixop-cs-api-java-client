package com.pixop.sdk.services.videos.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.pixop.sdk.services.videos.model.Project;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProjectResponse implements java.io.Serializable {

    private static final long serialVersionUID = 3357560256078975898L;

    @JsonProperty("project")
    private final Project project;

    @JsonCreator
    public ProjectResponse(@JsonProperty("project")final Project project) {
        this.project = project;
    }

    public Project getProject() {
        return this.project;
    }

    @Override
    public String toString() {
        return "PROJECT-RESPONSE ::: " + this.project.toString();
    }

}