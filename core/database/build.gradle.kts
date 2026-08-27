plugins {
    id("memify.android.library")
    id("memify.android.hilt")
}

android {
    namespace = "com.codekotliners.memify.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Used by DraftsRepositoryImpl to download and save images locally.
    implementation(libs.coil.base)
}
