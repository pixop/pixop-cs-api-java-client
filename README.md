# pixop-cs-api-java-client
Java 8 client SDK for Pixop Video Processing REST API

## Requirements
- A valid Pixop staging environment account (contact help@pixop.com)
- JDK 8
- Maven or Gradle

## Build .jar locally
Build the client .jar via Maven (`mvn clean install`) or Gradle (`./gradlew build`).

## Dependency
For Apache Maven pom-file:
```xml
    <dependency>
      <groupId>com.pixop</groupId>
      <artifactId>pixop-api-sdk</artifactId>
      <version>4.3.0</version>
    </dependency>
```

Gradle Groovy DSL:
```
implementation 'com.pixop:pixop-api-sdk:4.3.0'
```

## Essential snippets

### Configure all web service clients
```java
    final String serviceHost = "staging-api.pixop.com";

    final AccountsServiceClientConfig accountsServiceClientConfig = new AccountsServiceClientConfig.Builder()
            .setAccountsServiceHost(serviceHost)
            .setAccountsServicePort(443)
            .setSsl(true)
            .build();
    final AccountsServiceClient accountsServiceClient = new AccountsServiceClient(accountsServiceClientConfig);

    final VideosServiceClientConfig videosServiceClientConfig = new VideosServiceClientConfig.Builder()
            .setVideoServiceHost(serviceHost)
            .setVideosServicePort(443)
            .setSsl(true)
            .build();
    final VideosServiceClient videosServiceClient = new VideosServiceClient(videosServiceClientConfig);

    final MediaServiceClientConfig mediaServiceClientConfig = new MediaServiceClientConfig.Builder()
            .setMediaServiceHost(serviceHost)
            .setMediaServicePort(443)
            .setSsl(true)
            .build();
    final MediaServiceClient mediaServiceClient = new MediaServiceClient(mediaServiceClientConfig);
```

### Authenticate and acquire token for default team
```java
    final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                 password,
                                                                 null);
    final String jwtTokenString = newToken.getJwtTokenString();
    
    // ... do something useful with token (it expires in 15 min)
```

### Look up video
```java    
    final Video video = videosServiceClient.getVideo(videoId, jwtTokenString).getVideo();
    
    // ... do something useful with video
```

### Create new project and add new video into it
```java
    ProjectResponse addProjectResponse = videosServiceClient.addProject("my project", jwtTokenString);
    final String projectId = addProjectResponse.getProject().getId();

    AddVideoResponse addVideoResponse = videosServiceClient.addVideo("my video", projectId, jwtTokenString);
    final String videoId = addVideoResponse.getVideoId();

    // ... upload media
```

### Upload video file
```java
    // callback class for handling success, exception and failure
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
            // ... handle success
        }

        @Override
        public void onException(final Exception exception) {
            // ... handle exception
        }

        @Override
        public void onFailure(final int status, final String resultMessage) {
            // ... handle failure
        }
    }

    // main upload logic
    final String sourceFileName = "small.mp4";
    final File sourceFile = new File(sourceFileName);

    final UploadFuture uploadFuture = new TestUploadFuture(videoId,
                                                           sourceFileName);

    mediaServiceClient.uploadFromFile(videoId,
                                      jwtTokenString,
                                      false,
                                      sourceFile,
                                      uploadFuture);

    synchronized(uploadFuture) {
        try {
            uploadFuture.wait(600_000); // wait 10 minutes at most
        } catch (final Exception e) {
            // ... handle timeout
        }
    }

    if (!uploadFuture.isSuccess())
        // ... handle failure
    }

    // ... wait for ingestion, then process video
```

### Process video
```java
    final ProcessVideoRequest processVideoRequest = new ProcessVideoRequest.Builder()
         .withMediaContainerCodec(new MediaContainerCodec(CONTAINER.QUICKTIME_MOV,
                                                          CODEC.APPLE_PRORES))
         .withAppleProResProfile(APPLE_PRORES_PROFILE.PROFILE_STANDARD)
         .withDenoiser(DE_NOISER.PIXOP_DENOISER)
         .withScaler(SCALER.PIXOP_SUPER_RESOLUTION)
         .withResolution(new Resolution(RESOLUTION_TAG.RESOLUTION_HD_1080P))
         .withClarityBoost(CLARITY_BOOST.HIGH)
         .withFrameRateConverter(FRAME_RATE_CONVERTER.FRAME_BLENDING)
         .withFrameRate(new FrameRate(FRAME_RATE_TAG.FRAME_RATE_VIDEO_HD_FAST))
         .withPostProcessor(POST_PROCESSOR.PIXOP_FILM_GRAIN)
         .withRange(new Range.Builder()
                             .withStartPositionMilliseconds(0)
                             .withEndPositionMilliseconds(5_000)
                             .build())
         .build();

    final ProcessVideoResponse processVideoResponse = videosServiceClient.processVideo(videoId,
                                                                                       processVideoRequest,
                                                                                       jwtTokenString);
    // ... wait for processing to complete                                              
```

### Wait for processing to complete
```java
    // NB: similar code can be used to wait for ingestion
    final String newVideoId = processVideoResponse.getProcessedVideoId();

    while (true) {
        final ProcessVideoCheckProgressResponse checkProgressResponse = videosServiceClient.processVideoCheckProgress(newVideoId, jwtTokenString);

        final VideoProcessingState processingState = checkProgressResponse.getProcessingState();
        if (processingState != null && processingState.getProcessingStatus() != null) {
            if (processingState.getProcessingStatus().equalsIgnoreCase("DONE")) {
                // ... handle success (e.g. download processed video)
                break;
            } else if (processingState.getProcessingStatus().equalsIgnoreCase("ERROR")) {
                // ... handle error
                break;
            }
        }

        try {
            Thread.sleep(10_000); // wait 10 secs
        } catch (final InterruptedException e) {}
    }
```

### Download processed video
```java
    // callback class for handling success, exception and failure
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
            // ... handle success
        }

        @Override
        public void onException(final Exception exception) {
            // ... handle exception
        }

        @Override
        public void onFailure(final int status, final String resultMessage) {
            // ... handle failure
        }
    }

    // main download logic
    final String localFileName = "small_processed.mp4";
    final FileOutputStream outputStream = new FileOutputStream(new File(localFileName));

    final DownloadFuture downloadFuture = new TestDownloadFuture(videoId,
                                                                 localFileName);

    mediaServiceClient.downloadProcessedMedia(videoId,
                                              jwtTokenString,
                                              outputStream,
                                              downloadFuture);

    synchronized(downloadFuture) {
        try {
            downloadFuture.wait(600_000); // wait 10 minutes at most
        } catch (final Exception e) {
            // ... handle timeout
        }
    }

    if (!downloadFuture.isSuccess())
        // ... handle failure
    }
```

### Clean up video and project
```java
    // note: also deletes any processed media
    videosServiceClient.deleteVideo(videoId, jwtTokenString);

    // deleting the project is possible when all video has been removed
    videosServiceClient.deleteProject(projectId, jwtTokenString);
```

## Test programs
Four main test programs are included which show how to perform various common tasks (in `src/com/pixop/sdk/services`):

- **Authentication**: `/accounts/TestAccountsService.java`
- **CRUD on projects and videos**: `/videos/TestVideosService.java`
- **Process a video**: `/videos/TestProcessVideo.java`
- **Upload/download a media file**: `/media/TestMediaService.java`

Your Pixop account credentials must be set as environment variables in `PIXOP_API_EMAIL` and `PIXOP_API_PASSWORD` respectively before running any of these.

## REST endpoints documentation

https://www.pixop.com/pixop-api/docs
