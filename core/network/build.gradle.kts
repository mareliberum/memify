plugins {
    id("memify.android.library")
    id("memify.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.codekotliners.memify.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:prefs"))

    api(libs.ktor.client.core) // HttpClient и io.ktor.client.request/*.http.* нужны модулям, которые зависят от core:network (core:user, feature:auth, feature:templates и т.д.), поэтому api, а не implementation
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
}
