package com.pixop.sdk.services.accounts;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pixop.sdk.core.http.HttpMethod;
import com.pixop.sdk.core.http.HttpRequester;
import com.pixop.sdk.core.http.HttpRequester.HttpException;
import com.pixop.sdk.services.accounts.config.AccountsServiceClientConfig;
import com.pixop.sdk.services.accounts.request.UpdateAccountPasswordRequest;
import com.pixop.sdk.services.accounts.response.AuthenticatedIdentity;
import com.pixop.sdk.services.accounts.response.GetUserResponse;
import com.pixop.sdk.services.accounts.response.NewAuthToken;
import com.pixop.sdk.services.accounts.response.UpdateUserResponse;

/**
 * @author  Paul Cook
 * @version
 */
public class AccountsServiceClient {

    private static final Logger Log = LoggerFactory.getLogger(AccountsServiceClient.class.getName());

    public static final String HTTP_AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String HTTP_AUTHORIZATION_HEADER_SCHEMA = "Bearer";

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private final String remoteServiceHost;
    private final int remoteServicePort;
    private final int remoteServiceConnectTimeout;
    private final int remoteServiceSoTimeout;
    private final boolean remoteServiceSsl;
    private final String remoteServiceUserAgent;

    public AccountsServiceClient(final AccountsServiceClientConfig config) {
        this(config.getAccountsServiceHost(),
             config.getAccountsServicePort(),
             config.getAccountsServiceConnectTimeout(),
             config.getAccountsServiceSoTimeout(),
             config.isAccountsServiceSsl(),
             config.getAccountsServiceUserAgent());
    }

    public AccountsServiceClient(final String remoteServiceHost,
                                 final int remoteServicePort,
                                 final int remoteServiceConnectTimeout,
                                 final int remoteServiceSoTimeout,
                                 final boolean remoteServiceSsl,
                                 final String remoteServiceUserAgent) {
        this.remoteServiceHost = remoteServiceHost;
        this.remoteServicePort = remoteServicePort;
        this.remoteServiceConnectTimeout = remoteServiceConnectTimeout;
        this.remoteServiceSoTimeout = remoteServiceSoTimeout;
        this.remoteServiceSsl = remoteServiceSsl;
        this.remoteServiceUserAgent = remoteServiceUserAgent;
    }

    public NewAuthToken newToken(final String email,
                                 final String password,
                                 final String teamId) throws AccountsClientException {
        return doNewToken(email,
                          password,
                          teamId,
                          null); // jwtTokenString
    }

    public NewAuthToken newToken(final String jwtTokenString,
                                 final String teamId) throws AccountsClientException {
        return doNewToken(null, // email,
                          null, // password,
                          teamId,
                          jwtTokenString);
    }

    private NewAuthToken doNewToken(final String email,
                                    final String password,
                                    final String teamId,
                                    final String jwtTokenString) throws AccountsClientException {
        final Map<String, String> params = new HashMap<>();
        if (teamId != null)
            params.put("team-id", teamId);
        final NewAuthToken response = (NewAuthToken)callService(HttpMethod.GET,
                                                                "v1/token",
                                                                email,
                                                                password,
                                                                params,
                                                                null,
                                                                null,
                                                                jwtTokenString,
                                                                false,
                                                                NewAuthToken.class);
        return response;
    }

    public AuthenticatedIdentity validateToken(final HttpServletRequest request) throws AccountsClientException,
                                                                                        NoJWTTokenFoundException,
                                                                                        InvalidJWTTokenFoundException,
                                                                                        ExpiredJWTTokenException,
                                                                                        PrematureJWTTokenException,
                                                                                        EternalJWTTokenException,
                                                                                        InconsistentJWTClaimsException,
                                                                                        UserBannedException{
        final String tokenString = getJWTTokenFromRequest(request);
        if (tokenString == null)
            throw new NoJWTTokenFoundException("No JWT Auth token found in request");

        final String sourceIp = getRequestSourceIp(request);

        return validateToken(tokenString,
                             sourceIp);
    }

    public AuthenticatedIdentity validateToken(final String jwtTokenString,
                                               final String sourceIp) throws AccountsClientException,
                                                                             InvalidJWTTokenFoundException,
                                                                             ExpiredJWTTokenException,
                                                                             PrematureJWTTokenException,
                                                                             EternalJWTTokenException,
                                                                             InconsistentJWTClaimsException,
                                                                             UserBannedException,
                                                                             NoJWTTokenFoundException {
        final Map<String, String> headers = new HashMap<>();
        headers.put("X-Forwarded-For", sourceIp);

        final AuthenticatedIdentity response = (AuthenticatedIdentity)callService(HttpMethod.POST,
                                                                                  "v1/token",
                                                                                  null,
                                                                                  null,
                                                                                  null,
                                                                                  headers,
                                                                                  null,
                                                                                  jwtTokenString,
                                                                                  true,
                                                                                  AuthenticatedIdentity.class);

        if (response.getException() != null) {
            if (response.getException().equalsIgnoreCase("InvalidJWTTokenFoundException"))
                throw new InvalidJWTTokenFoundException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("ExpiredJWTTokenException"))
                throw new ExpiredJWTTokenException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("PrematureJWTTokenException"))
                throw new PrematureJWTTokenException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("EternalJWTTokenException"))
                throw new EternalJWTTokenException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("InconsistentJWTClaimsException"))
                throw new InconsistentJWTClaimsException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("UserBannedException"))
                throw new UserBannedException(response.getErrorMessage());
            if (response.getException().equalsIgnoreCase("NoJWTTokenFoundException"))
                throw new NoJWTTokenFoundException(response.getErrorMessage());
        }

        return response;
    }

    public GetUserResponse getUserAccount(final String jwtTokenString) throws AccountsClientException {
        final GetUserResponse response = (GetUserResponse)callService(HttpMethod.GET,
                                                                      "v1/account",
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      jwtTokenString,
                                                                      false,
                                                                      GetUserResponse.class);
        return response;
    }

    public UpdateUserResponse generateAuthSecret(final String jwtTokenString) throws AccountsClientException {
        final UpdateUserResponse response = (UpdateUserResponse)callService(HttpMethod.GET,
                                                                            "v1/generate-auth-secret",
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            jwtTokenString,
                                                                            false,
                                                                            UpdateUserResponse.class);
        return response;
    }

    public UpdateUserResponse generateAuthSecret(final String email, final String password) throws AccountsClientException {
        final UpdateUserResponse response = (UpdateUserResponse)callService(HttpMethod.GET,
                                                                            "v1/generate-auth-secret",
                                                                            email,
                                                                            password,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            false,
                                                                            UpdateUserResponse.class);
        return response;
    }

    public UpdateUserResponse generateSignatureSecret(final String jwtTokenString) throws AccountsClientException {
        final UpdateUserResponse response = (UpdateUserResponse)callService(HttpMethod.GET,
                                                                            "v1/generate-signature-secret",
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            jwtTokenString,
                                                                            false,
                                                                            UpdateUserResponse.class);
        return response;
    }

    public UpdateUserResponse updatePassword(final String jwtTokenString,
                                             final String newPassword) throws AccountsClientException {
        final UpdateAccountPasswordRequest request = new UpdateAccountPasswordRequest(newPassword);
        final String postBody = toJsonString(request);

        final UpdateUserResponse response = (UpdateUserResponse)callService(HttpMethod.POST,
                                                                            "v1/update-password",
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            postBody,
                                                                            jwtTokenString,
                                                                            false,
                                                                            UpdateUserResponse.class);
        return response;
    }

    private static String toJsonString(final Serializable requestObject) throws AccountsClientException {
        final ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestObject);
            return requestBody;
        } catch (final JsonProcessingException e) {
            throw new AccountsClientException("Invalid request object ...", e);
        }
    }

    private static String getJWTTokenFromRequest(final HttpServletRequest request) {
        final String authoriationHeader = request.getHeader("Authorization");
        if (authoriationHeader == null)
            return null;
        final String[] bits = authoriationHeader.split(" ");
        for (int i=0;i<bits.length;i++)
            if (bits[i].equalsIgnoreCase("Bearer") && i < bits.length - 1)
                return bits[i + 1];

        // No token found on the header ...
        return null;
    }

    private static String getRequestSourceIp(final HttpServletRequest request) {
        String ip = null;
        final String xForwardedFor = request.getHeader("x-forwarded-for");
        if (xForwardedFor != null) {
            final String[] ips = xForwardedFor.split(",");
            ip = ips[0].trim();
            if (ip.isEmpty())
                ip = null;
        }

        if (ip == null)
            ip = request.getRemoteAddr();
        if (ip == null)
            ip = "999.999.999.999";

        return ip;
    }

    private Serializable callService(final HttpMethod httpMethod,
                                     final String endpointPath,
                                     final String basicAuthUserId,
                                     final String basicAuthPassword,
                                     final Map<String, String> params,
                                     final Map<String, String> additionalHeaders,
                                     final String postBodyString,
                                     final String jwtTokenString,
                                     final boolean acceptAndInterpret401Response,
                                     final Class<? extends Serializable> responseObjectClass) throws AccountsClientException {
        final Map<String, String> headers = new HashMap<>();
        if (jwtTokenString != null)
            headers.put(HTTP_AUTHORIZATION_HEADER_NAME, HTTP_AUTHORIZATION_HEADER_SCHEMA + " " + jwtTokenString);

        final String url = (this.remoteServiceSsl ? "https://" : "http://") + this.remoteServiceHost + ":" + this.remoteServicePort + "/accounts/" + endpointPath;

        final HttpRequester.Builder builder =  new HttpRequester.Builder(this.remoteServiceUserAgent).setUrl(url)
                                                                                                     .setHeaders(headers)
                                                                                                     .setMethod(httpMethod)
                                                                                                     .setConnectTimeout(this.remoteServiceConnectTimeout)
                                                                                                     .setSoTimeout(this.remoteServiceSoTimeout);

        if (params != null)
            builder.setParams(params);

        if (postBodyString != null) {
            builder.setPostBody(postBodyString);
            builder.setPostContentType("application/json");
            builder.setPostContentCharset(CHARSET_UTF8);
        }

        if (basicAuthUserId != null)
            builder.setBasicAuthCredentials(basicAuthUserId,
                                            basicAuthPassword);

        if (acceptAndInterpret401Response)
            builder.setAllowedResponseStatusCode(401);

        final HttpRequester http = builder.build();

        try {
            http.execute();
        } catch (final Exception e) {
            throw new AccountsClientException("Failed to communicate with remote-accounts-service url [ " + url + " ] ...", e);
        }

        Log.debug(".. called remote accounts service url successfully [ " + url + " ] resp [ " + http.getResponse() + " ] ");

        if (responseObjectClass != null) {
            final Serializable response;
            try {
                response = http.getJSONResult(responseObjectClass);
            } catch (final HttpException e) {
                throw new AccountsClientException("Bad response from service :: " + http.getResponse(), e);
            }

            return response;
        }

        return null;
    }

    public static final class AccountsClientException extends Exception {

        private static final long serialVersionUID = 8026598541452444922L;

        public AccountsClientException(final String msg) {
            super(msg);
        }

        public AccountsClientException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    public static final class EternalJWTTokenException extends Exception {

        private static final long serialVersionUID = -726062929543143209L;

        public EternalJWTTokenException(final String msg) {
            super(msg);
        }

    }

    public static final class ExpiredJWTTokenException extends Exception {

        private static final long serialVersionUID = 8509503418282435504L;

        public ExpiredJWTTokenException(final String msg) {
            super(msg);
        }

    }

    public static final class InconsistentJWTClaimsException extends Exception {

        private static final long serialVersionUID = 2582228673699937825L;

        public InconsistentJWTClaimsException(final String msg) {
            super(msg);
        }

    }

    public static final class InvalidJWTTokenFoundException extends Exception {

        private static final long serialVersionUID = 1147209739091023155L;

        public InvalidJWTTokenFoundException(final String msg) {
            super(msg);
        }

    }

    public static final class NoJWTTokenFoundException extends Exception {

        private static final long serialVersionUID = -7982626543200233486L;

        public NoJWTTokenFoundException(final String msg) {
            super(msg);
        }

    }

    public static final class PrematureJWTTokenException extends Exception {

        private static final long serialVersionUID = -6519148327563635496L;

        public PrematureJWTTokenException(final String msg) {
            super(msg);
        }

    }

    public static final class UserBannedException extends Exception {

        private static final long serialVersionUID = -6614270823202700742L;

        public UserBannedException(final String msg) {
            super(msg);
        }

    }

}