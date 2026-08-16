package com.codekotliners.memify.core.models

/**
 * Исходные данные, из которых создаётся локальный черновик (Draft): что скачать
 * и сохранить на устройство. Раньше для этого использовался мок-класс MockMeme
 * из features.home.mocks — заменено на нормальный доменный тип, чтобы core не
 * зависел от тестовых данных конкретной фичи.
 */
data class DraftSource(
    val id: String,
    val url: String,
)
