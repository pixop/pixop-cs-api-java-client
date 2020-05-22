/*
 * HttpClientUtils.java
 *
 * Created on 19 March 2003, 11:20
 */

package com.pixop.sdk.core.http;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author  Paul Cook
 */
public final class HttpClientUtils {

    private static final Logger Log = LoggerFactory.getLogger(HttpClientUtils.class.getName());

    public static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
    public static final int DEFAULT_SO_TIMEOUT = 30000;

    private final static ConcurrentMap<String, HttpClientUtils> instances = new ConcurrentHashMap<>();

    private final IdleCloseThread closeThread = new IdleCloseThread();

    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    private final int connectionTimeout;
    private final int soTimeout;
    private final String proxyAddress;
    private final int proxyPort;

    private final ShutdownHook shutdownHook;

    public static HttpClientUtils getInstance(final int connectionTimeout,
                                              final int soTimeout) {
        return getInstance(connectionTimeout, soTimeout, null, 0);
    }

    public static HttpClientUtils getInstance(final int connectionTimeout,
                                              final int soTimeout,
                                              final String proxyAddress,
                                              final int proxyPort) {
        final String key = "c-" + connectionTimeout + "-so-" + soTimeout + "-proxy-addr-" + proxyAddress + "-proxy-port-" + proxyPort;
        HttpClientUtils instance = instances.get(key);
        if (instance == null) {
            instance = new HttpClientUtils(connectionTimeout,
                                           soTimeout,
                                           proxyAddress,
                                           proxyPort);
            instances.put(key, instance);
        }
        return instance;
    }

    private HttpClientUtils(final int connectionTimeout,
                            final int soTimeout) {
        this(connectionTimeout,
             soTimeout,
             null,
             0);
    }

    private HttpClientUtils(final int connectionTimeout,
                            final int soTimeout,
                            final String proxyAddress,
                            final int proxyPort) {
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.proxyAddress = proxyAddress;
        this.proxyPort = proxyPort;

        this.poolingHttpClientConnectionManager = getPoolingHttpClientConnectionManager();

        this.shutdownHook = new ShutdownHook(this);
    }

    private static PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;

        // Initialize httpclient connection manager
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(200);
        poolingHttpClientConnectionManager.setMaxTotal(5000);

        poolingHttpClientConnectionManager.setValidateAfterInactivity(30000);

        // Create socket configuration
        final SocketConfig socketConfig = SocketConfig.custom()
                                                      .setTcpNoDelay(true)
                                                      .build();
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);

        // Create message constraints
        final MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
                                                                                 .setMaxLineLength(2000)
                                                                                 .build();
        // Create connection configuration
        final ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                                                                           .setUnmappableInputAction(CodingErrorAction.IGNORE)
                                                                           .setCharset(Consts.UTF_8)
                                                                           .setMessageConstraints(messageConstraints)
                                                                           .build();
        poolingHttpClientConnectionManager.setDefaultConnectionConfig(connectionConfig);
        return poolingHttpClientConnectionManager;
    }

    /**
     * Instanciate a new HttpClient instance that uses the timeout values associated with this factory instance
     *
     * @return HttpClient a new HttpClient instance
     */
    public CloseableHttpClient getNewHttpClient(final String userAgentString,
                                                CredentialsProvider credProvider,
                                                final boolean allowHttpRedirectOnPost) {
        HttpHost proxy = null;
        if (this.proxyAddress != null && this.proxyPort > 0)
            proxy = new HttpHost(this.proxyAddress, this.proxyPort);

        final RequestConfig defaultRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT)
                                                                         .setExpectContinueEnabled(true)
                                                                         //.setStaleConnectionCheckEnabled(true)
                                                                         .setSocketTimeout(this.soTimeout)
                                                                         .setConnectTimeout(this.connectionTimeout)
                                                                         .setConnectionRequestTimeout(this.connectionTimeout)
                                                                         .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM,
                                                                                                                      AuthSchemes.DIGEST))
                                                                         .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                                                                         .setProxy(proxy)
                                                                         .build();

        // Use custom credentials provider if necessary.
        if (credProvider == null)
            credProvider = new BasicCredentialsProvider();

        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        if (allowHttpRedirectOnPost)
            redirectStrategy = new LaxRedirectStrategy();

        final CloseableHttpClient httpClient = HttpClients.custom()
                                                          .setConnectionManager(this.poolingHttpClientConnectionManager)
                                                          .setConnectionManagerShared(true)
                                                          .setDefaultCredentialsProvider(credProvider)
                                                          .setDefaultRequestConfig(defaultRequestConfig)
                                                          .setRetryHandler(new NoHttpResponseRetryHandler())
                                                          .setRedirectStrategy(redirectStrategy)
                                                          .setUserAgent(userAgentString)
                                                          .setProxy(proxy)
                                                          .build();

        return httpClient;
    }

    public static final class NoHttpResponseRetryHandler implements HttpRequestRetryHandler {

        private final long timeCreated;

        private NoHttpResponseRetryHandler() {
            this.timeCreated = System.currentTimeMillis();
        }

        @Override
        public boolean retryRequest(final IOException e,
                                    final int retryCount,
                                    final HttpContext httpCtx) {
            if (retryCount >= 100) {
                Log.warn("Maximum tries reached, pass exception back up to the consumer of the client :: http context : " + httpCtx.toString());
                return false;
            }
            if (e instanceof NoHttpResponseException) {
                final long age = System.currentTimeMillis() - this.timeCreated;
                if (age > 5000) {
                    // If there is a reasonable time expired, then we dont bother retrying, it is a real request that was submitted
                    // but the downstream didnt respond ...
                    // we are only interested in retrying immediate failures which indicate stale connections sitting in the connection pool of the http client
                    Log.warn(":: No response from server :: http context : " + httpCtx.toString());
                    return false;
                }
                Log.warn("No response from server on " + retryCount + " call ::: we will re-try");
                return true;
            }
            return false;
        }

    }

    public void closeIdleSockets() {
        this.poolingHttpClientConnectionManager.closeExpiredConnections();
        this.poolingHttpClientConnectionManager.closeIdleConnections(30, TimeUnit.SECONDS);
    }

    private static void closeIdleSocketsOnAllInstances() {
        for (final HttpClientUtils instance: instances.values())
            instance.closeIdleSockets();
    }

    public void shutdown() {
        Log.info(":::: shutting down instance [ " + this.connectionTimeout + "::" + this.soTimeout + " ] ");

        Log.info(":::: shutting down instance [ " + this.connectionTimeout + "::" + this.soTimeout + " ] :: idle connection thread");
        if (this.closeThread != null)
            this.closeThread.shutdown();
        Log.info(":::: shutting down instance [ " + this.connectionTimeout + "::" + this.soTimeout + " ] :: idle connection thread ... [ done ] ");

        Log.info(":::: shutting down instance [ " + this.connectionTimeout + "::" + this.soTimeout + " ] :: connection manager");
        try {
            this.poolingHttpClientConnectionManager.shutdown();
        } catch (final Exception e) {
            Log.error("Exception shutting down http client connection manager......", e);
        }

        Log.info(":::: shutting down instance [ " + this.connectionTimeout + "::" + this.soTimeout + " ] ... ++++++++++++++ [ done ] ");
    }

    public static void shutdownAll() {
        for (final HttpClientUtils instance: instances.values())
            instance.shutdown();
    }

    public static final class IdleCloseThread extends Thread {

        private boolean timeToQuit = false;

        public IdleCloseThread() {
            this.timeToQuit = false;
            this.setDaemon(true);
            start();
        }

        public void shutdown() {
            synchronized(this) {
                this.timeToQuit = true;
                notify();
            }
            try {
                join();
            } catch (final InterruptedException e) {
                Log.error("Exceptin waiting for thread to shutdown ....", e);
            }
        }

        @Override
        public void run() {
            for (;;) {
                synchronized(this) {
                    if (this.timeToQuit)
                        break;
                    try {
                        wait(10000);
                    } catch (final InterruptedException e) {}
                }
                closeIdleSocketsOnAllInstances();
            }
        }

    }

    public static final class ShutdownHook extends Thread {

        private final HttpClientUtils parent;

        public ShutdownHook(final HttpClientUtils parent) {
            this.parent = parent;
            try {
                Runtime.getRuntime().addShutdownHook(this);
            } catch (final Exception e) {
                Log.error("Exception registering shutdown hook ...", e);
            }
        }

        @Override
        public void run() {
            try {
                this.parent.shutdown();
            } catch (final Exception e) {
                Log.error("... exception caght during shutdown hook ....", e);
            }
        }

    }

}