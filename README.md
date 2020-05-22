# pixop-cs-api-java-client
Java 8 client SDK for Pixop Video Processing REST API

## Build .jar
Build the client .jar via Maven (`mvn clean install`) or Gradle (`./gradlew build`).

## Test programs
Four main test programs are included which show how to perform various common tasks (in `src/com/pixop/sdk/services`):

- **Authentication**: `/accounts/TestAccountsService.java`
- **CRUD on projects and videos**: `/videos/TestVideosService.java`
- **Process a video**: `/videos/TestProcessVideo.java`
- **Upload/download a media file**: `/media/TestMediaService.java`

Your Pixop account credentials must be set as environment variables in `PIXOP_API_EMAIL` and `PIXOP_API_PASSWORD` respectively before running any of these.

## REST endpoint documentation

https://static.pixop.com/documentation/Pixop_Video_Processing_REST_API_staging.html
