# EnglishLog Android

Kotlin + Jetpack Compose + Retrofit + kotlinx.serialization Android client. Backend is shared with `../Backend`.

## Open in Android Studio

1. Install Android Studio Ladybug (2024.2.1) or newer with Android SDK 35 and JDK 17/21.
2. Open this `Android` directory (the folder that contains `settings.gradle.kts`), not the parent `WorkEnglish` folder.
3. Let Gradle sync, then run the `app` configuration on an emulator or device.

The project uses Android Gradle Plugin 8.13.2, Gradle 8.14.5, Kotlin 2.0.21 and the Compose compiler plugin. `gradle.properties` points Gradle to Temurin 21 on this Mac. On another computer, remove `org.gradle.java.home` and select Android Studio's Embedded JDK (17+). Gradle repositories include Aliyun mirrors first because Google Maven TLS from this network is unstable.

## Run the app

Start Backend first (`../Backend`, default port `5050`).

```bash
./gradlew assembleDebug
```

The default API is `http://10.0.2.2:5050/` (Android emulator loopback to the host). For a physical device, change `ApiFactory.DEFAULT_BASE_URL` to the computer's LAN IP. Cleartext HTTP is allowed only for local development.

Default demo account from the Web client:

- email: `demo@example.com`
- password: `password123`

Register once if the account does not exist yet.

## Deploy

Create a release keystore, switch the API to HTTPS, store the token in DataStore/Keystore, enable minification, configure Google OAuth client IDs, and upload a signed AAB. Image upload is already on the backend; extend `Api.kt` with a multipart endpoint and Android Photo Picker for production.
