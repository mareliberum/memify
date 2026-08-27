plugins {
    id("memify.android.library")
    id("memify.android.compose")
}

android {
    namespace = "com.codekotliners.memify.core.navigation"
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.compose)
}
