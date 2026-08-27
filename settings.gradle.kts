import java.net.URI

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/maven/")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vk-id-captcha/android/")
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = URI("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        }
        maven {
            url = URI("https://artifactory-external.vkpartner.ru/artifactory/maven/")
        }
        maven {
            url = URI("https://artifactory-external.vkpartner.ru/artifactory/vk-id-captcha/android/")
        }
    }
}

rootProject.name = "Memify"
include(":app")
include(":core:model")
include(":core:common")
include(":core:database")
include(":core:network")
include(":core:prefs")
include(":core:navigation")
include(":core:ui")
include(":core:user")
include(":feature:confirmation")
include(":feature:passwordrecovery")
include(":feature:passwordupdate")
include(":feature:viewer")
include(":feature:auth")
include(":feature:templates")
include(":feature:settings")
include(":feature:profile")
include(":feature:create")
include(":feature:home")
