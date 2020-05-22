package com.pixop.sdk.services.videos.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "description", "parentId", "projectId", "userId", "teamId", "upload", "ingestion", "processing", "attributes", "dateCreated", "timeLastUpdated" })
public final class Video implements java.io.Serializable {
    private static final long serialVersionUID = -8748556970473864255L;

    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("description")
    private final String description;

    @JsonProperty("parentId")
    private final String parentId;

    @JsonProperty("projectId")
    private final String projectId;

    @JsonProperty("userId")
    private final String userId;

    @JsonProperty("teamId")
    private final String teamId;

    @JsonProperty("upload")
    private final VideoUpload upload;

    @JsonProperty("ingestion")
    private final VideoIngestion ingestion;

    @JsonProperty("processing")
    private final VideoProcessing processing;

    @JsonProperty("attributes")
    private final VideoAttributes attributes;

    @JsonProperty("dateCreated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date dateCreated;

    @JsonProperty("timeLastUpdated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timeLastModified;

    @JsonCreator
    public Video(@JsonProperty("id")final String id,
                 @JsonProperty("name")final String name,
                 @JsonProperty("description")final String description,
                 @JsonProperty("parentId")final String parentId,
                 @JsonProperty("fileName")final String fileName,
                 @JsonProperty("fileSize")final Long fileSize,
                 @JsonProperty("fileType")final String fileType,
                 @JsonProperty("fileTimeLastModified")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date fileTimeLastModified,
                 @JsonProperty("hasFinishedUploading")final Boolean hasFinishedUploading,
                 @JsonProperty("uploadState")final VideoUploadState uploadState,
                 @JsonProperty("metadata")final VideoMetadata metadata,
                 @JsonProperty("projectId")final String projectId,
                 @JsonProperty("userId")final String userId,
                 @JsonProperty("teamId")final String teamId,
                 @JsonProperty("fileContainerName")final String fileContainerName,
                 @JsonProperty("ingestionState")final VideoIngestionState ingestionState,
                 @JsonProperty("processingState")final VideoProcessingState processingState,
                 @JsonProperty("qualityAssessment")final VideoQualityAssessment qualityAssessment,
                 @JsonProperty("processingParameters")final VideoProcessingParameters processingParameters,
                 @JsonProperty("attributes")final VideoAttributes attributes,
                 @JsonProperty("dateCreated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date dateCreated,
                 @JsonProperty("timeLastUpdated")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date timeLastModified) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.projectId = projectId;
        this.userId = userId;
        this.teamId = teamId;
        this.upload = new VideoUpload(uploadState, hasFinishedUploading, fileName, fileSize, fileType, fileContainerName, fileTimeLastModified);
        this.ingestion = new VideoIngestion(ingestionState, metadata, qualityAssessment);
        this.processing = new VideoProcessing(processingState, processingParameters);
        this.attributes = attributes;
        this.dateCreated = dateCreated;
        this.timeLastModified = timeLastModified;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getParentId() {
        return this.parentId;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getTeamId() {
        return this.teamId;
    }

    public VideoUpload getUpload() { return this.upload; }

    public VideoIngestion getIngestion() {
        return ingestion;
    }

    public VideoProcessing getProcessing() {
        return processing;
    }

    public VideoAttributes getAttributes() {
        return this.attributes;
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
        sb.append("VIDEO :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

}
