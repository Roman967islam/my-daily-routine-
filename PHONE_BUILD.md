# My Daily Routine — phone-only APK build

## Build with GitHub Actions
1. Create a GitHub repository and upload this project.
2. Open the repository's **Actions** tab.
3. Select **Build My Daily Routine APK**.
4. Tap **Run workflow** (or push to `main` to trigger it automatically).
5. When the run finishes, open the run and download the **MyDailyRoutine-debug-apk** artifact.
6. Extract the downloaded ZIP and install `app-debug.apk` on your Android phone.

The workflow uses Java 17 and Gradle 8.9 on GitHub's cloud runner, so Android Studio/AndroidIDE is not required on the phone.
