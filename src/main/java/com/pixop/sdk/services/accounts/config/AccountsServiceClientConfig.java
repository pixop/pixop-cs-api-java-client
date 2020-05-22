package com.pixop.sdk.services.accounts.config;
/**
 * AccountsServiceClientConfig.java
 *
 *   <accounts-service service-host='auth.local' service-port='3333' connection-timeout='10000' so-timeout='10000' ssl='false' user-agent='xxxx'/>
 */

/**
 * @author  Paul Cook
 * @version
 */
public class AccountsServiceClientConfig implements java.io.Serializable {

    private static final long serialVersionUID = -8357049615132810333L;

    protected final String accountsServiceHost;
    protected final int accountsServicePort;
    protected final int accountsServiceConnectTimeout;
    protected final int accountsServiceSoTimeout;
    protected final boolean accountsServiceSsl;
    protected final String accountsServiceUserAgent;

    public AccountsServiceClientConfig(final String accountsServiceHost,
                                       final int accountsServicePort,
                                       final int accountsServiceConnectTimeout,
                                       final int accountsServiceSoTimeout,
                                       final boolean accountsServiceSsl,
                                       final String accountsServiceUserAgent) {
        this.accountsServiceHost = accountsServiceHost;
        this.accountsServicePort = accountsServicePort;
        this.accountsServiceConnectTimeout = accountsServiceConnectTimeout;
        this.accountsServiceSoTimeout = accountsServiceSoTimeout;
        this.accountsServiceSsl = accountsServiceSsl;
        this.accountsServiceUserAgent = accountsServiceUserAgent;
    }

    public final String getAccountsServiceHost() {
        return this.accountsServiceHost;
    }

    public final int getAccountsServicePort() {
        return this.accountsServicePort;
    }

    public final int getAccountsServiceConnectTimeout() {
        return this.accountsServiceConnectTimeout;
    }

    public final int getAccountsServiceSoTimeout() {
        return this.accountsServiceSoTimeout;
    }

    public final boolean isAccountsServiceSsl() {
        return this.accountsServiceSsl;
    }

    public final String getAccountsServiceUserAgent() {
        return this.accountsServiceUserAgent;
    }

    public static final class Builder {

        private String accountsServiceHost;
        private int accountsServicePort;
        private int connectionTimeout = 3000;
        private int soTimeout = 15000;
        private String userAgent;
        private boolean ssl;

        public Builder() {
        }

        public Builder setAccountsServiceHost(final String accountsServiceHost) {
            this.accountsServiceHost = accountsServiceHost;
            return this;
        }

        public Builder setAccountsServicePort(final int accountsServicePort) {
            this.accountsServicePort = accountsServicePort;
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

        public AccountsServiceClientConfig build() {
            return new AccountsServiceClientConfig(this.accountsServiceHost,
                                                   this.accountsServicePort,
                                                   this.connectionTimeout,
                                                   this.soTimeout,
                                                   this.ssl,
                                                   this.userAgent);
        }

    }

}