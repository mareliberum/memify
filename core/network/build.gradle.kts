plugins {
    id("memify.android.library")
    id("memify.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.codekotliners.memify.core.network"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.prefs)

    // HttpClient и io.ktor.client.request/*.http.* нужны модулям,
    // которые зависят от core:network, поэтому здесь api, а не implementation.
    api(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
}
