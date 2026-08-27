plugins {
    id("memify.android.library")
    id("memify.android.compose")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.features.templates"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.prefs)
    implementation(projects.core.ui)
    implementation(projects.feature.auth)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil.compose)
    implementation(libs.coil.base)

    // VK SDK — не Firebase, отдельная интеграция (см. пояснение в чате про ВК), не трогаем.
    implementation(libs.vk.android.sdk.core)
    implementation(libs.vk.android.sdk.api)
    implementation(libs.vkid.sdk.support)
}
