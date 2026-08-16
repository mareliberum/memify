package com.codekotliners.memify.core.repositories.draft

import com.codekotliners.memify.core.models.Draft
import com.codekotliners.memify.core.models.DraftSource
import kotlinx.coroutines.flow.Flow

interface DraftsRepository {
    suspend fun getDrafts(): Flow<List<Draft>>

    suspend fun saveDraft(draft: DraftSource)
}
