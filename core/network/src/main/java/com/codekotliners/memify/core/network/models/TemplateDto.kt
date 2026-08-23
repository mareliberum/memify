package com.codekotliners.memify.core.network.models

import kotlinx.serialization.Serializable

// Соответствует TemplateDto в backend/src/main/kotlin/routes/TemplatesRoutes.kt

@Serializable
data class TemplateDto(
    val id: String,
    val name: String,
    val url: String,
    val width: Int,
    val height: Int,
    val isFavourite: Boolean,
)
