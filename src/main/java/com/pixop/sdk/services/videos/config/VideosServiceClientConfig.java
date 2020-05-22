package com.pixop.sdk.services.videos.config;
/**
 * VideosServiceClientConfig.java
 *
 *   <videos-service service-host='auth.local' service-port='3333' connection-timeout='10000' so-timeout='10000' ssl='false' user-agent='xxxx'/>
 */

/**
 * @author  Paul Cook
 * @version
 */
public class VideosServiceClientConfig implements java.io.Serializable {

    private static final long serialVersionUID = -8357049615132810333L;

    protected final String videosServiceHost;
    protected final int videosServicePort;
    protected final int videosServiceConnectTimeout;
    protected final int videosServiceSoTimeout;
    protected final boolean videosServiceSsl;
    protected final String videosServiceUserAgent;

    public VideosServiceClientConfig(final String videosServiceHost,
                                     final int videosServicePort,
                                     final int videosServiceConnectTimeout,
                                     final int videosServiceSoTimeout,
                                     final boolean videosServiceSsl,
                                     final String videosServiceUserAgent) {
        this.videosServiceHost = videosServiceHost;
        this.videosServicePort = videosServicePort;
        this.videosServiceConnectTimeout = videosServiceConnectTimeout;
        this.videosServiceSoTimeout = videosServiceSoTimeout;
        this.videosServiceSsl = videosServiceSsl;
        this.videosServiceUserAgent = videosServiceUserAgent;
    }

    public final String getVideosServiceHost() {
        return this.videosServiceHost;
    }

    public final int getVideosServicePort() {
        return this.videosServicePort;
    }

    public final int getVideosServiceConnectTimeout() {
        return this.videosServiceConnectTimeout;
    }

    public final int getVideosServiceSoTimeout() {
        return this.videosServiceSoTimeout;
    }

    public final boolean isVideosServiceSsl() {
        return this.videosServiceSsl;
    }

    public final String getVideosServiceUserAgent() {
        return this.videosServiceUserAgent;
    }

    public static final class Builder {

        private String videosServiceHost;
        private int videosServicePort;
        private int connectionTimeout = 3000;
        private int soTimeout = 15000;
        private String userAgent;
        private boolean ssl;

        public Builder() {
        }

        public Builder setVideoServiceHost(final String videosServiceHost) {
            this.videosServiceHost = videosServiceHost;
            return this;
        }

        public Builder setVideosServicePort(final int videosServicePort) {
            this.videosServicePort = videosServicePort;
            return this;
        }

        public Builder setConnectionTimeout(final int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setSoTimeout(final int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setUserAgent(final String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public Builder setSsl(final boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public VideosServiceClientConfig build() {
            return new VideosServiceClientConfig(this.videosServiceHost,
                                                 this.videosServicePort,
                                                 this.connectionTimeout,
                                                 this.soTimeout,
                                                 this.ssl,
                                                 this.userAgent);
        }

    }

}