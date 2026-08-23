package com.codekotliners.memify.features.templates.data.datasource

import com.codekotliners.memify.core.network.api.ApiConfig
import com.codekotliners.memify.core.network.api.ApiException
import com.codekotliners.memify.core.network.api.authorizedRequest
import com.codekotliners.memify.core.network.models.TemplateDto
import com.codekotliners.memify.core.prefs.TokenStore
import com.codekotliners.memify.features.templates.data.mappers.toTemplate
import com.codekotliners.memify.features.templates.domain.datasource.DatasourceResult
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesDatasource
import com.codekotliners.memify.features.templates.domain.datasource.TemplatesFilter
import com.codekotliners.memify.features.templates.exceptions.UnauthorizedActionException
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * REST-реализация вместо старой [FirebaseTemplatesDatasource] (Cloud Firestore).
 *
 * У бэка нет постраничной выдачи по курсору (раньше это были DocumentSnapshot из Firestore) —
 * есть только ?limit=. Поэтому пагинация тут эмулируется: на каждый "догрузить ещё" запрашиваем
 * limit = (уже загружено) + pageSize и берём только новый хвост. Немного избыточно по трафику
 * при глубокой прокрутке, но не требует от бэка ничего лишнего (см. GET /templates в TemplatesRoutes.kt).
 */
class TemplatesRestDatasource @Inject constructor(
    private val httpClient: HttpClient,
    private val tokenStore: TokenStore,
) : TemplatesDatasource<Int> {
    override suspend fun getFilteredTemplates(
        type: TemplatesFilter,
        limit: Long,
        startWith: Int?,
    ): DatasourceResult<Int> {
        val alreadyFetched = startWith ?: 0
        val fetchLimit = alreadyFetched + limit

        val sortParam = if (type is TemplatesFilter.New) "new" else "best"
        val favouritesOnly = type is TemplatesFilter.Favorites

        if (favouritesOnly && type.userId == null) {
            throw UnauthorizedActionException("User not logged in")
        }

        val dtos: List<TemplateDto> =
            try {
                httpClient.authorizedRequest(tokenStore) {
                    method = HttpMethod.Get
                    url(ApiConfig.baseUrl + "templates")
                    parameter("limit", fetchLimit)
                    parameter("sort", sortParam)
                    if (favouritesOnly) parameter("favourites", true)
                }
            } catch (e: ApiException) {
                if (e.statusCode == 401) throw UnauthorizedActionException("User not logged in") else throw e
            }

        val newItems = dtos.drop(alreadyFetched)
        val nextStart = if (newItems.size.toLong() < limit) null else alreadyFetched + newItems.size

        return DatasourceResult(
            data = flow { newItems.forEach { emit(it.toTemplate()) } },
            nextToStart = nextStart,
        )
    }

    override suspend fun toggleLikeById(id: String): Boolean {
        val result: Map<String, Boolean> =
            httpClient.authorizedRequest(tokenStore) {
                method = HttpMethod.Post
                url(ApiConfig.baseUrl + "templates/$id/toggle-like")
            }
        return result["isFavourite"] ?: false
    }
}
