package com.pixop.sdk.services.media.config;
/**
 *  <media-service service-host='localhost'
 *                 service-port='8020'
 *                 connection-timeout='10000'
 *                 so-timeout='10000'
 *                 user-agent='xxxxx'
 *                 ssl='false'/>
 */

/**
 * @author  Paul Cook
 * @version
 */
public class MediaServiceClientConfig implements java.io.Serializable {

    private static final long serialVersionUID = 9047115617729106717L;

    protected final String mediaServiceHost;
    protected final int mediaServicePort;
    protected final int connectionTimeout;
    protected final int soTimeout;
    protected final String userAgent;
    protected final boolean ssl;

    public MediaServiceClientConfig(final String mediaServiceHost,
                                    final int mediaServicePort,
                                    final int connectionTimeout,
                                    final int soTimeout,
                                    final String userAgent,
                                    final boolean ssl) {
        this.mediaServiceHost = mediaServiceHost;
        this.mediaServicePort = mediaServicePort;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.userAgent = userAgent;
        this.ssl = ssl;
    }

    public final String getMediaServiceHost() {
        return this.mediaServiceHost;
    }

    public final int getMediaServicePort() {
        return this.mediaServicePort;
    }

    public final int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public final int getSoTimeout() {
        return this.soTimeout;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }

    public final boolean isSsl() {
        return this.ssl;
    }

    public static final class Builder {

        private String mediaServiceHost;
        private int mediaServicePort;
        private int connectionTimeout = 3000;
        private int soTimeout = 15000;
        private String userAgent;
        private boolean ssl;

        public Builder() {
        }

        public Builder setMediaServiceHost(final String mediaServiceHost) {
            this.mediaServiceHost = mediaServiceHost;
            return this;
        }

        public Builder setMediaServicePort(final int mediaServicePort) {
            this.mediaServicePort = mediaServicePort;
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

        public MediaServiceClientConfig build() {
            return new MediaServiceClientConfig(this.mediaServiceHost,
                                                this.mediaServicePort,
                                                this.connectionTimeout,
                                                this.soTimeout,
                                                this.userAgent,
                                                this.ssl);
        }

    }

}