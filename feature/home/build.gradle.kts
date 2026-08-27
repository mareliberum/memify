plugins {
    id("memify.android.library")
    id("memify.android.compose")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.features.home"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.user)
    implementation(projects.core.prefs)
    implementation(projects.core.navigation)
    implementation(projects.core.ui)
    implementation(projects.feature.auth)
    implementation(projects.feature.profile)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.base)
}
