package com.pixop.sdk.services.videos;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pixop.sdk.core.http.HttpMethod;
import com.pixop.sdk.core.http.HttpRequester;
import com.pixop.sdk.core.http.HttpRequester.HttpException;
import com.pixop.sdk.services.videos.config.VideosServiceClientConfig;
import com.pixop.sdk.services.videos.iterator.ListProjectsIterator;
import com.pixop.sdk.services.videos.iterator.ListVideosIterator;
import com.pixop.sdk.services.videos.request.PaginationParameters;
import com.pixop.sdk.services.videos.request.ProcessVideoRequest;
import com.pixop.sdk.services.videos.response.AddVideoResponse;
import com.pixop.sdk.services.videos.response.DeleteVideoResponse;
import com.pixop.sdk.services.videos.response.ListProjectsResponse;
import com.pixop.sdk.services.videos.response.ListVideosResponse;
import com.pixop.sdk.services.videos.response.ProcessVideoCheckProgressResponse;
import com.pixop.sdk.services.videos.response.ProcessVideoResponse;
import com.pixop.sdk.services.videos.response.ProjectResponse;
import com.pixop.sdk.services.videos.response.VideoResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class VideosServiceClient {

    private static final Logger Log = LoggerFactory.getLogger(VideosServiceClient.class.getName());

    public static final String HTTP_AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String HTTP_AUTHORIZATION_HEADER_SCHEMA = "Bearer";

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private final String remoteServiceHost;
    private final int remoteServicePort;
    private final int remoteServiceConnectTimeout;
    private final int remoteServiceSoTimeout;
    private final boolean remoteServiceSsl;
    private final String remoteServiceUserAgent;

    public VideosServiceClient(final VideosServiceClientConfig config) {
        this(config.getVideosServiceHost(),
             config.getVideosServicePort(),
             config.getVideosServiceConnectTimeout(),
             config.getVideosServiceSoTimeout(),
             config.isVideosServiceSsl(),
             config.getVideosServiceUserAgent());
    }

    public VideosServiceClient(final String remoteServiceHost,
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

    public ListProjectsResponse listProjects(final String jwtTokenString) throws VideosClientException {
        return listProjects(jwtTokenString,
                            null); // paginationParameters
    }

    public ListProjectsResponse listProjects(final String jwtTokenString,
                                             final PaginationParameters paginationParameters) throws VideosClientException {
        final ListProjectsResponse response = (ListProjectsResponse)callService(HttpMethod.GET,
                                                                                "v1/projects",
                                                                                paginationParameters == null ? null : paginationParameters.toQueryParametersMap(),
                                                                                null,
                                                                                jwtTokenString,
                                                                                ListProjectsResponse.class);
        return response;
    }

    public ListProjectsIterator iterateProjects(final String jwtTokenString) {
        return new ListProjectsIterator(this,
                                        jwtTokenString);
    }

    public ListVideosResponse listVideos(final String jwtTokenString) throws VideosClientException {
        return listVideos(jwtTokenString,
                          null); // paginationParameters
    }

    public ListVideosResponse listVideos(final String jwtTokenString,
                                         final PaginationParameters paginationParameters) throws VideosClientException {
        final ListVideosResponse response = (ListVideosResponse)callService(HttpMethod.GET,
                                                                            "v1/videos",
                                                                            paginationParameters == null ? null : paginationParameters.toQueryParametersMap(),
                                                                            null,
                                                                            jwtTokenString,
                                                                            ListVideosResponse.class);
        return response;
    }

    public ListVideosResponse listVideosInProject(final String projectId,
                                                  final String jwtTokenString) throws VideosClientException {
        return listVideosInProject(projectId,
                                   jwtTokenString,
                                   null); // paginationParameters
    }

    public ListVideosResponse listVideosInProject(final String projectId,
                                                  final String jwtTokenString,
                                                  final PaginationParameters paginationParameters) throws VideosClientException {
        final Map<String, String> params = new HashMap<>();
        params.put("project-id", projectId);
        if (paginationParameters != null)
            paginationParameters.toQueryParameters(params);
        final ListVideosResponse response = (ListVideosResponse)callService(HttpMethod.GET,
                                                                            "v1/videos",
                                                                            params,
                                                                            null,
                                                                            jwtTokenString,
                                                                            ListVideosResponse.class);
        return response;
    }

    public ListVideosIterator iterateVideos(final String jwtTokenString) {
        return new ListVideosIterator(this,
                                      jwtTokenString,
                                      null); // projectId
    }

    public ListVideosIterator iterateVideosInProject(final String jwtTokenString,
                                                     final String projectId) {
        return new ListVideosIterator(this,
                                      jwtTokenString,
                                      projectId);
    }

    public ProjectResponse getProject(final String projectId,
                                      final String jwtTokenString) throws VideosClientException {
        final ProjectResponse response = (ProjectResponse)callService(HttpMethod.GET,
                                                                      "v1/project/" + projectId,
                                                                      null,
                                                                      null,
                                                                      jwtTokenString,
                                                                      ProjectResponse.class);
        return response;
    }

    public VideoResponse getVideo(final String videoId,
                                  final String jwtTokenString) throws VideosClientException {
        final VideoResponse response = (VideoResponse)callService(HttpMethod.GET,
                                                            "v1/video/" + videoId,
                                                            null,
                                                            null,
                                                            jwtTokenString,
                                                            VideoResponse.class);
        return response;
    }

    public ProjectResponse addProject(final String name,
                                      final String jwtTokenString) throws VideosClientException {
        final AddProjectRequest addProjectRequest = new AddProjectRequest(name);
        final String requestBody = toJsonString(addProjectRequest);
        final ProjectResponse response = (ProjectResponse)callService(HttpMethod.PUT,
                                                                      "v1/project",
                                                                      null,
                                                                      requestBody,
                                                                      jwtTokenString,
                                                                      ProjectResponse.class);
        return response;
    }

    public ProjectResponse updateProject(final String projectId,
                                         final String name,
                                         final String jwtTokenString) throws VideosClientException {
        final UpdateProjectRequest updateProjectRequest = new UpdateProjectRequest(name);
        final String requestBody = toJsonString(updateProjectRequest);
        final ProjectResponse response = (ProjectResponse)callService(HttpMethod.POST,
                                                                      "v1/project/" + projectId,
                                                                      null,
                                                                      requestBody,
                                                                      jwtTokenString,
                                                                      ProjectResponse.class);
        return response;
    }

    public void deleteProject(final String projectId,
                              final String jwtTokenString) throws VideosClientException {
        callService(HttpMethod.DELETE,
                    "v1/project/" + projectId,
                    null,
                    null,
                    jwtTokenString,
                    null);
    }

    public AddVideoResponse addVideo(final String name,
                                     final String projectId,
                                     final String jwtTokenString) throws VideosClientException {
        final AddVideoRequest addVideoRequest = new AddVideoRequest(name,
                                                                    projectId);
        final String requestBody = toJsonString(addVideoRequest);
        final AddVideoResponse response = (AddVideoResponse)callService(HttpMethod.PUT,
                                                                        "v1/video",
                                                                        null,
                                                                        requestBody,
                                                                        jwtTokenString,
                                                                        AddVideoResponse.class);
        return response;
    }

    public ProcessVideoResponse processVideo(final String videoId,
                                             final ProcessVideoRequest processVideoRequest,
                                             final String jwtTokenString) throws VideosClientException {
        final String requestBody = toJsonString(processVideoRequest);
        final ProcessVideoResponse response = (ProcessVideoResponse)callService(HttpMethod.POST,
                                                                                "v1/process-video/" + videoId,
                                                                                null,
                                                                                requestBody,
                                                                                jwtTokenString,
                                                                                ProcessVideoResponse.class);
        return response;
    }

    public ProcessVideoCheckProgressResponse processVideoCheckProgress(final String videoId,
                                                                       final String jwtTokenString) throws VideosClientException {
        final ProcessVideoCheckProgressResponse response;
        response = (ProcessVideoCheckProgressResponse)callService(HttpMethod.GET,
                                                                  "v1/check-process-video-progress/" + videoId,
                                                                  null,
                                                                  null,
                                                                  jwtTokenString,
                                                                  ProcessVideoCheckProgressResponse.class);
        return response;
    }

    public DeleteVideoResponse deleteVideo(final String videoId,
                                           final String jwtTokenString) throws VideosClientException {
        final DeleteVideoResponse response = (DeleteVideoResponse)callService(HttpMethod.DELETE,
                                                                              "v1/video/" + videoId,
                                                                              null,
                                                                              null,
                                                                              jwtTokenString,
                                                                              DeleteVideoResponse.class);
        return response;
    }

    private Serializable callService(final HttpMethod httpMethod,
                                     final String endpointPath,
                                     final Map<String, String> params,
                                     final String postBodyString,
                                     final String jwtTokenString,
                                     final Class<? extends Serializable> responseObjectClass) throws VideosClientException {
        Map<String, String> headers = null;
        if (jwtTokenString != null)
            headers = jwtTokenAuthHeader(jwtTokenString);

        final String url = (this.remoteServiceSsl ? "https://" : "http://") + this.remoteServiceHost + ":" + this.remoteServicePort + "/videos/" + endpointPath;

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

        final HttpRequester http = builder.build();

        try {
            http.execute();
        } catch (final Exception e) {
            throw new VideosClientException("Failed to communicate with remote-videos-service url [ " + url + " ] ...", e);
        }

        Log.debug(".. called remote videos service url successfully [ " + url + " ] resp [ " + http.getResponse() + " ] ");

        if (responseObjectClass != null) {
            final Serializable response;
            try {
                response = http.getJSONResult(responseObjectClass);
            } catch (final HttpException e) {
                throw new VideosClientException("Bad response from service :: " + http.getResponse(), e);
            }

            return response;
        }

        return null;
    }

    private static Map<String, String> jwtTokenAuthHeader(final String tokenString) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HTTP_AUTHORIZATION_HEADER_NAME, HTTP_AUTHORIZATION_HEADER_SCHEMA + " " + tokenString);
        return headers;
    }

    private static String toJsonString(final Serializable requestObject) throws VideosClientException {
        final ObjectMapper objectMapper = new ObjectMapper();
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(requestObject);
            return requestBody;
        } catch (final JsonProcessingException e) {
            throw new VideosClientException("Invalid request object ...", e);
        }
    }

    public static final class VideosClientException extends Exception {

        private static final long serialVersionUID = 8026598541452444922L;

        public VideosClientException(final String msg) {
            super(msg);
        }

        public VideosClientException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    public static final class AddVideoRequest implements java.io.Serializable {

        private static final long serialVersionUID = 9177186021859756942L;

        @JsonProperty("name")
        private final String name;

        @JsonProperty("projectId")
        private final String projectId;

        public AddVideoRequest(final String name,
                               final String projectId) {
            this.name = name;
            this.projectId = projectId;
        }

    }

    public static final class AddProjectRequest implements java.io.Serializable {

        private static final long serialVersionUID = -3660346305449021531L;

        @JsonProperty("name")
        private final String name;

        public AddProjectRequest(final String name) {
            this.name = name;
        }

    }

    public static final class UpdateProjectRequest implements java.io.Serializable {

        private static final long serialVersionUID = -2343315182890052324L;

        @JsonProperty("name")
        private final String name;

        public UpdateProjectRequest(final String name) {
            this.name = name;
        }

    }

}
