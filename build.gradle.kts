import java.util.Properties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
    // Firebase полностью убран из проекта — плагины com.google.gms.google-services и
    // com.google.firebase.crashlytics больше не регистрируются вообще (раньше тут были
    // объявлены с apply false и нигде не применялись — теперь их просто нет).
}

vkidManifestPlaceholders {
    fun error() =
        logger.error(
            "Warning! Build will not work!" +
                "\nCreate the 'secrets.properties' file in the 'sample/app' folder" +
                " and add your 'VKIDClientID' and 'VKIDClientSecret' to it.",
        )

    val properties = Properties()
    properties.load(file("app/secrets.properties").inputStream())
    val clientId = properties["VKIDClientID"] ?: error()
    val clientSecret = properties["VKIDClientSecret"] ?: error()

    init(
        clientId = clientId.toString(),
        clientSecret = clientSecret.toString(),
    )
    vkidRedirectHost = "vk.com"
    vkidRedirectScheme = "vk53583099"
}
