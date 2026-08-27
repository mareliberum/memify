plugins {
    id("memify.android.library")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.core.prefs"
}

dependencies {
    // EncryptedSharedPreferences для TokenStore (свои JWT-токены вместо Firebase Auth).
    implementation(libs.androidx.security.crypto)
}
