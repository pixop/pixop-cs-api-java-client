package com.pixop.sdk.services.videos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoPixelFormat implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChromaSubsampling implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private String yuv;
        private int bits;

        public ChromaSubsampling() {
        }

        public ChromaSubsampling(final String yuv,
                                 final int bits) {
            this.yuv = yuv;
            this.bits = bits;
        }

        public String getYuv() {
            return this.yuv;
        }

        public int getBits() {
            return this.bits;
        }
    }

    private String name;
    private ChromaSubsampling chromaSubsampling;

    public VideoPixelFormat(@JsonProperty("name") final String name,
                            @JsonProperty("chromaSubsampling") final ChromaSubsampling chromaSubsampling) {
        this.name = name;
        this.chromaSubsampling = chromaSubsampling;
    }

    public String getName() {
        return name;
    }

    public ChromaSubsampling getChromaSubsampling() {
        return chromaSubsampling;
    }
}
