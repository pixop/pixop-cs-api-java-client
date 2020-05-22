package com.pixop.sdk.services.videos;

import com.pixop.sdk.services.accounts.AccountsServiceClient;
import com.pixop.sdk.services.accounts.config.AccountsServiceClientConfig;
import com.pixop.sdk.services.accounts.response.NewAuthToken;
import com.pixop.sdk.services.videos.config.VideosServiceClientConfig;
import com.pixop.sdk.services.videos.model.VideoProcessingState;
import com.pixop.sdk.services.videos.request.ProcessVideoRequest;
import com.pixop.sdk.services.videos.request.ProcessVideoRequest.*;
import com.pixop.sdk.services.videos.response.DeleteVideoResponse;
import com.pixop.sdk.services.videos.response.ListVideosResponse;
import com.pixop.sdk.services.videos.response.ProcessVideoCheckProgressResponse;
import com.pixop.sdk.services.videos.response.ProcessVideoResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class TestProcessVideo {

    public static final String TEST_VIDEO_NAME_PREFIX = "TEST-CASE-VIDEO-";
    public static final String TEST_PROJECT_NAME_PREFIX = "TEST-CASE-PROJECT-";

    public static void main(String[] args) throws Exception {
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

        final String teamId = null;
        final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                     password,
                                                                     teamId);

        final String jwtTokenString = newToken.getJwtTokenString();

        System.out.println("::: got new JWT token for user [ " + email + " ] ::: TOKEN [ " + jwtTokenString + " ] identified user id [ " + newToken.getUserId() + " ] Team [ " + newToken.getTeamId() + " ] ");

        final ListVideosResponse listVideosResponse = videosServiceClient.listVideos(jwtTokenString);

        System.out.println(":::: got list of videos for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listVideosResponse.toString());

        /*
        ListVideosResponse.VideoSummary videoSummary = null;
        for (final ListVideosResponse.VideoSummary videoSummaryCandidate: listVideosResponse.getContents().getVideos())
            if (videoSummaryCandidate.getName().startsWith(TestVideosService.TEST_VIDEO_NAME_PREFIX))
                if (videoSummaryCandidate.getFileName() != null)
                    videoSummary = videoSummaryCandidate;
        if (videoSummary == null)
            throw new Exception("COULD NOT FIND A MATCHING VIDEO WITHG A NAME STARTING WITH [ " + TestVideosService.TEST_VIDEO_NAME_PREFIX + " ] that does not already have media loaded ...!!! --- ABORT!");
        */
        final String videoId = "ca3b774f-bcc6-4601-8357-04d876a2ef90";//videoSummary.getId();

        //System.out.println("::::: got a video that we are going to try and process ...... :: " + videoSummary);

        final ProcessVideoRequest processVideoRequest;
        processVideoRequest = new ProcessVideoRequest.Builder()
                                                     .withMediaContainerCodec(new MediaContainerCodec(CONTAINER.QUICKTIME_MOV,
                                                                                                      CODEC.APPLE_PRORES))
                                                     .withAppleProResProfile(APPLE_PRORES_PROFILE.PROFILE_STANDARD)
                                                     .withDenoiser(DE_NOISER.THREE_D_DENOISER)
                                                     .withScaler(SCALER.PIXOP_SUPER_RESOLUTION)
                                                     .withResolution(new Resolution(RESOLUTION_TAG.RESOLUTION_HD_1080P))
                                                     .withClarityBoost(CLARITY_BOOST.HIGH)
                                                     .withRange(new Range.Builder()
                                                                         .withStartPositionMilliseconds(0)
                                                                         .withEndPositionMilliseconds(5000)
                                                                         .build())
                                                     .build();

        System.out.println(":: Constructed a process video request object :: " + processVideoRequest);

        final ProcessVideoResponse processVideoResponse = videosServiceClient.processVideo(videoId,
                                                                                           processVideoRequest,
                                                                                           jwtTokenString);

        System.out.println("::: got response from process video service call :: " + processVideoResponse);

        // check progress every few seconds untill complete ...

        final long timeStarted = System.currentTimeMillis();

        final String newVideoId = processVideoResponse.getProcessedVideoId();

        System.out.println(".... check progress of newly generated video [ " + newVideoId + " ] ");

        for (;;) {
            final long timeRunning = System.currentTimeMillis() - timeStarted;
            if (timeRunning > 10000) {
                System.out.println("... waited for over a minute ...  time to give up ....");

                final DeleteVideoResponse deleteVideoResponse = videosServiceClient.deleteVideo(newVideoId, jwtTokenString);
                System.out.println(":::: .. cancelled process video for new video [ " + newVideoId + " ] :: " + deleteVideoResponse);

                break;
            }

            final ProcessVideoCheckProgressResponse checkProgressResponse = videosServiceClient.processVideoCheckProgress(newVideoId, jwtTokenString);

            System.out.println("... been running for [ " + timeRunning + " ] ... check progress response :: " + checkProgressResponse);

            final VideoProcessingState processingState = checkProgressResponse.getProcessingState();
            if (processingState != null) {
                if (processingState.getProcessingStatus() != null &&
                    processingState.getProcessingStatus().equalsIgnoreCase("DONE")) {
                    System.out.println("::::: check progress says :: DONE!!!! .. we're finished!!!");
                    break;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {}
        }

        System.out.println("... finished!!!");
    }

}