package com.pixop.sdk.services.videos.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoUpload implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("uploadState")
    private final VideoUploadState uploadState;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    @JsonProperty("hasFinishedUploading")
    private final Boolean hasFinishedUploading;

    @JsonProperty("fileName")
    private final String fileName;

    @JsonProperty("fileSize")
    private final Long fileSize;

    @JsonProperty("fileType")
    private final String fileType;

    @JsonProperty("fileContainerName")
    private final String fileContainerName;

    @JsonProperty("fileTimeLastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date fileTimeLastModified;

    @JsonCreator
    public VideoUpload(@JsonProperty("uploadState")final VideoUploadState uploadState,
                       @JsonProperty("hasFinishedUploading")final Boolean hasFinishedUploading,
                       @JsonProperty("fileName")final String fileName,
                       @JsonProperty("fileSize")final Long fileSize,
                       @JsonProperty("fileType")final String fileType,
                       @JsonProperty("fileContainerName")final String fileContainerName,
                       @JsonProperty("fileTimeLastModified")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")final Date fileTimeLastModified) {
        this.uploadState = uploadState;
        this.hasFinishedUploading = hasFinishedUploading;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
        this.fileContainerName = fileContainerName;
        this.fileTimeLastModified = fileTimeLastModified;
    }

    public VideoUploadState getUploadState() {
        return uploadState;
    }

    public Boolean getHasFinishedUploading() {
        return this.hasFinishedUploading;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public String getFileType() {
        return this.fileType;
    }

    public String getFileContainerName() {
        return this.fileContainerName;
    }

    public Date getFileTimeLastModified() {
        return this.fileTimeLastModified;
    }
}
