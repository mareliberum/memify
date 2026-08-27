package com.codekotliners.memify.features.settings.data.repository

import com.codekotliners.memify.features.settings.data.datasource.AboutAppDataSource
import com.codekotliners.memify.features.settings.data.mapper.toDomain
import com.codekotliners.memify.features.settings.domain.model.AboutAppInfo
import com.codekotliners.memify.features.settings.domain.repository.AboutAppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultAboutAppRepository @Inject constructor(
    private val dataSource: AboutAppDataSource,
) : AboutAppRepository {
    override fun getInfo(): AboutAppInfo = dataSource.getInfo().toDomain()
}
