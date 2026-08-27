package com.codekotliners.memify.features.home.domain.usecase

import com.codekotliners.memify.features.home.domain.model.ToggleHomePostLikeResult
import com.codekotliners.memify.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class ToggleHomePostLikeUseCase @Inject constructor(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(postId: String): ToggleHomePostLikeResult {
        if (!repository.isLoggedIn()) {
            return ToggleHomePostLikeResult.AuthenticationRequired
        }

        return repository.toggleLike(postId)
    }
}
