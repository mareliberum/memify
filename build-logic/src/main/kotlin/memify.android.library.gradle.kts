import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.library")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val compileSdkVersion =
    libs
        .findVersion("compileSdk")
        .get()
        .requiredVersion
        .toInt()
val minSdkVersion =
    libs
        .findVersion("minSdk")
        .get()
        .requiredVersion
        .toInt()
val javaVersion = JavaVersion.toVersion(libs.findVersion("javaTarget").get().requiredVersion)

extensions.configure<LibraryExtension> {
    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}
