package com.pixop.sdk.services.videos;

import com.pixop.sdk.services.accounts.AccountsServiceClient;
import com.pixop.sdk.services.accounts.config.AccountsServiceClientConfig;
import com.pixop.sdk.services.accounts.response.NewAuthToken;
import com.pixop.sdk.services.videos.config.VideosServiceClientConfig;
import com.pixop.sdk.services.videos.iterator.ListProjectsIterator;
import com.pixop.sdk.services.videos.iterator.ListVideosIterator;
import com.pixop.sdk.services.videos.model.Project;
import com.pixop.sdk.services.videos.model.Video;
import com.pixop.sdk.services.videos.request.PaginationParameters;
import com.pixop.sdk.services.videos.response.AddVideoResponse;
import com.pixop.sdk.services.videos.response.ListProjectsResponse;
import com.pixop.sdk.services.videos.response.ListVideosResponse;
import com.pixop.sdk.services.videos.response.ProjectResponse;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author  Paul Cook
 * @version
 */
public final class TestVideosService {

    public static final String TEST_VIDEO_NAME_PREFIX = "TEST-CASE-VIDEO-";
    public static final String TEST_PROJECT_NAME_PREFIX = "TEST-CASE-PROJECT-";

    public static void main(String[] args) throws AccountsServiceClient.AccountsClientException, VideosServiceClient.VideosClientException {
        final AccountsServiceClientConfig accountsServiceClientConfig = new AccountsServiceClientConfig.Builder()
                .setAccountsServiceHost("staging-api.pixop.com")
                .setAccountsServicePort(443)
                .setSsl(true)
                .build();

        final AccountsServiceClient accountsServiceClient = new AccountsServiceClient(accountsServiceClientConfig);

        final VideosServiceClientConfig videosServiceClientConfig = new VideosServiceClientConfig.Builder()
                .setVideoServiceHost("staging-api.pixop.com")
                .setVideosServicePort(443)
                .setSsl(true)
                .build();

        final VideosServiceClient videosServiceClient = new VideosServiceClient(videosServiceClientConfig);

        final String email = System.getenv("PIXOP_API_EMAIL");
        final String password = System.getenv("PIXOP_API_PASSWORD");

        String teamId = null;
        final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                     password,
                                                                     teamId);

        final String jwtTokenString = newToken.getJwtTokenString();

        System.out.println("::: got new JWT token for user [ " + email + " ] ::: TOKEN [ " + jwtTokenString + " ] identified user id [ " + newToken.getUserId() + " ] Team [ " + newToken.getTeamId() + " ] ");

        /*
        teamId = core.getConfig().getTestCaseConstants().getAuthenticateWithAdditionalTeam();

        final NewAuthToken newToken2 = accountsServiceClient.newToken(email,
                                                                      password,
                                                                      teamId);

        final String jwtTokenStringAdditionalTeam = newToken2.getJwtTokenString();

        System.out.println("::: got new JWT token for user [ " + email + " ] ::: TOKEN [ " + jwtTokenStringAdditionalTeam + " ] identified user id [ " + newToken2.getUserId() + " ] Team [ " + newToken2.getTeamId() + " ] ");
        */

        ListProjectsResponse listProjectsResponse = videosServiceClient.listProjects(jwtTokenString);

        System.out.println(":::: got list of projects for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listProjectsResponse.toString());

        System.out.println("----- traverse all known projects for this user/team and retrieve matching videos -----");
        for (;;) {
            for (final ListProjectsResponse.ProjectSummary project: listProjectsResponse.getContents().getProjects()) {
                final String projectId = project.getId();
                System.out.println("---- try project id [ " + projectId + " ] ....");
                ListVideosResponse listVideosResponse = videosServiceClient.listVideosInProject(projectId, jwtTokenString);
                System.out.println("... got videos for project [ " + projectId + " ] :: " + listVideosResponse);
                for (;;) {
                    if (listVideosResponse.getNavigationLinks().getNextPage() == null)
                        break;
                    final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                              .withPageSize(listVideosResponse.getPageSize())
                                                                                              .withPageNumber(listVideosResponse.getPageNumber() + 1)
                                                                                              .build();
                    listVideosResponse = videosServiceClient.listVideosInProject(projectId,
                                                                                 jwtTokenString,
                                                                                 paginationParameters);
                    System.out.println("... got next page of videos for project [ " + projectId + " ] :: " + listVideosResponse);
                }
            }

            if (listProjectsResponse.getNavigationLinks().getNextPage() == null)
                break;

            // -- pull the next page and loop again ....

            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listProjectsResponse.getPageSize())
                                                                                      .withPageNumber(listProjectsResponse.getPageNumber() + 1)
                                                                                      .build();
            listProjectsResponse = videosServiceClient.listProjects(jwtTokenString,
                                                                    paginationParameters);

            System.out.println(":::: got next page of projects for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listProjectsResponse.toString());
        }

        ListVideosResponse listVideosResponse = videosServiceClient.listVideos(jwtTokenString);

        System.out.println(":::: got list of videos for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listVideosResponse.toString());
        for (;;) {
            if (listVideosResponse.getNavigationLinks().getNextPage() == null)
                break;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listVideosResponse.getPageSize())
                                                                                      .withPageNumber(listVideosResponse.getPageNumber() + 1)
                                                                                      .build();
            listVideosResponse = videosServiceClient.listVideos(jwtTokenString,
                                                                paginationParameters);
            System.out.println("... got next page of videos for user [ " + newToken.getUserId() + " / " + email + " ]:: " + listVideosResponse);
        }

        System.out.println("---- retrieve video's for a bogus project id ....");
        String projectId = "bogus-project-id";
        listVideosResponse = videosServiceClient.listVideosInProject(projectId, jwtTokenString);
        System.out.println("... got videos for project [ " + projectId + " ] :: " + listVideosResponse);

        System.out.println(".... retrieve the detailed project details for all the projects we found ....");
        listProjectsResponse = videosServiceClient.listProjects(jwtTokenString);

        for (;;) {
            for (final ListProjectsResponse.ProjectSummary projectSummary: listProjectsResponse.getContents().getProjects()) {
                projectId = projectSummary.getId();
                System.out.println("---- try project id [ " + projectId + " ] ....");
                final ProjectResponse projectResponse = videosServiceClient.getProject(projectId, jwtTokenString);
                final Project project = projectResponse.getProject();
                System.out.println("... got project [ " + projectId + " ] :: " + project.toString());
            }

            if (listProjectsResponse.getNavigationLinks().getNextPage() == null)
                break;

            // -- pull the next page and loop again ....

            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listProjectsResponse.getPageSize())
                                                                                      .withPageNumber(listProjectsResponse.getPageNumber() + 1)
                                                                                      .build();
            listProjectsResponse = videosServiceClient.listProjects(jwtTokenString,
                                                                    paginationParameters);
        }
        System.out.println("... finished retrieving detailed project details ...");

        /*
        final String knownVideoId = "9a6f9ba2-ecf0-4f23-b205-c7d71189dd69"

        final VideoResponse videoResponse = videosServiceClient.getVideo(knownVideoId, jwtTokenString);
        final Video v2 = videoResponse.getVideo();
        System.out.println("... v2 :: " + v2);
        System.out.println("....vv2 :: " + v2.toString());
        */

        // add a new video ....

        String name = TEST_VIDEO_NAME_PREFIX + createCommonTimestampString(new Date(System.currentTimeMillis()));
        AddVideoResponse addVideoResponse = videosServiceClient.addVideo(name, projectId, jwtTokenString);
        System.out.println("::: add video :: new id [ " + addVideoResponse.getProjectId() + " ] :: [ " + addVideoResponse.getVideoId() + " ] ");
        System.out.println("::: add video :: upload media ===> " + addVideoResponse.getUploadMediaServiceEndpointUrl());
        System.out.println("::: add video :: process video ===> " + addVideoResponse.getProcessVideoServiceEndpointUrl());
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        listVideosResponse = videosServiceClient.listVideos(jwtTokenString);

        System.out.println(":::: got list of videos for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listVideosResponse.toString());

        System.out.println(".... retrieve the detailed video details for all the videos we found ....");
        for (;;) {
            for (final ListVideosResponse.VideoSummary videoSummary: listVideosResponse.getContents().getVideos()) {
                final String videoId = videoSummary.getId();
                System.out.println("---- try video id [ " + videoId + " ] ....");
                final Video video = videosServiceClient.getVideo(videoId, jwtTokenString).getVideo();
                System.out.println("... got video [ " + videoId + " ] :: " + video.toString());
            }

            if (listVideosResponse.getNavigationLinks().getNextPage() == null)
                break;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listVideosResponse.getPageSize())
                                                                                      .withPageNumber(listVideosResponse.getPageNumber() + 1)
                                                                                      .build();
            listVideosResponse = videosServiceClient.listVideos(jwtTokenString,
                                                                paginationParameters);
        }
        System.out.println("... finished retrieving detailed video details ...");

        // create a new project and add a video ...

        System.out.println(":::::: create a new project ==========================");
        name = TEST_PROJECT_NAME_PREFIX + createCommonTimestampString(new Date(System.currentTimeMillis()));
        ProjectResponse addProjectResponse = videosServiceClient.addProject(name, jwtTokenString);
        projectId = addProjectResponse.getProject().getId();
        System.out.println("::: add project :: new id [ " + addProjectResponse.getProject().getId() + " ] ");
        System.out.println("::: add project :: name [ " + addProjectResponse.getProject().getName() + " ] ");
        System.out.println("::: add project :: team-id [ " + addProjectResponse.getProject().getTeamId() + " ] ");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        name = name + "=== AFTER-MODIFICATION";
        ProjectResponse updateProjectResponse = videosServiceClient.updateProject(projectId, name, jwtTokenString);
        System.out.println("::: update project :: id [ " + updateProjectResponse.getProject().getId() + " ] ");
        System.out.println("::: update project :: new name [ " + updateProjectResponse.getProject().getName() + " ] ");
        System.out.println("::: update project :: team-id [ " + addProjectResponse.getProject().getTeamId() + " ] ");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        name = TEST_VIDEO_NAME_PREFIX + createCommonTimestampString(new Date(System.currentTimeMillis())) + "--test-project";
        addVideoResponse = videosServiceClient.addVideo(name, projectId, jwtTokenString);
        System.out.println("::: add video against test project ..:: new id [ " + addVideoResponse.getProjectId() + " ] :: [ " + addVideoResponse.getVideoId() + " ] ");
        System.out.println("::: add video against test project:: upload media ===> " + addVideoResponse.getUploadMediaServiceEndpointUrl());
        System.out.println("::: add video against test project:: process video ===> " + addVideoResponse.getProcessVideoServiceEndpointUrl());
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        /*
        System.out.println(":::::: create a new project against the additional team ==========================");
        name = TEST_PROJECT_NAME_PREFIX + DateUtil.createCommonTimestampString(new Date(System.currentTimeMillis()));
        addProjectResponse = videosServiceClient.addProject(name, jwtTokenStringAdditionalTeam);
        projectId = addProjectResponse.getProject().getId();
        System.out.println("::: add project :: new id [ " + addProjectResponse.getProject().getId() + " ] ");
        System.out.println("::: add project :: name [ " + addProjectResponse.getProject().getName() + " ] ");
        System.out.println("::: add project :: team-id [ " + addProjectResponse.getProject().getTeamId() + " ] ");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        name = name + "=== AFTER-MODIFICATION";
        updateProjectResponse = videosServiceClient.updateProject(projectId, name, jwtTokenStringAdditionalTeam);
        System.out.println("::: update project :: id [ " + updateProjectResponse.getProject().getId() + " ] ");
        System.out.println("::: update project :: new name [ " + updateProjectResponse.getProject().getName() + " ] ");
        System.out.println("::: update project :: team-id [ " + addProjectResponse.getProject().getTeamId() + " ] ");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");

        name = TEST_VIDEO_NAME_PREFIX + DateUtil.createCommonTimestampString(new Date(System.currentTimeMillis())) + "--test-project";
        addVideoResponse = videosServiceClient.addVideo(name, projectId, jwtTokenStringAdditionalTeam);
        System.out.println("::: add video against test project ..:: new id [ " + addVideoResponse.getProjectId() + " ] :: [ " + addVideoResponse.getVideoId() + " ] ");
        System.out.println("::: add video against test project:: upload media ===> " + addVideoResponse.getUploadMediaServiceEndpointUrl());
        System.out.println("::: add video against test project:: process video ===> " + addVideoResponse.getProcessVideoServiceEndpointUrl());
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        System.out.println("-----------");
        */

        listProjectsResponse = videosServiceClient.listProjects(jwtTokenString);

        System.out.println(":::: got list of projects for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listProjectsResponse.toString());

        System.out.println("----- traverse all known projects for this user/team and retrieve matching videos -----");
        for (;;) {
            for (final ListProjectsResponse.ProjectSummary project: listProjectsResponse.getContents().getProjects()) {
                projectId = project.getId();
                System.out.println("---- try project id [ " + projectId + " ] .... [ " + project.getName() + " ] ");
                listVideosResponse = videosServiceClient.listVideosInProject(projectId, jwtTokenString);
                System.out.println("... got videos for project [ " + projectId + " ] :: " + listVideosResponse);
            }
            if (listVideosResponse.getNavigationLinks().getNextPage() == null)
                break;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listVideosResponse.getPageSize())
                                                                                      .withPageNumber(listVideosResponse.getPageNumber() + 1)
                                                                                      .build();
            listVideosResponse = videosServiceClient.listVideosInProject(projectId,
                                                                         jwtTokenString,
                                                                         paginationParameters);
        }

        System.out.println("::::::::::::::::::::: test project iterator :::::::::::");
        try {
            for (final ListProjectsResponse.ProjectSummary projectSummary: videosServiceClient.iterateProjects(jwtTokenString))
                System.out.println(":::: got project summary :: " + projectSummary);
        } catch (final ListProjectsIterator.ProjectIteratorException e) {
            System.err.println(":::: bad news!!....:" + e);
            throw e;
        }

        System.out.println("::::::::::::::::::::: test video iterator :::::::::::");
        try {
            for (final ListVideosResponse.VideoSummary videoSummary: videosServiceClient.iterateVideos(jwtTokenString))
                System.out.println(":::: got video summary :: " + videoSummary);
        } catch (final ListVideosIterator.VideoIteratorException e) {
            System.err.println(":::: bad news!!....:" + e);
            throw e;
        }

        /*
        System.out.println("::::::::::::::::::::: test project iterator (additional team) :::::::::::");
        try {
            for (final ListProjectsResponse.ProjectSummary projectSummary: videosServiceClient.iterateProjects(jwtTokenStringAdditionalTeam))
                System.out.println(":::: got project summary :: " + projectSummary);
        } catch (final ListProjectsIterator.ProjectIteratorException e) {
            System.err.println(":::: bad news!!....:" + e);
            throw e;
        }
        */

        System.out.println("::::::::::::::::::::: test video iterator (additional team) :::::::::::");
        try {
            for (final ListVideosResponse.VideoSummary videoSummary: videosServiceClient.iterateVideos(jwtTokenString))
                System.out.println(":::: got video summary :: " + videoSummary);
        } catch (final ListVideosIterator.VideoIteratorException e) {
            System.err.println(":::: bad news!!....:" +  e);
            throw e;
        }

        System.out.println("==== DONE ===");
    }

    private static String createCommonTimestampString(final Date date) {
        return COMMON.get().format(date);
    }

    private static final ThreadLocal<SimpleDateFormat> COMMON = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // yyyy-MM-dd HH:mm:ss
            sdf.setLenient(false);

            return sdf;
        }
    };

}