import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

plugins {
    id("com.android.application")
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
val targetSdkVersion =
    libs
        .findVersion("targetSdk")
        .get()
        .requiredVersion
        .toInt()
val javaVersion = JavaVersion.toVersion(libs.findVersion("javaTarget").get().requiredVersion)

extensions.configure<ApplicationExtension> {
    compileSdk = compileSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        targetSdk = targetSdkVersion
    }

    compileOptions {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies.add(
    "coreLibraryDesugaring",
    libs.findLibrary("desugar-jdk-libs").get(),
)
