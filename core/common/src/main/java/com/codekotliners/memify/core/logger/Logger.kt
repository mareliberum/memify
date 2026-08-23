package com.codekotliners.memify.core.logger

import android.util.Log

// Раньше писал ошибки в Firebase Crashlytics. Firebase убран из проекта — теперь просто
// пишет в logcat. Если понадобится сторонний краш-репортинг, сюда можно подключить
// что угодно (например, Yandex AppMetrica Crashes).
object Logger {
    enum class Level {
        DEBUG,
        INFO,
        WARNING,
        ERROR,
    }

    fun initialize(isDebug: Boolean) {
        // no-op: раньше тут включался/выключался сбор крашей в Firebase Crashlytics
    }

    fun log(
        level: Level,
        tag: String = "APP_LOG",
        message: String,
        exception: Throwable? = null,
    ) {
        when (level) {
            Level.DEBUG -> Log.d(tag, message)
            Level.INFO -> Log.i(tag, message)
            Level.WARNING -> Log.w(tag, message)
            Level.ERROR -> Log.e(tag, message, exception)
        }
    }

    fun logInfo(message: String, tag: String = "APP_INFO") = log(Level.INFO, tag, message)

    fun logDebug(message: String, tag: String = "APP_DEBUG") = log(Level.DEBUG, tag, message)

    fun logWarning(message: String, tag: String = "APP_WARNING") = log(Level.WARNING, tag, message)

    fun logError(message: String, exception: Throwable? = null, tag: String = "APP_ERROR") =
        log(Level.ERROR, tag, message, exception)
}
