package com.codekotliners.memify.features.settings.domain.repository

import com.codekotliners.memify.features.settings.domain.model.AboutAppInfo

interface AboutAppRepository {
    fun getInfo(): AboutAppInfo
}
