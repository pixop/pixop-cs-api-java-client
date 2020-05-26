# pixop-cs-api-java-client
Java 8 client SDK for Pixop Video Processing REST API

## Requirements
- A valid Pixop staging environment account (contact support@pixop.com)
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
      <version>1.0.0</version>
    </dependency>
```

Gradle Groovy DSL:
```
implementation 'com.pixop:pixop-api-sdk:1.0.0'
```

## Essential concepts

### Configure all service clients
```java
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
```

### Authenticate and acquire token
```java
    final NewAuthToken newToken = accountsServiceClient.newToken(email,
                                                                 password,
                                                                 teamId);
    final String jwtTokenString = newToken.getJwtTokenString();
    // ... do something useful with token
```

### Look up video
```java    
    final Video video = videosServiceClient.getVideo(videoId, jwtTokenString).getVideo();
    // ... do something useful with video
```

## Test programs
Four main test programs are included which show how to perform various common tasks (in `src/com/pixop/sdk/services`):

- **Authentication**: `/accounts/TestAccountsService.java`
- **CRUD on projects and videos**: `/videos/TestVideosService.java`
- **Process a video**: `/videos/TestProcessVideo.java`
- **Upload/download a media file**: `/media/TestMediaService.java`

Your Pixop account credentials must be set as environment variables in `PIXOP_API_EMAIL` and `PIXOP_API_PASSWORD` respectively before running any of these.

## REST endpoints documentation

https://static.pixop.com/documentation/Pixop_Video_Processing_REST_API_staging.html
