package com.pixop.sdk.services.videos.model;
/*
 * {
 *  "size": 35769000,
 *  "codecName": "h264",
 *  "frameWidth": 853,
 *  "frameHeight": 480,
 *  "totalFrames": 241,
 *  "containerName": "matroska,webm",
 *  "longCodecName": "H.264 / AVC / MPEG-4 AVC / MPEG-4 part 10",
 *  "pixelFormatName": "yuv444p",
 *  "primaryBitDepth": 8,
 *  "videoStreamSize": 35769000,
 *  "averageFramerate": 29.97003,
 *  "durationInMillis": 7980,
 *  "longContainerName": "Matroska / WebM",
 *  "displayAspectRatio": 1.7770833333333333
 * }
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.pixop.sdk.common.util.FFmpegToSDKPixelFormatSerializer;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "containerName", "longContainerName", "codecName", "longCodecName", "pixelFormat", "primaryBitDepth", "frameWidth", "frameHeight", "displayAspectRatio", "averageFramerate", "durationInMillis", "totalFrames", "size", "videoStreamSize" })
public class VideoMetadata implements java.io.Serializable {

    private static final long serialVersionUID = 5489292996527754799L;

    private String containerName;
    private String longContainerName;
    private String codecName;
    private String longCodecName;
    private VideoPixelFormat pixelFormat;
    private String pixelFormatName;
    private Integer primaryBitDepth;
    private Integer frameWidth;
    private Integer frameHeight;
    private Double displayAspectRatio;
    private Double averageFramerate;
    private Long durationInMillis;
    private Integer totalFrames;
    private Long size;
    private Long videoStreamSize;

    public VideoMetadata() {
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(final String _containerName) {
        containerName = _containerName;
    }

    public String getLongContainerName() {
        return longContainerName;
    }

    public void setLongContainerName(final String _longContainerName) {
        longContainerName = _longContainerName;
    }

    public String getCodecName() {
        return codecName;
    }

    public void setCodecName(final String _codecName) {
        codecName = _codecName;
    }

    public String getLongCodecName() {
        return longCodecName;
    }

    public void setLongCodecName(final String _longCodecName) {
        longCodecName = _longCodecName;
    }

    public void setPixelFormat(final VideoPixelFormat _pixelFormat) {
        this.pixelFormatName = _pixelFormat.getName();
    }

    @JsonSerialize(using = FFmpegToSDKPixelFormatSerializer.class, contentAs = VideoPixelFormat.class)
    @JsonProperty("pixelFormat")
    public String getPixelFormatName() {
        return pixelFormatName;
    }

    @JsonProperty("pixelFormatName")
    public void setPixelFormatName(final String _pixelFormatName) {
        pixelFormatName = _pixelFormatName;
    }

    public Integer getPrimaryBitDepth() {
        return primaryBitDepth;
    }

    public void setPrimaryBitDepth(final Integer _primaryBitDepth) {
        primaryBitDepth = _primaryBitDepth;
    }

    public Integer getFrameWidth() {
        return frameWidth;
    }

    public void setFrameWidth(final Integer _frameWidth) {
        frameWidth = _frameWidth;
    }

    public Integer getFrameHeight() {
        return frameHeight;
    }

    public void setFrameHeight(final Integer _frameHeight) {
        frameHeight = _frameHeight;
    }

    public Double getDisplayAspectRatio() {
        return displayAspectRatio;
    }

    public void setDisplayAspectRatio(final Double _displayAspectRatio) {
        displayAspectRatio = _displayAspectRatio;
    }

    public Double getAverageFramerate() {
        return averageFramerate;
    }

    public void setAverageFramerate(final Double _averageFramerate) {
        averageFramerate = _averageFramerate;
    }

    public Long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(final Long _durationInMillis) {
        durationInMillis = _durationInMillis;
    }

    public Integer getTotalFrames() {
        return totalFrames;
    }

    public void setTotalFrames(final Integer _totalFrames) {
        totalFrames = _totalFrames;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(final Long _size) {
        size = _size;
    }

    public Long getVideoStreamSize() {
        return videoStreamSize;
    }

    public void setVideoStreamSize(final Long _videoStreamSize) {
        videoStreamSize = _videoStreamSize;
    }
}