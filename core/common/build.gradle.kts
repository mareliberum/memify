plugins {
    id("memify.android.library")
}

android {
    namespace = "com.codekotliners.memify.core.common"
}

dependencies {
    // Firebase Crashlytics убран — Logger теперь пишет только в logcat (см. Logger.kt).
}
