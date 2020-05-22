package com.pixop.sdk.services.videos.model;
/*
 * {
 *  "sourceVideo": {
 *                  "id": "032a6c92-5b45-467e-a80a-6743b16de4dd",
 *                  "endPositionMillis": 2000,
 *                  "startPositionMillis": null
 *                  },
 *  "targetVideo": {
 *                  "id": "98e4908e-de86-467b-bfbc-e12d01cbf0ce",
 *                  "codec": {
 *                            "name": "prores_ks",
 *                            "properties": {
 *                                            "profile": "_4444"
 *                                          }
 *                            },
 *                  "bitrate": 1152000000,
 *                  "container": {
 *                                "name": "mov",
 *                                "properties": {}
 *                               },
 *                  "frameWidth": 3840,
 *                  "frameHeight": 2160,
 *                  "pixelFormat": {
 *                                  "name": "yuv444p10le"
 *                                 },
 *                  "videoFilters": [
 *                                   {"name": "pabsr1",
 *                                    "properties": {
 *                                                   "clarityBoost": "3",
 *                                                   "sourceHeight": "1080",
 *                                                   "targetHeight": "2160"
 *                                                  }
 *                                   }
 *                                  ],
 *                  "watermarkEnabled": true
 *                 }
 * }
 */

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pixop.sdk.common.util.FFmpegToSDKPixelFormatSerializer;

import java.util.List;
import java.util.Map;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoProcessingParameters implements java.io.Serializable {

    private static final long serialVersionUID = -948363748093006551L;

    private String environment;
    private SourceVideo sourceVideo;
    private TargetVideo targetVideo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class SourceVideo implements java.io.Serializable {

        private static final long serialVersionUID = 7783775807256854583L;

        private Integer startPositionMilliseconds;
        private Integer endPositionMilliseconds;

        public SourceVideo() {
        }

        public Integer getStartPositionMilliseconds() {
            return startPositionMilliseconds;
        }

        public void setStartPositionMilliseconds(final Integer _startPositionMilliseconds) {
            startPositionMilliseconds = _startPositionMilliseconds;
        }

        public Integer getEndPositionMilliseconds() {
            return endPositionMilliseconds;
        }

        public void setEndPositionMilliseconds(final Integer _endPositionMilliseconds) {
            endPositionMilliseconds = _endPositionMilliseconds;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({ "container", "codec", "pixelFormat", "bitrate", "frameWidth", "frameHeight", "averageFramerate", "videoFilters" })
    public static final class TargetVideo implements java.io.Serializable {

        private static final long serialVersionUID = 4557458049750531341L;

        private TargetVideo.Container container;
        private TargetVideo.Codec codec;
        private VideoPixelFormat pixelFormat;
        private String pixelFormatName;
        private Long bitrate;
        private Integer frameWidth;
        private Integer frameHeight;
        private Double averageFramerate;
        private List<VideoFilter> videoFilters;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Codec implements java.io.Serializable {

            private static final long serialVersionUID = -2869746224351848350L;

            private String name;

            @JsonProperty("properties")
            private Map<String, String> properties;

            public Codec() {
            }

            public Codec(final String _name, final Map<String, String> _properties) {
                name = _name;
                properties = _properties;
            }

            public String getName() {
                return this.name;
            }

            public void setName(final String name) {
                this.name = name;
            }

            public Map<String, String> getProperties() {
                return this.properties;
            }

            public void setProperties(final Map<String, String> properties) {
                this.properties = properties;
            }

        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class Container implements java.io.Serializable {

            private static final long serialVersionUID = -4458155495550274250L;

            private String name;

            @JsonProperty("properties")
            private Map<String, String> properties;

            public Container() {
            }

            public Container(final String _name, final Map<String, String> _properties) {
                name = _name;
                properties = _properties;
            }

            public String getName() {
                return this.name;
            }

            public void setName(final String name) {
                this.name = name;
            }

            public Map<String, String> getProperties() {
                return this.properties;
            }

            public void setProperties(final Map<String, String> properties) {
                this.properties = properties;
            }

        }


        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static final class VideoFilter implements java.io.Serializable {

            private static final long serialVersionUID = -4475952806674776548L;

            private String name;
            private TargetVideo.VideoFilter.Properties properties;

            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static final class Properties implements java.io.Serializable {

                private static final long serialVersionUID = 6822234752678734403L;

                private Integer targetWidth;
                private Integer targetHeight;
                private String clarityBoost;

                public Properties() {
                }

                public Integer getTargetWidth() {
                    return this.targetWidth;
                }

                public void setTargetWidth(final Integer targetWidth) {
                    this.targetWidth = targetWidth;
                }


                public Integer getTargetHeight() {
                    return this.targetHeight;
                }

                public void setTargetHeight(final Integer targetHeight) {
                    this.targetHeight = targetHeight;
                }

                public String getClarityBoost() {
                    return this.clarityBoost;
                }

                public void setClarityBoost(final String clarityBoost) {
                    this.clarityBoost = clarityBoost;
                }
            }

            public VideoFilter() {
            }

            public String getName() {
                return this.name;
            }

            public void setName(final String name) {
                this.name = name;
            }

            public TargetVideo.VideoFilter.Properties getProperties() {
                return this.properties;
            }

            public void setProperties(final TargetVideo.VideoFilter.Properties properties) {
                this.properties = properties;
            }

        }

        public TargetVideo() {
        }


        public TargetVideo.Container getContainer() {
            return this.container;
        }

        public void setContainer(final TargetVideo.Container container) {
            this.container = container;
        }

        public TargetVideo.Codec getCodec() {
            return this.codec;
        }

        public void setCodec(final TargetVideo.Codec codec) {
            this.codec = codec;
        }

        public VideoPixelFormat getPixelFormat() {
            return pixelFormat;
        }

        public void setPixelFormat(final VideoPixelFormat _pixelFormat) {
            this.pixelFormat = _pixelFormat;
            this.pixelFormatName = _pixelFormat.getName();
        }

        @JsonSerialize(using = FFmpegToSDKPixelFormatSerializer.class, contentAs = VideoPixelFormat.class)
        @JsonProperty("pixelFormat")
        public String getPixelFormatName() {
            return this.pixelFormatName;
        }

        @JsonProperty("pixelFormatName")
        public void setPixelFormatName(final String pixelFormatName) {
            this.pixelFormatName = pixelFormatName;
        }

        public Long getBitrate() {
            return this.bitrate;
        }

        public void setBitrate(final Long bitrate) {
            this.bitrate = bitrate;
        }

        public Integer getFrameWidth() {
            return this.frameWidth;
        }

        public void setFrameWidth(final Integer frameWidth) {
            this.frameWidth = frameWidth;
        }

        public Integer getFrameHeight() {
            return this.frameHeight;
        }

        public void setFrameHeight(final Integer frameHeight) {
            this.frameHeight = frameHeight;
        }

        public Double getAverageFramerate() {
            return averageFramerate;
        }

        public void setAverageFramerate(final Double _averageFramerate) {
            averageFramerate = _averageFramerate;
        }

        public List<VideoFilter> getVideoFilters() {
            return this.videoFilters;
        }

        public void setVideoFilters(final List<VideoFilter> videoFilters) {
            this.videoFilters = videoFilters;
        }
    }

    public VideoProcessingParameters() {
    }

    @JsonIgnore
    public String getEnvironment() {
        return this.environment;
    }

    public void setEnvironment(final String environment) {
        this.environment = environment;
    }

    public SourceVideo getSourceVideo() {
        return this.sourceVideo;
    }

    public void setSourceVideo(final SourceVideo sourceVideo) {
        this.sourceVideo = sourceVideo;
    }

    public TargetVideo getTargetVideo() {
        return this.targetVideo;
    }

    public void setTargetVideo(final TargetVideo targetVideo) {
        this.targetVideo = targetVideo;
    }

}