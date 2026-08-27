package com.codekotliners.memify.features.profile.data.repository

import com.codekotliners.memify.features.profile.data.datasource.ProfileLocalDataSource
import com.codekotliners.memify.features.profile.data.datasource.ProfileRemoteDataSource
import com.codekotliners.memify.features.profile.data.mapper.toDomain
import com.codekotliners.memify.features.profile.domain.model.ProfileAccount
import com.codekotliners.memify.features.profile.domain.model.ProfileMeme
import com.codekotliners.memify.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultProfileRepository @Inject constructor(
    private val localDataSource: ProfileLocalDataSource,
    private val remoteDataSource: ProfileRemoteDataSource,
) : ProfileRepository {
    override fun observeCreatedMemes(): Flow<List<ProfileMeme>> =
        localDataSource.observeCreatedMemes().map { memes ->
            memes.map { meme -> meme.toDomain() }
        }

    override suspend fun getAccount(): ProfileAccount =
        if (localDataSource.isLoggedIn()) {
            remoteDataSource.getAccount().toDomain()
        } else {
            ProfileAccount.Guest
        }

    override suspend fun getLikedMemes(): List<ProfileMeme> =
        remoteDataSource.getLikedMemes().map { meme -> meme.toDomain() }

    override suspend fun updateAvatar(imageUri: String): String =
        remoteDataSource.updateAvatar(imageUri)
}
