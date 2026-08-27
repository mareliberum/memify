package com.codekotliners.memify.features.settings.presentation.model

sealed interface SettingsMessage {
    data object AccountLoadFailed : SettingsMessage

    data object NameUpdated : SettingsMessage

    data object NameUpdateFailed : SettingsMessage

    data object SignedOut : SettingsMessage

    data object SignOutFailed : SettingsMessage

    data object VkNameUnavailable : SettingsMessage
}
