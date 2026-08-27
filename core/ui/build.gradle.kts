plugins {
    id("memify.android.library")
    id("memify.android.compose")
}

android {
    namespace = "com.codekotliners.memify.core.ui"
}

dependencies {
    implementation(projects.core.navigation)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.coil.compose)
}
