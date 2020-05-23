package com.pixop.sdk.services.media;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pixop.sdk.core.http.HttpMethod;
import com.pixop.sdk.core.http.HttpRequester;
import com.pixop.sdk.core.http.HttpRequester.Builder;
import com.pixop.sdk.core.http.HttpRequester.HttpException;
import com.pixop.sdk.services.media.config.MediaServiceClientConfig;
import com.pixop.sdk.services.media.response.DeleteMediaResponse;

/**
 * @author  Paul Cook
 * @version
 */
public final class MediaServiceClient {

    private static final Logger Log = LoggerFactory.getLogger(MediaServiceClient.class.getName());

    public static final String HTTP_AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String HTTP_AUTHORIZATION_HEADER_SCHEMA = "Bearer";

    public static final String UPLOAD_FULLY_INGESTED_PATH = "/media/v1/upload";
    public static final String UPLOAD_LIGHT_INGESTED_PATH = "/media/v1/upload/light";
    public static final String DOWNLOAD_ORIGINAL_MEDIA_PATH = "/media/v1/download/original";
    public static final String DOWNLOAD_PROCESSED_MEDIA_PATH = "/media/v1/download/processed";
    public static final String DOWNLOAD_SMALL_SCREENSHOT_PATH = "/media/v1/download/screenshot/small/";
    public static final String DOWNLOAD_LARGE_SCREENSHOT_PATH = "/media/v1/download/screenshot/large/";

    public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    private final String remoteServiceUrl;
    private final int remoteServiceConnectTimeout;
    private final int remoteServiceSoTimeout;
    private final String remoteServiceUserAgent;

    public static abstract class UploadFuture extends MediaServiceTransferOperationFuture {

    }

    public static abstract class DownloadFuture extends MediaServiceTransferOperationFuture {

    }

    protected static abstract class MediaServiceTransferOperationFuture {

        private volatile boolean success = false;

        public MediaServiceTransferOperationFuture() {
        }

        protected final void success() {
            this.success = true;
            onSuccess();
            wakeUp();
        }

        protected final void exception(final Exception exception) {
            this.success = false;
            onException(exception);
            wakeUp();
        }

        protected final void failure(final int status, final String resultMessage) {
            this.success = false;
            onFailure(status, resultMessage);
            wakeUp();
        }

        public final boolean isSuccess() {
            return this.success;
        }

        public abstract void onSuccess();

        public abstract void onException(final Exception exception);

        public abstract void onFailure(final int status, final String resultMessage);

        private final void wakeUp() {
            synchronized(this) {
                try {
                    notify();
                } catch (final Exception e) {
                    Log.error("Exception waking up future ...", e);
                }
            }
        }

    }

    public static final class MediaServiceException extends Exception {

        private static final long serialVersionUID = -2832841546378302906L;

        public MediaServiceException(final String msg) {
            super(msg);
        }

        public MediaServiceException(final String msg, final Throwable t) {
            super(msg, t);
        }

    }

    private MediaServiceClient(final String remoteServiceHost,
                               final int remoteServicePort,
                               final int remoteServiceConnectTimeout,
                               final int remoteServiceSoTimeout,
                               final String remoteServiceUserAgent,
                               final boolean ssl) {
        this.remoteServiceUrl = (ssl ? "https" : "http") +
                                "://" +
                                remoteServiceHost +
                                ":" +
                                remoteServicePort;
        this.remoteServiceConnectTimeout = remoteServiceConnectTimeout;
        this.remoteServiceSoTimeout = remoteServiceSoTimeout;
        this.remoteServiceUserAgent = remoteServiceUserAgent;
    }

    public MediaServiceClient(final MediaServiceClientConfig mediaServiceClientConfig) {
        this(mediaServiceClientConfig.getMediaServiceHost(),
             mediaServiceClientConfig.getMediaServicePort(),
             mediaServiceClientConfig.getConnectionTimeout(),
             mediaServiceClientConfig.getSoTimeout(),
             mediaServiceClientConfig.getUserAgent(),
             mediaServiceClientConfig.isSsl());
    }

    public void upload(final String videoId,
                       final String jwtTokenString,
                       final File file,
                       final boolean fullyIngested,
                       final UploadFuture uploadFuture) throws IOException {
        new UploadThread(this,
                         videoId,
                         jwtTokenString,
                         file,
                         fullyIngested,
                         uploadFuture);
    }

    public void uploadFromFile(final String videoId,
                               final String jwtTokenString,
                               final boolean fullyIngested,
                               final File filePath,
                               final UploadFuture uploadFuture) {
        new UploadFromFileThread(this,
                                 videoId,
                                 jwtTokenString,
                                 fullyIngested,
                                 filePath,
                                 uploadFuture);
    }

    private static final class UploadThread extends Thread {

        private final MediaServiceClient client;
        private final String videoId;
        private final String jwtTokenString;
        private final UploadFuture uploadFuture;

        private final File file;
        private final boolean fullyIngested;

        // Covers streaming file uploads
        public UploadThread(final MediaServiceClient client,
                            final String videoId,
                            final String jwtTokenString,
                            final File file,
                            final boolean fullyIngested,
                            final UploadFuture uploadFuture) {
            this.client = client;
            this.videoId = videoId;
            this.jwtTokenString = jwtTokenString;
            this.file = file;
            this.fullyIngested = fullyIngested;
            this.uploadFuture = uploadFuture;

            start();
        }

        @Override
        public void run() {
            try {
                this.client.doUpload(this.videoId,
                                     this.jwtTokenString,
                                     this.fullyIngested,
                                     this.file,
                                     this.uploadFuture);
            } catch (final Exception e) {
                Log.error("....... upload failed .....", e);
                notifyFutureOfException(this.uploadFuture, e);
            }
        }

    }

    private static final class UploadFromFileThread extends Thread {

        private final MediaServiceClient client;
        private final String videoId;
        private final String jwtTokenString;
        private final boolean fullyIngested;
        private final File filePath;
        private final UploadFuture uploadFuture;

        public UploadFromFileThread(final MediaServiceClient client,
                                    final String videoId,
                                    final String jwtTokenString,
                                    final boolean fullyIngested,
                                    final File filePath,
                                    final UploadFuture uploadFuture) {
            this.client = client;
            this.videoId = videoId;
            this.jwtTokenString = jwtTokenString;
            this.fullyIngested = fullyIngested;
            this.filePath = filePath;
            this.uploadFuture = uploadFuture;

            start();
        }

        @Override
        public void run() {
            try {
                this.client.doUploadFromFile(this.videoId,
                                             this.jwtTokenString,
                                             this.fullyIngested,
                                             this.filePath,
                                             this.uploadFuture);
            } catch (final Exception e) {
                Log.error("....... upload from file failed .....", e);
                notifyFutureOfException(this.uploadFuture, e);
            }
        }
    }

    private void doUploadFromFile(final String videoId,
                                  final String jwtTokenString,
                                  final boolean fullyIngested,
                                  final File filePath,
                                  final UploadFuture uploadFuture) {
        try {
            //in = new FileInputStream(filePath);
            this.upload(videoId,
                        jwtTokenString,
                        filePath,
                        fullyIngested,
                        uploadFuture);
        } catch (final Exception e) {
            Log.error("....... upload failed .....", e);
            notifyFutureOfException(uploadFuture, e);
        }
    }

    private void doUpload(final String videoId,
                          final String jwtTokenString,
                          final boolean fullyIngested,
                          final File file,
                          final UploadFuture uploadFuture) throws MediaServiceException {
        final Map<String, String> headers = new HashMap<>();
        if (jwtTokenString != null && !jwtTokenString.isEmpty())
            headers.put(HTTP_AUTHORIZATION_HEADER_NAME, HTTP_AUTHORIZATION_HEADER_SCHEMA + " " + jwtTokenString);

        final String remoteServiceUrl = this.remoteServiceUrl + (fullyIngested ? UPLOAD_FULLY_INGESTED_PATH : UPLOAD_LIGHT_INGESTED_PATH) + "/" + videoId;

        final Builder builder = new HttpRequester.Builder(this.remoteServiceUserAgent)
                                                 .setMethod(HttpMethod.POST)
                                                 .setUrl(remoteServiceUrl)
                                                 .setHeaders(headers)
                                                 .setConnectTimeout(this.remoteServiceConnectTimeout)
                                                 .setSoTimeout(this.remoteServiceSoTimeout)
                                                 .setFiles(new File[] { file, })
                                                 .setPostContentType("multipart/form-data")
                                                 .setPostContentCharset(CHARSET_UTF8)
                                                 .setAllowedResponseStatusCodes(400, 500, 503, 401);

        final HttpRequester http = builder.build();

        try {
            http.execute();
        } catch (final Exception e) {
            throw new MediaServiceException("Failed to communicate with remote-media-service url [ " + remoteServiceUrl + " ] ...", e);
        }

        final int statusCode = http.getStatus();
        final String statusMsg = http.getReason();

        Log.debug(".. called remote media service url successfully [ " + remoteServiceUrl + " ] resp [ " + statusCode + ":" + statusMsg + " ] ");

        if (statusCode != 200) {
            Log.warn("Error returned from remote service ... [ " + statusCode + " ] - [ " + statusMsg + " ] ");

            notifyFutureOfFailure(uploadFuture,
                                  statusCode,
                                  statusMsg);
        } else {
            notifyFutureOfSuccess(uploadFuture);
        }
    }

    private static void notifyFutureOfSuccess(final MediaServiceTransferOperationFuture future) {
        if (future != null)
            future.success();
        else
            Log.error("No Future object available for success callback...");
    }

    private static void notifyFutureOfException(final MediaServiceTransferOperationFuture future,
                                                final Exception exception) {
        if (future != null)
            future.exception(exception);
        else
            Log.error("No Future object available for exception callback...");
    }

    private static void notifyFutureOfFailure(final MediaServiceTransferOperationFuture future,
                                              final int status,
                                              final String resultMessage) {
        if (future != null)
            future.failure(status,
                           resultMessage);
        else
            Log.error("No Future object available for failure callback...");
    }

    public void downloadOriginalMedia(final String videoId,
                                      final String jwtTokenString,
                                      final OutputStream outputStream,
                                      final DownloadFuture downloadFuture) {
        new DownloadThread(this,
                           videoId,
                           jwtTokenString,
                           DOWNLOAD_ORIGINAL_MEDIA_PATH,
                           outputStream,
                           downloadFuture);
    }

    public void downloadProcessedMedia(final String videoId,
                                       final String jwtTokenString,
                                       final OutputStream outputStream,
                                       final DownloadFuture downloadFuture) {
        new DownloadThread(this,
                           videoId,
                           jwtTokenString,
                           DOWNLOAD_PROCESSED_MEDIA_PATH,
                           outputStream,
                           downloadFuture);
    }

    public void downloadSmallScreenshot(final String videoId,
                                        final String jwtTokenString,
                                        final OutputStream outputStream,
                                        final DownloadFuture downloadFuture) {
        new DownloadThread(this,
                           videoId,
                           jwtTokenString,
                           DOWNLOAD_SMALL_SCREENSHOT_PATH,
                           outputStream,
                           downloadFuture);
    }

    public void downloadLargeScreenshot(final String videoId,
                                        final String jwtTokenString,
                                        final OutputStream outputStream,
                                        final DownloadFuture downloadFuture) {
        new DownloadThread(this,
                           videoId,
                           jwtTokenString,
                           DOWNLOAD_LARGE_SCREENSHOT_PATH,
                           outputStream,
                           downloadFuture);
    }

    private static final class DownloadThread extends Thread {

        private final MediaServiceClient client;
        private final String videoId;
        private final String jwtTokenString;
        private final String remoteServicePath;
        private final OutputStream outputStream;
        private final DownloadFuture downloadFuture;

        public DownloadThread(final MediaServiceClient client,
                              final String videoId,
                              final String jwtTokenString,
                              final String remoteServicePath,
                              final OutputStream outputStream,
                              final DownloadFuture downloadFuture) {
            this.client = client;
            this.videoId = videoId;
            this.jwtTokenString = jwtTokenString;
            this.remoteServicePath = remoteServicePath;
            this.outputStream = outputStream;
            this.downloadFuture = downloadFuture;

            start();
        }

        @Override
        public void run() {
            try {
                this.client.doDownloadToOutputStream(this.videoId,
                                                     this.jwtTokenString,
                                                     this.remoteServicePath,
                                                     this.outputStream,
                                                     this.downloadFuture);
            } catch (final Exception e) {
                Log.error("....... download media file to output stream failed .....", e);
                notifyFutureOfException(this.downloadFuture, e);
            }
        }
    }

    private void doDownloadToOutputStream(final String videoId,
                                          final String jwtTokenString,
                                          final String remoteServicePath,
                                          final OutputStream outputStream,
                                          final DownloadFuture downloadFuture) throws MediaServiceException {
        final String remoteServiceUrl = this.remoteServiceUrl + remoteServicePath + "/" + videoId;

        final Map<String, String> headers = new HashMap<>();
        if (jwtTokenString != null && !jwtTokenString.isEmpty())
            headers.put(HTTP_AUTHORIZATION_HEADER_NAME, HTTP_AUTHORIZATION_HEADER_SCHEMA + " " + jwtTokenString);

        final Builder builder = new HttpRequester.Builder(this.remoteServiceUserAgent)
                                                 .setMethod(HttpMethod.GET)
                                                 .setUrl(remoteServiceUrl)
                                                 .setHeaders(headers)
                                                 .setConnectTimeout(this.remoteServiceConnectTimeout)
                                                 .setSoTimeout(this.remoteServiceSoTimeout)
                                                 .setStreamResponseBodyToOutputStream(outputStream)
                                                 .setAllowedResponseStatusCodes(400, 500, 503, 401);

        final HttpRequester http = builder.build();

        try {
            http.execute();
        } catch (final Exception e) {
            throw new MediaServiceException("Failed to communicate with remote-media-service url [ " + remoteServiceUrl + " ] ...", e);
        }

        final int statusCode = http.getStatus();
        final String statusMsg = http.getReason();

        Log.debug(".. called remote media service url successfully [ " + remoteServiceUrl + " ] resp [ " + statusCode + ":" + statusMsg + " ] ");

        if (statusCode != 200) {
            Log.warn("Error returned from remote service ... [ " + statusCode + " ] - [ " + statusMsg + " ] ");

            notifyFutureOfFailure(downloadFuture,
                                  statusCode,
                                  statusMsg);
        } else {
            notifyFutureOfSuccess(downloadFuture);
        }
    }

}
