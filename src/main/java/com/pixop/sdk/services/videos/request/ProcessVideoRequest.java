package com.pixop.sdk.services.videos.request;
/*
 * ProcessVideoRequest.java
 *
 *  {
 *    "mediaContainerCodec": {
 *      "container": "mov",
 *       "codec": "h264"
 *    },
 *    "appleProresProfile": "standard",
 *    "dnxhdHrProfile": "dnxhd_sq",
 *    "chromaSubsampling": {
 *      "yuv": "4:2:2",
 *      "bits": 8
 *    },
 *    "bitrate": {
 *      "mbps": 20.5
 *    },
 *    "deinterlacer": "deint",
 *    "denoiser": "hqdn3d",
 *    "stabilizer": "dejit",
 *    "scaler": "pabsr1",
 *    "resolution": {
 *      "tag": "hd_1080p",
 *      "width": 1920,
 *      "height": 1080
 *    },
 *    "clarityBoost": "low",
 *    "range": {
 *      "startPositionMilliseconds": 0,
 *      "endPositionMilliseconds": 30000
 *    }
 *  }
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author  Paul Cook
 * @version
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProcessVideoRequest implements java.io.Serializable {

    private static final long serialVersionUID = 5250347319997998043L;

    @JsonProperty("mediaContainerCodec")
    private final MediaContainerCodec mediaContainerCodec;

    @JsonProperty("appleProresProfile")
    private final String appleProResProfile;

    @JsonProperty("dnxhdHrProfile")
    private final String dnxHdHrProfile;

    @JsonProperty("chromaSubsampling")
    private final ChromaSubsampling chromaSubsampling;

    @JsonProperty("bitrate")
    private final BitRate bitrate;

    @JsonProperty("deinterlacer")
    private final String deinterlacer;

    @JsonProperty("denoiser")
    private final String denoiser;

    @JsonProperty("stabilizer")
    private final String stabilizer;

    @JsonProperty("scaler")
    private final String scaler;

    @JsonProperty("resolution")
    private final Resolution resolution;

    @JsonProperty("clarityBoost")
    private final String clarityBoost;

    @JsonProperty("range")
    private final Range range;

    @JsonCreator
    private ProcessVideoRequest(@JsonProperty("mediaContainerCodec")final MediaContainerCodec mediaContainerCodec,
                                @JsonProperty("appleProresProfile")final String appleProResProfile,
                                @JsonProperty("dnxhdHrProfile")final String dnxHdHrProfile,
                                @JsonProperty("chroma_subsampling")final ChromaSubsampling chromaSubsampling,
                                @JsonProperty("bitrate")final BitRate bitrate,
                                @JsonProperty("deinterlacer")final String deinterlacer,
                                @JsonProperty("denoiser")final String denoiser,
                                @JsonProperty("stabilizer")final String stabilizer,
                                @JsonProperty("scaler")final String scaler,
                                @JsonProperty("resolution")final Resolution resolution,
                                @JsonProperty("clarityBoost")final String clarityBoost,
                                @JsonProperty("range")final Range range) {
        this.mediaContainerCodec = mediaContainerCodec;
        this.appleProResProfile = appleProResProfile;
        this.dnxHdHrProfile = dnxHdHrProfile;
        this.chromaSubsampling = chromaSubsampling;
        this.bitrate = bitrate;
        this.deinterlacer = deinterlacer;
        this.denoiser = denoiser;
        this.stabilizer = stabilizer;
        this.scaler = scaler;
        this.resolution = resolution;
        this.clarityBoost = clarityBoost;
        this.range = range;
    }

    public MediaContainerCodec getMediaContainerCodec() {
        return this.mediaContainerCodec;
    }

    public String getAppleProResProfile() {
        return this.appleProResProfile;
    }

    public String getDnxHdHrProfile() {
        return this.dnxHdHrProfile;
    }

    public ChromaSubsampling getChromaSubsampling() {
        return this.chromaSubsampling;
    }

    public BitRate getBitrate() {
        return this.bitrate;
    }

    public String getDeinterlacer() {
        return this.deinterlacer;
    }

    public String getDenoiser() {
        return this.denoiser;
    }

    public String getStabilizer() {
        return this.stabilizer;
    }

    public String getScaler() {
        return this.scaler;
    }

    public Resolution getResolution() {
        return this.resolution;
    }

    public String getClarityBoost() {
        return this.clarityBoost;
    }

    public Range getRange() {
        return this.range;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PROCESS-VIDEO-REQUEST :: ");
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            sb.append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this));
        } catch (final JsonProcessingException e) {
            sb.append("::: JSON EXCEPTION ::: " + e.getMessage());
        }
        return sb.toString();
    }

    public static final class Builder {

        private MediaContainerCodec mediaContainerCodec;
        private String appleProResProfile;
        private String dnxHdHrProfile;
        private ChromaSubsampling chromaSubsampling;
        private BitRate bitrate;
        private String deinterlacer;
        private String denoiser;
        private String stabilizer;
        private String scaler;
        private Resolution resolution;
        private String clarityBoost;
        private Range range;

        public ProcessVideoRequest build() {
            return new ProcessVideoRequest(this.mediaContainerCodec,
                                           this.appleProResProfile,
                                           this.dnxHdHrProfile,
                                           this.chromaSubsampling,
                                           this.bitrate,
                                           this.deinterlacer,
                                           this.denoiser,
                                           this.stabilizer,
                                           this.scaler,
                                           this.resolution,
                                           this.clarityBoost,
                                           this.range);
        }

        public Builder withMediaContainerCodec(final MediaContainerCodec mediaContainerCodec) {
            this.mediaContainerCodec = mediaContainerCodec;
            return this;
        }

        public Builder withAppleProResProfile(final APPLE_PRORES_PROFILE appleProResProfile) {
            this.appleProResProfile = appleProResProfile.name;
            return this;
        }

        public Builder withDnxHdHrProfile(final DNXHD_HR_PROFILE dnxHdHrProfile) {
            this.dnxHdHrProfile = dnxHdHrProfile.name;
            return this;
        }

        public Builder withChromaSubsampling(final ChromaSubsampling chromaSubsampling) {
            this.chromaSubsampling = chromaSubsampling;
            return this;
        }

        public Builder withBitrate(final BitRate bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        public Builder withDeinterlacer(final DE_INTERLACER deinterlacer) {
            this.deinterlacer = deinterlacer.name;
            return this;
        }

        public Builder withDenoiser(final DE_NOISER denoiser) {
            this.denoiser = denoiser.name;
            return this;
        }

        public Builder withStabilizer(final STABILIZER stabilizer) {
            this.stabilizer = stabilizer.name;
            return this;
        }

        public Builder withScaler(final SCALER scaler) {
            this.scaler = scaler.name;
            return this;
        }

        public Builder withResolution(final Resolution resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder withClarityBoost(final CLARITY_BOOST clarityBoost) {
            this.clarityBoost = clarityBoost.name;
            return this;
        }

        public Builder withRange(final Range range) {
            this.range = range;
            return this;
        }

    }

    public static final class MediaContainerCodec implements java.io.Serializable {

        private static final long serialVersionUID = 6284356393980607651L;

        @JsonProperty("container")
        private final String container;

        @JsonProperty("codec")
        private final String codec;

        public MediaContainerCodec(final CONTAINER container,
                                   final CODEC codec) {
            this.container = container.name;
            this.codec = codec.name;
        }

        @JsonCreator
        private MediaContainerCodec(@JsonProperty("container")final String container,
                                    @JsonProperty("codec")final String codec) {
            this.container = container;
            this.codec = codec;
        }

        public String getContainer() {
            return this.container;
        }

        public String getCodec() {
            return this.codec;
        }

    }

    public enum CONTAINER {

        QUICKTIME_MOV("mov", CODEC.H264_AVC,
                             CODEC.APPLE_PRORES,
                             CODEC.AVID_DNXHD_HR,
                             CODEC.H265_HEVC,
                             CODEC.MPEG_2),
        MATERIAL_EXCHANGE_FORMAT("mxf", CODEC.H264_AVC,
                                        CODEC.XDCAM,
                                        CODEC.MPEG_2,
                                        CODEC.AVID_DNXHD_HR),
        MPEG_4("mp4", CODEC.H264_AVC,
                      CODEC.H265_HEVC),
        MPEG_2("mts", CODEC.H264_AVC,
                      CODEC.H265_HEVC,
                      CODEC.MPEG_2);

        private final String name;
        private final Set<CODEC> supportedCodecs;

        private CONTAINER(final String name, final CODEC... listOfSupportedCodecs) {
            this.name = name;
            final Set<CODEC> set = new HashSet<>();
            for (final CODEC codec: listOfSupportedCodecs)
                set.add(codec);
            this.supportedCodecs = Collections.unmodifiableSet(set);
        }

        public String getName() {
            return this.name;
        }

        public Collection<CODEC> getSupportedCodecs() {
            return this.supportedCodecs;
        }

        public boolean isSupportedCodec(final CODEC codec) {
            return this.supportedCodecs.contains(codec);
        }

        public static CONTAINER getMatching(final String name) {
            if (name != null)
                for (final CONTAINER container: values())
                    if (container.name.equalsIgnoreCase(name))
                        return container;
            return null;
        }

    }

    public enum CODEC {

        H264_AVC("h264"),
        APPLE_PRORES("prores"),
        AVID_DNXHD_HR("dnxhd"),
        XDCAM("xdcam"),
        H265_HEVC("hevc"),
        MPEG_2("mpeg2");

        private final String name;

        private CODEC(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static CODEC getMatching(final String name) {
            if (name != null)
                for (final CODEC codec: values())
                    if (codec.name.equalsIgnoreCase(name))
                        return codec;
            return null;
        }

    }

    public enum APPLE_PRORES_PROFILE {

        PROFILE_PROXY("proxy"),
        PROFILE_LT("lt"),
        PROFILE_STANDARD("standard"),
        PROFILE_HQ("hq"),
        PROFILE_4444("4444"),
        PROFILE_4444_XQ("4444xq");

        private final String name;

        private APPLE_PRORES_PROFILE(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static APPLE_PRORES_PROFILE getMatching(final String name) {
            if (name != null)
                for (final APPLE_PRORES_PROFILE profile: values())
                    if (profile.name.equalsIgnoreCase(name))
                        return profile;
            return null;
        }

    }

    public enum DNXHD_HR_PROFILE {

        DNXHD_LB("dnxhd_lb", "dnxhd", "_lb"),
        DNXHD_SQ("dnxhd_sq", "dnxhd", "_sq"),
        DNXHD_HQ("dnxhd_hq", "dnxhd", "_hq"),
        DNXHD_HQX("dnxhd_hqx", "dnxhd", "_hqx"),
        DNXHD_444("dnxhd_444", "dnxhd", "_444"),

        DNXHR_LB("dnxhr_lb", "dnxhr_lb", null),
        DNXHR_SQ("dnxhr_sq", "dnxhr_sq", null),
        DNXHR_HQ("dnxhr_hq", "dnxhr_hq", null),
        DNXHR_HQX("dnxhr_hqx", "dnxhr_hqx", null),
        DNXHR_444("dnxhr_444", "dnxhr_444", null);

        private final String name;
        private final String profile;
        private final String subprofile;

        private DNXHD_HR_PROFILE(final String name,
                                 final String profile,
                                 final String subprofile) {
            this.name = name;
            this.profile = profile;
            this.subprofile = subprofile;
        }

        public String getName() {
            return this.name;
        }

        public String getProfile() {
            return profile;
        }

        public String getSubprofile() {
            return subprofile;
        }

        public static DNXHD_HR_PROFILE getMatching(final String name) {
            if (name != null)
                for (final DNXHD_HR_PROFILE profile: values())
                    if (profile.name.equalsIgnoreCase(name))
                        return profile;
            return null;
        }

    }

    public enum YUV {

        YUV_410("4:1:0"),
        YUV_411("4:1:1"),
        YUV_420("4:2:0"),
        YUV_422("4:2:2"),
        YUV_440("4:4:0"),
        YUV_444("4:4:4");

        private final String name;

        private YUV(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static YUV getMatching(final String name) {
            if (name != null)
                for (final YUV yuv: values())
                    if (yuv.name.equalsIgnoreCase(name))
                        return yuv;
            return null;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class ChromaSubsampling implements java.io.Serializable {

        private static final long serialVersionUID = 8955964101666456669L;

        public static final ChromaSubsampling YUV_410_8_BIT = new ChromaSubsampling(YUV.YUV_410, 8);

        public static final ChromaSubsampling YUV_411_8_BIT = new ChromaSubsampling(YUV.YUV_411, 8);

        public static final ChromaSubsampling YUV_420_8_BIT = new ChromaSubsampling(YUV.YUV_420, 8);
        public static final ChromaSubsampling YUV_420_10_BIT = new ChromaSubsampling(YUV.YUV_420, 10);
        public static final ChromaSubsampling YUV_420_12_BIT = new ChromaSubsampling(YUV.YUV_420, 12);
        public static final ChromaSubsampling YUV_420_14_BIT = new ChromaSubsampling(YUV.YUV_420, 14);
        public static final ChromaSubsampling YUV_420_16_BIT = new ChromaSubsampling(YUV.YUV_420, 16);

        public static final ChromaSubsampling YUV_422_8_BIT = new ChromaSubsampling(YUV.YUV_422, 8);
        public static final ChromaSubsampling YUV_422_10_BIT = new ChromaSubsampling(YUV.YUV_422, 10);
        public static final ChromaSubsampling YUV_422_12_BIT = new ChromaSubsampling(YUV.YUV_422, 12);
        public static final ChromaSubsampling YUV_422_14_BIT = new ChromaSubsampling(YUV.YUV_422, 14);
        public static final ChromaSubsampling YUV_422_16_BIT = new ChromaSubsampling(YUV.YUV_422, 16);

        public static final ChromaSubsampling YUV_440_8_BIT = new ChromaSubsampling(YUV.YUV_440, 8);
        public static final ChromaSubsampling YUV_440_10_BIT = new ChromaSubsampling(YUV.YUV_440, 10);
        public static final ChromaSubsampling YUV_440_12_BIT = new ChromaSubsampling(YUV.YUV_440, 12);

        public static final ChromaSubsampling YUV_444_8_BIT = new ChromaSubsampling(YUV.YUV_444, 8);
        public static final ChromaSubsampling YUV_444_10_BIT = new ChromaSubsampling(YUV.YUV_444, 10);
        public static final ChromaSubsampling YUV_444_12_BIT = new ChromaSubsampling(YUV.YUV_444, 12);
        public static final ChromaSubsampling YUV_444_14_BIT = new ChromaSubsampling(YUV.YUV_444, 14);
        public static final ChromaSubsampling YUV_444_16_BIT = new ChromaSubsampling(YUV.YUV_444, 16);

        @JsonProperty("yuv")
        private final String yuv;

        @JsonProperty("bits")
        private final Integer bits;

        private ChromaSubsampling(final YUV yuv,
                                  final int bits) {
            this.yuv = yuv.name;
            this.bits = bits;
        }

        @JsonCreator
        private ChromaSubsampling(@JsonProperty("yuv")final String yuv,
                                  @JsonProperty("bits")final int bits) {
            this.yuv = yuv;
            this.bits = bits;
        }

        public String getYuv() {
            return this.yuv;
        }

        public Integer getBits() {
            return this.bits;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class BitRate implements java.io.Serializable {

        private static final long serialVersionUID = 6094604107803044254L;

        @JsonProperty("mbps")
        private final Double mbps;

        public static final BitRate BITRATE_90_8 = new BitRate(90.8);
        public static final BitRate BITRATE_136_1 = new BitRate(136.1);
        public static final BitRate BITRATE_181_5 = new BitRate(181.5);
        public static final BitRate BITRATE_272_3 = new BitRate(272.3);
        public static final BitRate BITRATE_363_0 = new BitRate(363.0);

        @JsonCreator
        private BitRate(@JsonProperty("mbps")final Double mbps) {
            this.mbps = mbps;
        }

        public Double getMbps() {
            return mbps;
        }

        public static final class Builder {

            private Double mbps;

            public Builder withMbps(final double mbps) {
                this.mbps = mbps;
                return this;
            }

            public BitRate build() {
                return new BitRate(this.mbps);
            }

        }

    }

    public static enum RESOLUTION_TAG {

        RESOLUTION_HD_1080P("hd_1080p", 1920, 1080),
        RESOLUTION_4k_UHD_2160p("uhd_4k", 3840, 2160),
        RESOLUTION_8k_UHD_4320p("uhd_8k", 7680, 4320);

        private final String name;
        private final int width;
        private final int height;

        private RESOLUTION_TAG(final String name,
                               final int width,
                               final int height) {
            this.name = name;
            this.width = width;
            this.height = height;
        }

        public String getName() {
            return this.name;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public static RESOLUTION_TAG getMatching(final String name) {
            if (name != null)
                for (final RESOLUTION_TAG tag: values())
                    if (tag.name.equalsIgnoreCase(name))
                        return tag;
            return null;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Resolution implements java.io.Serializable {

        private static final long serialVersionUID = 3655565988146062739L;

        public static final Resolution RESOLUTION_HD_1080P = new Resolution(RESOLUTION_TAG.RESOLUTION_HD_1080P);
        public static final Resolution RESOLUTION_4k_UHD_2160p = new Resolution(RESOLUTION_TAG.RESOLUTION_4k_UHD_2160p);
        public static final Resolution RESOLUTION_8k_UHD_4320p = new Resolution(RESOLUTION_TAG.RESOLUTION_8k_UHD_4320p);
        public static final Resolution RESOLUTION_1706x960 = new Resolution(1706, 960);
        public static final Resolution RESOLUTION_2559x1440 = new Resolution(2559, 1440);
        public static final Resolution RESOLUTION_3412x1920 = new Resolution(3412, 1920);

        @JsonProperty("tag")
        private final String tag;

        @JsonProperty("width")
        private final Integer width;

        @JsonProperty("height")
        private final Integer height;

        public Resolution(final RESOLUTION_TAG tag) {
            this(tag.name,
                 null,
                 null);
        }

        public Resolution(final int width, final int height) {
            this(null,
                 width,
                 height);
        }

        @JsonCreator
        private Resolution(@JsonProperty("tag")final String tag,
                           @JsonProperty("width")final Integer width,
                           @JsonProperty("height")final Integer height) {
            this.tag = tag;
            this.width = width;
            this.height = height;
        }

        public String getTag() {
            return this.tag;
        }

        public Integer getWidth() {
            return this.width;
        }

        public Integer getHeight() {
            return this.height;
        }

    }

    public static enum DE_INTERLACER {

        PIXOP_DEINTERLACING("deint"),
        YADIF("yadif"),
        BWDIT("bwdif"),
        WESTON_3_FIELD("weston3f");

        private final String name;

        private DE_INTERLACER(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static DE_INTERLACER getMatching(final String name) {
            if (name != null)
                for (final DE_INTERLACER deInterlacer: values())
                    if (deInterlacer.name.equalsIgnoreCase(name))
                        return deInterlacer;
            return null;
        }

    }

    public static enum DE_NOISER {

        PIXOP_DENOSING("denoise"),
        THREE_D_DENOISER("hqdn3d");

        private final String name;

        private DE_NOISER(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static DE_NOISER getMatching(final String name) {
            if (name != null)
                for (final DE_NOISER deNoiser: values())
                    if (deNoiser.name.equalsIgnoreCase(name))
                        return deNoiser;
            return null;
        }

    }

    public static enum STABILIZER {

        PIXOP_DEJITTERING("dejit");

        private final String name;

        private STABILIZER(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static STABILIZER getMatching(final String name) {
            if (name != null)
                for (final STABILIZER stabilizer: values())
                    if (stabilizer.name.equalsIgnoreCase(name))
                        return stabilizer;
            return null;
        }

    }

    public static enum SCALER {

        PIXOP_SUPER_RESOLUTION("pabsr1"),
        BICUBIC_INTERPOLATION("scale");

        private final String name;

        private SCALER(final String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public static SCALER getMatching(final String name) {
            if (name != null)
                for (final SCALER scaler: values())
                    if (scaler.name.equalsIgnoreCase(name))
                        return scaler;
            return null;
        }

    }

    public static enum CLARITY_BOOST {

        NONE("none", 0),
        MARGIINAL("marginal", 1),
        VERY_LOW("very_low", 2),
        LOW("low", 3),
        MEDIUM("medium", 4),
        HIGH("high", 5),
        VERY_HIGH("very_high", 6);

        private final String name;
        private final int level;

        private CLARITY_BOOST(final String name, final int level) {
            this.name = name;
            this.level = level;
        }

        public String getName() {
            return this.name;
        }

        public int getLevel() {
            return this.level;
        }

        public static CLARITY_BOOST getMatching(final String name) {
            if (name != null)
                for (final CLARITY_BOOST clarityBoost: values()) {
                    if (clarityBoost.name.equalsIgnoreCase(name))
                        return clarityBoost;
                    if (name.equals(clarityBoost.level + ""))
                        return clarityBoost;
                }
            return null;
        }

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static final class Range implements java.io.Serializable {

        private static final long serialVersionUID = 7646010009938974948L;

        @JsonProperty("startPositionMilliseconds")
        private final Integer startPositionMilliseconds;

        @JsonProperty("endPositionMilliseconds")
        private final Integer endPositionMilliseconds;

        @JsonCreator
        private Range(@JsonProperty("startPositionMilliseconds")final int startPositionMilliseconds,
                      @JsonProperty("endPositionMilliseconds")final int endPositionMilliseconds) {
            this.startPositionMilliseconds = startPositionMilliseconds;
            this.endPositionMilliseconds = endPositionMilliseconds;
        }

        public Integer getStartPositionMilliseconds() {
            return this.startPositionMilliseconds;
        }

        public Integer getEndPositionMilliseconds() {
            return this.endPositionMilliseconds;
        }

        public static final class Builder {

            private Integer startPositionMilliseconds;
            private Integer endPositionMilliseconds;

            public Builder withStartPositionMilliseconds(final int startPositionMilliseconds) {
                this.startPositionMilliseconds = startPositionMilliseconds;
                return this;
            }

            public Builder withEndPositionMilliseconds(final int endPositionMilliseconds) {
                this.endPositionMilliseconds = endPositionMilliseconds;
                return this;
            }

            public Range build() {
                return new Range(this.startPositionMilliseconds,
                                 this.endPositionMilliseconds);
            }

        }

    }

}