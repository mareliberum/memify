plugins {
    id("memify.android.library")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.core.user"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.prefs)
}
