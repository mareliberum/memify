plugins {
    id("memify.android.library")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.core.user"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:prefs"))
}
