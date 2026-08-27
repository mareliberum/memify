plugins {
    `kotlin-dsl`
}

group = "com.codekotliners.memify.buildlogic"

dependencies {
    implementation(libs.build.android.gradle.plugin)
    implementation(libs.build.kotlin.compose.gradle.plugin)
    implementation(libs.build.ksp.gradle.plugin)
    implementation(libs.build.hilt.gradle.plugin)
}
