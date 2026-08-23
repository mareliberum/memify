package com.codekotliners.memify.features.templates.data.mappers

import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.core.network.models.TemplateDto

fun TemplateDto.toTemplate(): Template =
    Template(
        id = id,
        name = name,
        url = url,
        width = width,
        height = height,
        isFavourite = isFavourite,
    )
