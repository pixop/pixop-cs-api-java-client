package com.pixop.sdk.services.media;

import com.pixop.sdk.services.accounts.AccountsServiceClient;
import com.pixop.sdk.services.accounts.config.AccountsServiceClientConfig;
import com.pixop.sdk.services.accounts.response.NewAuthToken;
import com.pixop.sdk.services.media.MediaServiceClient.DownloadFuture;
import com.pixop.sdk.services.media.MediaServiceClient.UploadFuture;
import com.pixop.sdk.services.media.config.MediaServiceClientConfig;
import com.pixop.sdk.services.videos.TestVideosService;
import com.pixop.sdk.services.videos.VideosServiceClient;
import com.pixop.sdk.services.videos.config.VideosServiceClientConfig;
import com.pixop.sdk.services.videos.model.Video;
import com.pixop.sdk.services.videos.request.PaginationParameters;
import com.pixop.sdk.services.videos.response.ListVideosResponse;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author  Paul Cook
 * @version
 */
public final class TestMediaService {

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

        final MediaServiceClientConfig mediaServiceClientConfig = new MediaServiceClientConfig.Builder()
                .setMediaServiceHost("staging-api.pixop.com")
                .setMediaServicePort(443)
                .setSsl(true)
                .build();

        final MediaServiceClient mediaServiceClient = new MediaServiceClient(mediaServiceClientConfig);

        final String email = System.getenv("PIXOP_API_EMAIL");
        final String password = System.getenv("PIXOP_API_PASSWORD");

        final String teamId = null;
        final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                     password,
                                                                     teamId);

        final String jwtTokenString = newToken.getJwtTokenString();

        System.out.println("::: got new JWT token for user [ " + email + " ] ::: TOKEN [ " + jwtTokenString + " ] identified user id [ " + newToken.getUserId() + " ] Team [ " + newToken.getTeamId() + " ] ");

        ListVideosResponse listVideosResponse = videosServiceClient.listVideos(jwtTokenString);

        System.out.println(":::: got list of videos for user [ " + newToken.getUserId() + " / " + email + " ] :: " + listVideosResponse.toString());

        //xxxxx do an 'add video' in the test video service test ..  use a name starting with TEST-CASE-VIDEO-yyyy.mm.dd.hh.mm.ss
        //xxxxx look for the first one of these that doesnt have a file currently uploaded

        ListVideosResponse.VideoSummary videoSummary = null;
        for (;;) {
            for (final ListVideosResponse.VideoSummary videoSummaryCandidate: listVideosResponse.getContents().getVideos())
                if (videoSummaryCandidate.getName().startsWith(TestVideosService.TEST_VIDEO_NAME_PREFIX))
                    if (videoSummaryCandidate.getFileName() == null)
                        videoSummary = videoSummaryCandidate;
            if (videoSummary != null)
                break;
            if (listVideosResponse.getNavigationLinks().getNextPage() == null)
                break;
            final PaginationParameters paginationParameters = new PaginationParameters.Builder()
                                                                                      .withPageSize(listVideosResponse.getPageSize())
                                                                                      .withPageNumber(listVideosResponse.getPageNumber() + 1)
                                                                                      .build();
            listVideosResponse = videosServiceClient.listVideos(jwtTokenString,
                                                                paginationParameters);
        }
        if (videoSummary == null)
            throw new Exception("COULD NOT FIND A MATCHING VIDEO WITH A NAME STARTING WITH [ " + TestVideosService.TEST_VIDEO_NAME_PREFIX + " ] that does not already have media loaded ...!!! --- ABORT!");

        final String videoId = videoSummary.getId();

        final String sourceFileName = "small.mp4";
        final File sourceFile = new File(sourceFileName);

        final UploadFuture uploadFuture = new TestUploadFuture(videoId,
                                                               sourceFileName);

        System.out.println(":::: PERFORMING A MEDIA UPLOAD TO VIDEO ID [ " + videoId + " ] ... this will take a few seconds ....");

        mediaServiceClient.uploadFromFile(videoId,
                                          jwtTokenString,
                                          false,
                                          sourceFile,
                                          uploadFuture);

        synchronized(uploadFuture) {
            try {
                uploadFuture.wait(600000); // wait 10 minutes at most
            } catch (final Exception e) {
                System.err.println("Exception waiting for upload future ....:" + e);
                throw new Exception("ABORT!", e);
            }
        }

        if (uploadFuture.isSuccess())
            System.out.println("::::: SUCCESSFULLY UPLOADED MEDIA FILE!!!");
        else {
            System.err.println("Failure uploading to media service ....");
            throw new Exception("Failure uploading to media service ....");
        }

        final Video video = videosServiceClient.getVideo(videoId, jwtTokenString).getVideo();
        System.out.println("... got video [ " + videoId + " ] :: " + video.toString());

        // test download of original file ......

        final String localFileName = "/tmp/1/fart.file";
        final File testDir = new File("/tmp/1");
        if (!testDir.exists())
            if (!testDir.mkdirs())
                throw new Exception("Could not create temp work-dir '/tmp/1'");

        final FileOutputStream outputStream = new FileOutputStream(new File(localFileName));

        final DownloadFuture downloadFuture = new TestDownloadFuture(videoId,
                                                                     localFileName);

        System.out.println(":::: PERFORMING A MEDIA DOWNLOAD FROM VIDEO ID [ " + videoId + " ] ... this will take a few seconds ....");

        mediaServiceClient.downloadOriginalMedia(videoId,
                                                 jwtTokenString,
                                                 outputStream,
                                                 downloadFuture);

        synchronized(downloadFuture) {
            try {
                downloadFuture.wait(600000); // wait 10 minutes at most
            } catch (final Exception e) {
                System.err.println("Exception waiting for download future ....:" + e);
                throw new Exception("ABORT!", e);
            }
        }

        if (downloadFuture.isSuccess())
            System.out.println("::::: SUCCESSFULLY DOWNLOADED ORIGINAL MEDIA FILE!!!");
        else {
            System.err.println("Failure downloading from media service ....");
            throw new Exception("Failure downloading from media service ....");
        }
    }

    public static final class TestUploadFuture extends UploadFuture {

        private final String videoId;
        private final String uploadFileName;

        public TestUploadFuture(final String videoId,
                                final String uploadFileName) {
            super();
            this.videoId = videoId;
            this.uploadFileName = uploadFileName;
        }

        @Override
        public void onSuccess() {
            System.out.println(":::: SUCCESS :: finished uploading media to media service :: video [ " + this.videoId + " ] filename [ " + this.uploadFileName + " ] ");
        }

        @Override
        public void onException(final Exception exception) {
            System.err.println(":::: EXCEPTION :: uploading media to media service :: video [ " + this.videoId + " ] filename [ " + this.uploadFileName + " ]: " + exception);
        }

        @Override
        public void onFailure(final int status, final String resultMessage) {
            System.out.println(":::: FAILURE :: whilst uploading media to media service :: video [ " + this.videoId + " ] filename [ " + this.uploadFileName + " ]  :: STATUS [ " + status + " / " + resultMessage + " ] ");
        }

    }

    public static final class TestDownloadFuture extends DownloadFuture {

        private final String videoId;
        private final String localFileName;

        public TestDownloadFuture(final String videoId,
                                  final String localFileName) {
            super();
            this.videoId = videoId;
            this.localFileName = localFileName;
        }

        @Override
        public void onSuccess() {
            System.out.println(":::: SUCCESS :: finished downloading media from media service :: video [ " + this.videoId + " ] to local filename [ " + this.localFileName + " ] ");
        }

        @Override
        public void onException(final Exception exception) {
            System.err.println(":::: EXCEPTION :: downloading media from media service :: video [ " + this.videoId + " ] to local filename [ " + this.localFileName + " ]: " + exception);
        }

        @Override
        public void onFailure(final int status, final String resultMessage) {
            System.out.println(":::: FAILURE :: whilst downloading media from media service :: video [ " + this.videoId + " ] to local filename [ " + this.localFileName + " ]  :: STATUS [ " + status + " / " + resultMessage + " ] ");
        }

    }

}