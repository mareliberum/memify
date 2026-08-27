plugins {
    id("memify.android.library")
}

android {
    namespace = "com.codekotliners.memify.core.model"
}

dependencies {
    // Нужен только для аннотации @Immutable на Template.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
}
