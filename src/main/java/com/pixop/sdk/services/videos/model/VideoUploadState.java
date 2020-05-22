package com.pixop.sdk.services.videos.model;
/*
 * {
 *  "updatedAt": 1579689593754,
 *  "uploadStatus": "DONE"
 * }
 */

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoUploadState implements java.io.Serializable {
    private static final long serialVersionUID = 5489292996527754799L;

    @JsonProperty("updatedAt")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    @JsonProperty("uploadStatus")
    private String uploadStatus;

    @JsonProperty("progress")
    private Double progress;

    public VideoUploadState(@JsonProperty("updatedAt")@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss") final Date _updatedAt,
                            @JsonProperty("uploadStatus") final String _uploadStatus,
                            @JsonProperty("progress") final Double _progress) {
        updatedAt = _updatedAt;
        uploadStatus = _uploadStatus;
        progress = _progress;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(final String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(final Double _progress) {
        progress = _progress;
    }
}