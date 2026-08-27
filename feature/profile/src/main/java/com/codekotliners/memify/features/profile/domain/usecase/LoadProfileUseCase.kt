package com.codekotliners.memify.features.profile.domain.usecase

import com.codekotliners.memify.features.profile.domain.model.ProfileAccount
import com.codekotliners.memify.features.profile.domain.model.ProfileSnapshot
import com.codekotliners.memify.features.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class LoadProfileUseCase @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(): ProfileSnapshot {
        val account = repository.getAccount()
        if (account is ProfileAccount.Guest) {
            return ProfileSnapshot(
                account = account,
                likedMemes = emptyList(),
            )
        }

        return try {
            ProfileSnapshot(
                account = account,
                likedMemes = repository.getLikedMemes(),
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (_: Exception) {
            ProfileSnapshot(
                account = account,
                likedMemes = emptyList(),
                likedMemesLoadFailed = true,
            )
        }
    }
}
