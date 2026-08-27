plugins {
    id("memify.android.application")
    id("memify.android.compose")
    id("memify.android.hilt")
    id("org.jlleitschuh.gradle.ktlint") version "14.2.0"
    id("com.vk.vkompose") version "0.8.0"
    id("vkid.manifest.placeholders")
}
android {
    namespace = "com.codekotliners.memify"

    defaultConfig {
        applicationId = "com.codekotliners.memify"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    packaging {
        resources {
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
        }
    }
}

ktlint {
    debug = true
    verbose = true
}

dependencies {
    // Core modules
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:prefs"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:user"))

    // Feature modules
    implementation(project(":feature:confirmation"))
    implementation(project(":feature:passwordrecovery"))
    implementation(project(":feature:passwordupdate"))
    implementation(project(":feature:viewer"))
    implementation(project(":feature:auth"))
    implementation(project(":feature:templates"))
    implementation(project(":feature:settings"))
    implementation(project(":feature:profile"))
    implementation(project(":feature:create"))
    implementation(project(":feature:home"))

    // Androidx Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.espresso.core)
    implementation(libs.coil.base)
    implementation(libs.androidx.browser)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Compose + UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Architecture Components
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Google Sign-In (play-services-auth) — не Firebase, см. feature:auth.
    implementation(libs.play.services.auth)

    // Collections
    implementation(libs.kotlinx.collections.immutable)

    // VKID
    implementation("com.vk.id:vkid:2.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.vk.id:onetap-compose:2.5.1")
    implementation("com.vk.id:vk-sdk-support:2.5.1")
    implementation("com.vk:android-sdk-core:4.1.0")
    implementation("com.vk:android-sdk-api:4.1.0")
}

vkompose {

    skippabilityCheck {

        strongSkipping {
            // Fail compilation if there is any problem with strong skipping mode
            strongSkippingFailFastEnabled = false // false by default
        }
    }

    recompose {
        isHighlighterEnabled = true
        isLoggerEnabled = true
    }

    testTag {
        isApplierEnabled = true
        isDrawerEnabled = false
        isCleanerEnabled = false

        isApplierEnabled = true
    }

    sourceInformationClean = true
}

// Firebase убран полностью: плагины com.google.firebase.crashlytics и
// com.google.gms.google-services больше не применяются, google-services.json не используется.
