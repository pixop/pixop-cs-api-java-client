/*
 * Created on 15-Sep-2005
 *
 * Generic HTTP Call / Fetch
 */
package com.pixop.sdk.core.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple wrapper for calls to a url
 * @author Paul Cook
 */
public final class HttpRequester {

    private static final Logger Log = LoggerFactory.getLogger(HttpRequester.class.getName());

    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_ACCEPT = "Accept";

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final ThreadLocal<ObjectMapper> OBJECT_MAPPER = new ThreadLocal<ObjectMapper>() {
        @Override
        protected ObjectMapper initialValue() {
            return new ObjectMapper();
        }
    };

    private final String userAgent;

    private final String url;
    private final HttpMethod httpMethod;
    private final Map<String, String> params;
    private final Map<String, String> postUriQueryParams;
    private final Map<String, String> headers;
    private final File[] files;
    private final Map<String, FileBody> fileParts;
    private final byte[] postBody;
    private final String postContentType;
    private final Charset postContentCharset;
    private final String acceptHeader;

    private final BodyPart[] bodyParts;

    private final boolean proxyEnabled;
    private final String proxyHost;
    private final int proxyPort;

    private final boolean forceNoPlusForSpaceInUrlEncoding;

    private final boolean allowHttpRedirectOnPost;

    private final int connectionTimeout;
    private final int soTimeout;

    private final Charset requestEncoding;

    private final boolean basicAuthRequired;
    private final String basicAuthUsername;
    private final String basicAuthPassword;

    private final Set<Integer> allowedResponseStatusCodes;

    private final OutputStream streamResponseBodyToOutputStream;

    // Results of http call ....

    private int status = -1;
    private String reason = null;
    private boolean success = false;
    private boolean done = false;
    private String response = null;
    private byte[] binaryResponse = null;
    private String mimeType;
    private Charset charset;

    private String queryString = null;

    private Header[] allResponseHeaders = null;

    // Lazilly populated on demand ...
    private Map<String, String[]> responseHeaderMap = null;

    private HttpRequester(final Builder builder) {
        this.userAgent = (builder.userAgent == null ? "-Unidentified JAVA Application-" : builder.userAgent);
        this.url = builder.url;
        this.httpMethod = builder.httpMethod;
        this.params = builder.params;
        this.postUriQueryParams = builder.postUriQueryParams;
        this.headers = builder.headers;
        this.files = builder.files;
        this.fileParts = builder.fileParts;

        if (builder.postBodyString != null) {
            if (builder.postContentCharset != null)
                this.postBody = builder.postBodyString.getBytes(builder.postContentCharset);
            else
                this.postBody = builder.postBodyString.getBytes(CHARSET_UTF8);
        } else
            this.postBody = builder.postBody;

        this.postContentType = builder.postContentType;
        this.postContentCharset = builder.postContentCharset;
        this.acceptHeader = builder.acceptHeader;

        this.bodyParts = builder.bodyParts;

        this.proxyEnabled = builder.proxyEnabled;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;

        this.forceNoPlusForSpaceInUrlEncoding = builder.forceNoPlusForSpaceInUrlEncoding;

        this.allowHttpRedirectOnPost = builder.allowHttpRedirectOnPost;

        this.connectionTimeout = builder.connectionTimeout;
        this.soTimeout = builder.soTimeout;

        this.requestEncoding = builder.requestEncoding;

        this.basicAuthRequired = builder.basicAuthRequired;
        this.basicAuthUsername = builder.basicAuthUsername;
        this.basicAuthPassword = builder.basicAuthPassword;

        this.allowedResponseStatusCodes = builder.allowedResponseStatusCodes;

        this.streamResponseBodyToOutputStream = builder.streamResponseBodyToOutputStream;
    }

    public static final class Builder {

        private final String userAgent;

        private String url;
        private HttpMethod httpMethod = HttpMethod.GET;
        private Map<String, String> params = null;
        private Map<String, String> postUriQueryParams;
        private Map<String, String> headers = null;
        private File[] files = null;
        private Map<String, FileBody> fileParts = null;
        private byte[] postBody = null;
        private String postBodyString = null;
        private String postContentType = null;
        private Charset postContentCharset = null;
        private String acceptHeader = null;

        private BodyPart[] bodyParts = null;

        private boolean proxyEnabled = false;
        private String proxyHost = null;
        private int proxyPort = -1;
        private boolean forceNoPlusForSpaceInUrlEncoding = false;

        private boolean allowHttpRedirectOnPost = false;

        private int connectionTimeout = HttpClientUtils.DEFAULT_CONNECTION_TIMEOUT;
        private int soTimeout = HttpClientUtils.DEFAULT_SO_TIMEOUT;

        private Charset requestEncoding = CHARSET_UTF8;

        private boolean basicAuthRequired = false;
        private String basicAuthUsername = null;
        private String basicAuthPassword = null;

        private Set<Integer> allowedResponseStatusCodes;

        private OutputStream streamResponseBodyToOutputStream;

        public Builder(final String userAgent) {
            this.userAgent = userAgent;
        }

        public HttpRequester build() {
            return new HttpRequester(this);
        }

        public Builder setUrl(final String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(final HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder setParams(final Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setPostUriQueryParams(final Map<String, String> postUriQueryParams) {
            this.postUriQueryParams = postUriQueryParams;
            return this;
        }

        public Builder setHeaders(final Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder setFiles(final File[] files) {
            this.files = files;
            return this;
        }

        public Builder setFileParts(final Map<String, FileBody> fileParts) {
            this.fileParts = fileParts;
            return this;
        }

        public Builder setPostBody(final byte[] postBody) {
            this.postBody = postBody;
            this.postBodyString = null;
            return this;
        }

        public Builder setPostBody(final String postBodyString) {
            this.postBody = null;
            this.postBodyString = postBodyString;
            return this;
        }

        public Builder setPostContentType(final String postContentType) {
            this.postContentType = postContentType;
            return this;
        }

        public Builder setPostContentCharset(final Charset postContentCharset) {
            this.postContentCharset = postContentCharset;
            return this;
        }

        public Builder setAcceptHeader(final String acceptHeader) {
            this.acceptHeader = acceptHeader;
            return this;
        }

        public Builder setBodyParts(final BodyPart[] bodyParts) {
            this.bodyParts = bodyParts;
            return this;
        }

        public Builder setProxyEnabled(final boolean proxyEnabled) {
            this.proxyEnabled = proxyEnabled;
            return this;
        }

        public Builder setProxyHost(final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public Builder setProxyPort(final int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public Builder forceNoPlusForSpaceInUrlEncoding() {
            this.forceNoPlusForSpaceInUrlEncoding = true;
            return this;
        }

        public Builder allowPlusForSpaceInUrlEncoding() {
            this.forceNoPlusForSpaceInUrlEncoding = false;
            return this;
        }

        public Builder allowHttpRedirectOnPost(final boolean allowHttpRedirectOnPost) {
            this.allowHttpRedirectOnPost = allowHttpRedirectOnPost;
            return this;
        }

        public Builder setConnectTimeout(final int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder setSoTimeout(final int soTimeout) {
            this.soTimeout = soTimeout;
            return this;
        }

        public Builder setRequestEncoding(final Charset requestEncoding) {
            this.requestEncoding = requestEncoding;
            return this;
        }

        public Builder setBasicAuthCredentials(final String username, final String password) {
            this.basicAuthRequired = true;
            this.basicAuthUsername = username;
            this.basicAuthPassword = password;
            return this;
        }

        public Builder setAllowedResponseStatusCode(final int status) {
            if (this.allowedResponseStatusCodes == null)
                this.allowedResponseStatusCodes = new HashSet<>();
            this.allowedResponseStatusCodes.add(status);
            return this;
        }

        public Builder setAllowedResponseStatusCodes(final int... statusCodes) {
            if (this.allowedResponseStatusCodes == null)
                this.allowedResponseStatusCodes = new HashSet<>();
            for (final int status: statusCodes)
                this.allowedResponseStatusCodes.add(status);
            return this;
        }

        public Builder setStreamResponseBodyToOutputStream(final OutputStream streamResponseBodyToOutputStream) {
            this.streamResponseBodyToOutputStream = streamResponseBodyToOutputStream;
            return this;
        }

    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean isDone() {
        return this.done;
    }

    public int getStatus() {
        return this.status;
    }

    public String getReason() {
        return this.reason;
    }

    public String getResponse() {
        return this.response;
    }

    public byte[] getBinaryResponse() {
        if (this.binaryResponse == null)
            return null;
        return this.binaryResponse.clone();
    }

    public String getUrl() {
        return this.url;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public String getResponseHeaderValue(final String headerName) {
        final String[] headers = getResponseHeader(headerName);
        if (headers == null || headers.length < 1)
            return null;
        return headers[0];
    }

    public String[] getResponseHeader(final String headerName) {
        synchronized(this) {
            if (this.responseHeaderMap == null) {
                this.responseHeaderMap = new HashMap<>();
                if (this.allResponseHeaders != null) {
                    for (final Header header: this.allResponseHeaders) {
                        if (header.getName() == null || header.getValue() == null)
                            continue;
                        String[] list = this.responseHeaderMap.get(header.getName());
                        if (list == null) {
                            list = new String[1];
                            list[0] = header.getValue();
                            this.responseHeaderMap.put(header.getName(), list);
                        } else {
                            final String[] newList = new String[list.length + 1];
                            for (int i = 0;i < list.length;i++)
                                newList[i] = list[i];
                            newList[list.length] = header.getValue();
                            this.responseHeaderMap.put(header.getName(), newList);
                        }
                    }
                }
            }
            return this.responseHeaderMap.get(headerName);
        }
    }

    public Serializable getJSONResult(final Class<? extends Serializable> responseBodyObjctClass) throws HttpException {
        final ObjectMapper objectMapper = OBJECT_MAPPER.get();

        Serializable response;
        try {
            response = objectMapper.readValue(this.response, responseBodyObjctClass);
        } catch (final IOException e) {
            throw new HttpException("Unparseable JSON :: " + this.response, e);
        }

        return response;
    }

    public boolean execute() throws ConnectionRefusedException,
                                    HttpException,
                                    TimeoutException,
                                    ConnectionTimeoutException {
        CredentialsProvider credentialsProvider = null;
        HttpClientContext httpClientContext = null;

        if (this.basicAuthRequired) {
            URI requestURI;
            try {
                requestURI = new URI(this.url);
            } catch (final URISyntaxException e) {
                throw new HttpException("Bad URL", e);
            }
            final HttpHost targetHost = URIUtils.extractHost(requestURI);

            credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                                               new UsernamePasswordCredentials(this.basicAuthUsername,
                                                                               this.basicAuthPassword));

            final AuthCache authCache = new BasicAuthCache();
            authCache.put(targetHost, new BasicScheme());

            // Add AuthCache to the execution context
            httpClientContext = HttpClientContext.create();
            httpClientContext.setCredentialsProvider(credentialsProvider);
            httpClientContext.setAuthCache(authCache);
        }

        final CloseableHttpClient client = HttpClientUtils.getInstance(this.connectionTimeout,
                                                                       this.soTimeout).getNewHttpClient(this.userAgent,
                                                                                                        credentialsProvider,
                                                                                                        this.allowHttpRedirectOnPost);

        HttpUriRequest method = null;
        List<NameValuePair> nvps = null;

        RequestConfig proxyRequestConfig = null;
        if (this.proxyEnabled && this.proxyHost != null) {
            final HttpHost proxy = new HttpHost(this.proxyHost, this.proxyPort, "http");
            proxyRequestConfig = RequestConfig.custom()
                                              .setProxy(proxy)
                                              .build();
        }

        CloseableHttpResponse httpResponse = null;
        final long timeStarted = System.currentTimeMillis();
        try {
            if (this.params != null) {
                nvps = new ArrayList<>();
                for (final Map.Entry<String, String> entry: this.params.entrySet()) {
                    final String name = entry.getKey();
                    final Object obj = entry.getValue();
                    if (obj instanceof String) {
                        final String value = (String) obj;
                        nvps.add(new BasicNameValuePair(name, value));
                    } else if (obj instanceof String[]) {
                        final String[] values = (String[]) obj;
                        for (final String value: values)
                            nvps.add(new BasicNameValuePair(name, value));
                    }
                }
            }

            if (nvps != null)
                this.queryString = URLEncodedUtils.format(nvps, this.requestEncoding);
            if (this.forceNoPlusForSpaceInUrlEncoding)
                this.queryString = this.queryString.replace("+", "%20");

            // If we want to explicitely put query params on the POST URI, then construct the query string to append here ...
            String postUriQueryString = null;
            if (this.postUriQueryParams != null && !this.postUriQueryParams.isEmpty()) {
                final List<NameValuePair> postUriNvps = new ArrayList<>();
                for (final Map.Entry<String, String> entry: this.postUriQueryParams.entrySet())
                    postUriNvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                postUriQueryString = URLEncodedUtils.format(postUriNvps, this.requestEncoding);
                if (this.forceNoPlusForSpaceInUrlEncoding)
                    postUriQueryString = postUriQueryString.replace("+", "%20");
            }

            if (this.httpMethod == HttpMethod.GET) {
                HttpGet httpGet = null;
                if (nvps != null)
                    httpGet = new HttpGet(this.url + "?" + this.queryString);
                else
                    httpGet = new HttpGet(this.url);
                method = httpGet;
                if (proxyRequestConfig != null)
                    httpGet.setConfig(proxyRequestConfig);
            } else if (this.httpMethod == HttpMethod.HEAD) {
                HttpHead httpHead = null;
                if (nvps != null)
                    httpHead = new HttpHead(this.url + "?" + this.queryString);
                else
                    httpHead = new HttpHead(this.url);
                method = httpHead;
                if (proxyRequestConfig != null)
                    httpHead.setConfig(proxyRequestConfig);
            } else if (this.httpMethod == HttpMethod.DELETE) {
                HttpDelete httpDelete = null;
                if (nvps != null)
                    httpDelete = new HttpDelete(this.url + "?" + this.queryString);
                else
                    httpDelete = new HttpDelete(this.url);
                method = httpDelete;
                if (proxyRequestConfig != null)
                    httpDelete.setConfig(proxyRequestConfig);
            } else {
                String url = this.url;
                if (postUriQueryString != null)
                    url = url + "?" + postUriQueryString;
                HttpEntityEnclosingRequestBase post = null;
                if (this.httpMethod == HttpMethod.POST)
                    post = new HttpPost(url);
                else if (this.httpMethod == HttpMethod.PUT)
                    post = new HttpPut(url);
                else if (this.httpMethod == HttpMethod.PATCH)
                    post = new HttpPatch(url);
                else
                    throw new HttpException("Unsupported method [ " + this.httpMethod + " ] ");

                if (this.files != null || this.fileParts != null) {
                    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                    // create file parts
                    if (this.files != null) {
                        for (int i=0;i<this.files.length;i++) {
                            if (!this.files[i].exists())
                                Log.error("Cannot send missing file [ " + this.files[i].getAbsolutePath() + " ] ");
                            else if (this.files[i].isDirectory())
                                Log.error("Cannot send file [ " + this.files[i].getAbsolutePath() + " ] - it is a directory");
                            else
                                entity = entity.addBinaryBody(this.files[i].getName(), this.files[i]);
                        }
                    }

                    if (this.fileParts != null)
                        for (final Map.Entry<String, FileBody> fileEntry: this.fileParts.entrySet())
                            entity = entity.addBinaryBody(fileEntry.getKey(),
                                                          fileEntry.getValue().getFile(),
                                                          fileEntry.getValue().getContentType(),
                                                          fileEntry.getValue().getFilename());
                    // create string param parts
                    if (nvps != null)
                        for (final NameValuePair nvp: nvps)
                            entity = entity.addPart(nvp.getName(), new StringBody(nvp.getValue(), ContentType.TEXT_PLAIN));

                    post.setEntity(entity.build());
                } else if (this.postBody != null) {
                    final ContentType contentType = ContentType.create(this.postContentType, this.postContentCharset);
                    final ByteArrayEntity entity = new ByteArrayEntity(this.postBody, contentType);
                    post.setEntity(entity);
                } else if (this.bodyParts != null) {
                    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
                    for (final BodyPart part: this.bodyParts)
                        entity = entity.addPart(part.getName(), part.asPart());

                    // create string param parts
                    if (nvps != null)
                        for (final NameValuePair nvp: nvps)
                            entity = entity.addPart(nvp.getName(), new StringBody(nvp.getValue(), ContentType.TEXT_PLAIN));

                    post.setEntity(entity.build());
                } else {
                    if (nvps != null)
                        post.setEntity(new UrlEncodedFormEntity(nvps, this.requestEncoding));
                }
                if (proxyRequestConfig != null)
                    post.setConfig(proxyRequestConfig);
                method = post;
            }

            if (this.headers != null) {
                for (final Map.Entry<String, String> entry: this.headers.entrySet()) {
                    final String headerName = entry.getKey();
                    final String headerValue = entry.getValue();
                    method.setHeader(headerName, headerValue);
                }
            }

            if (this.acceptHeader != null)
                method.setHeader(HEADER_ACCEPT, this.acceptHeader);

            if (this.userAgent != null)
                method.setHeader(HEADER_USER_AGENT, this.userAgent);

            if (proxyRequestConfig != null) {

            }

            /*
             * this loop is a 'sticky-plaster' solution ....
             *
             * the problem ... newer versions of http client do not test the connection before checking out from the pool
             * this leads to trying to use a stale connection (remote end disconnected) ...
             * this fails immediately ...
             *
             * the RetryHandler should be kicking in at this point and try to grab more connections from the pool until we get a new/good one
             * unfortunately, its not ... this loop attempts to do the same thing .........
             * .. we bail after 100 ms ..
             * the theory is,  if we're running concurrently enough to get that many connections in the pool, then we are concurrent enough
             * to have many threads trying to consume/discard/re-create them concurrently, so we should quickly work our way through and clean the pool
             * 100ms is there as a safety valve ... at which point,  bail out and fail the request .........
             */
            final long timeStartedTrying = System.currentTimeMillis();
            for (;;) {
                try {
                    httpResponse = client.execute(method, httpClientContext);
                    break;
                } catch (final NoHttpResponseException e) {
                    final long timeWaited = System.currentTimeMillis() - timeStartedTrying;
                    if (timeWaited > 100)
                        throw e;
                }
            }
            if (httpResponse == null)
                throw new NoHttpResponseException("No response received from destination ...");

            this.status = httpResponse.getStatusLine().getStatusCode();
            this.reason = httpResponse.getStatusLine().getReasonPhrase();

            if (Log.isDebugEnabled())
                Log.info("called url [ " + this.url + " ] ");

            final HttpEntity entity = httpResponse.getEntity();
            final ContentType responseContentType = ContentType.getOrDefault(entity);
            this.mimeType = responseContentType.getMimeType();
            this.charset = responseContentType.getCharset() == null ? this.requestEncoding : responseContentType.getCharset();
            if (entity == null) {
                this.binaryResponse = null;
                this.response = null;
            } else {
                if (this.streamResponseBodyToOutputStream != null) {

                    // pipe the response body into the supplied stream ....

                    final InputStream inputStream = entity.getContent();
                    IOUtils.copy(inputStream, this.streamResponseBodyToOutputStream);
                } else {

                    // retrieve the entire binary body

                    this.binaryResponse = EntityUtils.toByteArray(entity);
                    this.response = new String(this.binaryResponse, this.charset);
                }
            }

            this.allResponseHeaders = httpResponse.getAllHeaders();

            if (this.status >= 200 && this.status <= 205)
                this.status = 200;

            if (this.status != 200) {
                if (this.allowedResponseStatusCodes == null || !this.allowedResponseStatusCodes.contains(this.status))
                    throw new Exception("Failed to call URL [ " + this.url + " ] got response [ " + this.status + " ] [ " + this.reason + " ] ");
                this.success = false;
            } else
                this.success = true;
        } catch (final HttpHostConnectException e) {
            final long timeWaited = System.currentTimeMillis() - timeStarted;
            final String request = " [ " + this.url + " ] [ " + this.queryString + " ] ";
            throw new ConnectionRefusedException("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] :: time-waited [ " + timeWaited + " ] ", e);
        } catch (final ConnectTimeoutException e) {
            final long timeWaited = System.currentTimeMillis() - timeStarted;
            final String request = " [ " + this.url + " ] [ " + this.queryString + " ] ";
            throw new ConnectionTimeoutException("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] :: time-waited [ " + timeWaited + " ] ", e);
        } catch (final SocketTimeoutException e) {
            final long timeWaited = System.currentTimeMillis() - timeStarted;
            final String request = " [ " + this.url + " ] [ " + this.queryString + " ] ";
            throw new TimeoutException("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] :: time-waited [ " + timeWaited + " ] ", e);
        } catch (final Exception e) {
            final long timeWaited = System.currentTimeMillis() - timeStarted;
            final String request = " [ " + this.url + " ] [ " + this.queryString + " ] ";
            if (Log.isDebugEnabled())
                Log.debug("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] ::", e);
            if (e instanceof SocketException)
                if (e.getMessage().indexOf("Connection reset") >= 0)
                    throw new ConnectionRefusedException("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] :: time-waited [ " + timeWaited + " ] ", e);
            throw new HttpException("Something went horribly wrong poking the url " + request + " -- status [ " + this.status + " ] Reason [ " + this.reason + " ] :: time-waited [ " + timeWaited + " ] ", e);
        } finally {
            if (httpResponse != null)
                try {
                    httpResponse.close();
                } catch (final Exception e) {
                    Log.info("Failed to close response=" + httpResponse, e);
                }
            try {
                client.close();
            } catch (final Exception e) {
                Log.info("Failed to close http-client", e);
            }
            this.done = true;
        }
        return this.success;
    }

    public static final class BodyPart {

        private final String name;
        private final String content;
        private final byte[] binaryContent;
        private final InputStream inputStream;
        private final String contentType;
        private final Charset charset;
        private final String transferEncoding;

        public BodyPart(final String name,
                        final String content,
                        final String contentType,
                        final Charset charset,
                        final String transferEncoding) {
            this.name = name;
            this.content = content;
            this.binaryContent = null;
            this.inputStream = null;
            this.contentType = contentType;
            this.charset = charset;
            this.transferEncoding = transferEncoding;
        }

        public BodyPart(final String name,
                        final byte[] binaryContent,
                        final String contentType) {
            this.name = name;
            this.binaryContent = binaryContent;
            this.contentType = contentType;
            this.inputStream = null;
            this.content = null;
            this.charset = null;
            this.transferEncoding = null;
        }

        public BodyPart(final String name,
                        final InputStream inputStream,
                        final String contentType) {
            this.name = name;
            this.binaryContent = null;
            this.contentType = contentType;
            this.inputStream = inputStream;
            this.content = null;
            this.charset = null;
            this.transferEncoding = null;
        }

        public String getName() {
            return this.name;
        }

        public ContentBody asPart() {
            if (this.binaryContent != null)
                return new CustomByteBody(this.name,
                                          this.binaryContent,
                                          this.contentType);

            if (this.inputStream != null)
                return new CustomInputStreamBody(this.name,
                                                 this.inputStream,
                                                 this.contentType);

            return new CustomStringBody(this.content,
                                        this.contentType,
                                        this.charset,
                                        this.transferEncoding);
        }

    }

    public static final class CustomStringBody extends StringBody {

        private final String transferEncoding;

        public CustomStringBody(final String text,
                                final String mimeType,
                                final Charset charset,
                                final String transferEncoding) {
            super(text,
                  ContentType.create(mimeType, charset));
            this.transferEncoding = transferEncoding;
        }

        @Override
        public String getTransferEncoding() {
            if (this.transferEncoding == null)
                return super.getTransferEncoding();
            return this.transferEncoding;
        }

    }

    public static final class CustomByteBody extends ByteArrayBody {

        public CustomByteBody(final String name,
                              final byte[] content,
                              final String mimeType) {
            super(content,
                  ContentType.create(mimeType),
                  name);
        }

    }

    public static final class CustomInputStreamBody extends InputStreamBody {

        public CustomInputStreamBody(final String name,
                                     final InputStream inputStream,
                                     final String mimeType) {
            super(inputStream,
                  ContentType.create(mimeType),
                  name);
        }

    }

    public static final class ConnectionRefusedException extends Exception {

        private static final long serialVersionUID = 288199728738690385L;

        public ConnectionRefusedException() {
        }

        public ConnectionRefusedException(final String msg) {
            super(msg);
        }

        public ConnectionRefusedException(final Throwable t) {
            super(t);
        }

        public ConnectionRefusedException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    public static final class HttpException extends Exception {

        private static final long serialVersionUID = -8014767070279080289L;

        public HttpException() {
        }

        public HttpException(final String msg) {
            super(msg);
        }

        public HttpException(final Throwable t) {
            super(t);
        }

        public HttpException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    public static final class ConnectionTimeoutException extends Exception {

        private static final long serialVersionUID = 5330277494439674462L;

        public ConnectionTimeoutException() {
        }

        public ConnectionTimeoutException(final String msg) {
            super(msg);
        }

        public ConnectionTimeoutException(final Throwable t) {
            super(t);
        }

        public ConnectionTimeoutException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    public static final class TimeoutException extends Exception {

        private static final long serialVersionUID = -8014767070279080289L;

        public TimeoutException() {
        }

        public TimeoutException(final String msg) {
            super(msg);
        }

        public TimeoutException(final Throwable t) {
            super(t);
        }

        public TimeoutException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

}