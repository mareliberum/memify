package com.codekotliners.memify.features.templates.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codekotliners.memify.core.models.Template
import com.codekotliners.memify.core.network.api.ApiException
import com.codekotliners.memify.features.templates.domain.repository.TemplatesRepository
import com.codekotliners.memify.features.templates.exceptions.UnauthorizedActionException
import com.codekotliners.memify.features.templates.exceptions.VKUnauthorizedActionException
import com.codekotliners.memify.features.templates.presentation.state.ErrorType
import com.codekotliners.memify.features.templates.presentation.state.Tab
import com.codekotliners.memify.features.templates.presentation.state.TabState
import com.codekotliners.memify.features.templates.presentation.state.TemplatesPageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

// Тот же текст, что и templates_network_error_message в strings.xml этого модуля — используем здесь напрямую,
// т.к. у ViewModel нет Context для getString().
private const val NETWORK_ERROR_MESSAGE = "Проверьте подключение к сети и попробуйте ещё раз"

@HiltViewModel
class TemplatesFeedViewModel @Inject constructor(
    private val repository: TemplatesRepository,
) : ViewModel() {
    private val _pageState = MutableStateFlow(TemplatesPageState(refreshing = false, selectedTab = Tab.BEST))
    val pageState: StateFlow<TemplatesPageState> = _pageState

    private val loadingTabs = mutableSetOf<Tab>()
    private val refreshingTabs = mutableSetOf<Tab>()

    val limitPerRequest: Long = 30

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    init {
        refresh()
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun onLikeToggle(id: String) {
        viewModelScope.launch {
            var res =
                try {
                    repository.toggleLike(id)
                } catch (e: UnauthorizedActionException) {
                    _toastMessage.value = e.message
                    return@launch
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    // сервер недоступен/нет сети — не крашимся, показываем тост вместо этого
                    _toastMessage.value = NETWORK_ERROR_MESSAGE
                    return@launch
                }

            _pageState.update {
                it.updatedCurrentTabState(
                    TabState.Content(
                        it.getTemplatesOfSelectedState().map {
                            if (it.id == id) {
                                it.copy(
                                    isFavourite = res,
                                )
                            } else {
                                it
                            }
                        },
                        it.getIsLoadingMoreByState(it.getCurrentTabState()),
                        it.getReachedEndByState(it.getCurrentTabState()),
                    ),
                )
            }
        }
    }

    fun refresh() {
        loadDataForTab(_pageState.value.selectedTab, refresh = true)
    }

    fun selectTab(tab: Tab) {
        _pageState.update {
            it.copy(
                selectedTab = tab,
                refreshing = tab in refreshingTabs,
            )
        }
        if (_pageState.value.getTabState(tab) is TabState.None) {
            loadDataForTab(tab)
        }
    }

    fun loadDataForTab(tab: Tab) {
        loadDataForTab(tab, refresh = false)
    }

    @Suppress("detekt.LongMethod", "detekt.CyclomaticComplexMethod")
    private fun loadDataForTab(
        tab: Tab,
        refresh: Boolean,
    ) {
        val currentState = _pageState.value.getTabState(tab)
        if (tab in loadingTabs) {
            return
        }
        if (!refresh &&
            currentState is TabState.Content &&
            (currentState.isLoadingMore || currentState.reachedEnd)
        ) {
            return
        }

        loadingTabs += tab
        if (refresh) {
            refreshingTabs += tab
        }

        if (refresh || currentState !is TabState.Content) {
            _pageState.update {
                it.updatedTabState(tab, TabState.Loading).copy(
                    refreshing = it.selectedTab in refreshingTabs,
                )
            }
        } else {
            _pageState.update {
                it.updatedTabState(
                    tab,
                    TabState.Content(
                        it.getTemplatesByState(currentState),
                        true,
                        false,
                    ),
                )
            }
        }

        viewModelScope.launch {
            // dataFlow оборачиваем в flow { emitAll(...) }, а не берём Flow из репозитория
            // напрямую: getBestTemplates/getNewTemplates/... — suspend-функции, и сам их вызов
            // (например неудачный HTTP-запрос) может бросить исключение ДО появления Flow —
            // то есть вне зоны действия .catch{} ниже. Раньше такое исключение просто роняло
            // корутину молча, состояние навсегда оставалось Loading ("вечная загрузка").
            // Обернув suspend-вызов в flow { emitAll(...) }, мы делаем его ленивым: он выполнится
            // только при сборе dataFlow, то есть уже под защитой .onEmpty{}/.catch{} ниже.
            val dataFlow =
                flow {
                    val templatesFlow =
                        when (tab) {
                            Tab.BEST ->
                                repository.getBestTemplates(limit = limitPerRequest, refresh = refresh)

                            Tab.NEW ->
                                repository.getNewTemplates(limit = limitPerRequest, refresh = refresh)

                            Tab.FAVOURITE ->
                                repository.getFavouriteTemplates(limit = limitPerRequest, refresh = refresh)

                            Tab.VK_IMAGES ->
                                repository.getVkTemplates(limit = limitPerRequest, refresh = refresh)
                        }
                    emitAll(templatesFlow)
                }

            val buffer = mutableListOf<Template>()
            dataFlow
                .onEmpty {
                    if (!refresh && currentState is TabState.Content && currentState.templates.isNotEmpty()) {
                        _pageState.update {
                            it.updatedTabState(
                                tab,
                                TabState.Content(
                                    currentState.templates,
                                    false,
                                    true,
                                ),
                            )
                        }
                    } else {
                        _pageState.update {
                            it.updatedTabState(
                                tab,
                                TabState.Empty,
                            )
                        }
                    }
                }.catch { e ->
                    if (e is CancellationException) {
                        throw e
                    }
                    val errorType =
                        when (e) {
                            is UnauthorizedActionException -> ErrorType.NEED_LOGIN
                            is VKUnauthorizedActionException -> ErrorType.NEED_LINK_VK
                            is IOException, is ApiException -> ErrorType.NETWORK
                            else -> ErrorType.UNKNOWN
                        }

                    _pageState.update {
                        it.updatedTabState(tab, TabState.Error(errorType))
                    }
                }.collect { template ->
                    buffer += template
                }

            if (buffer.isNotEmpty()) {
                _pageState.update {
                    it.updatedTabContent(
                        tab,
                        buffer.toList(),
                        false,
                    )
                }
            }

            finishLoading(tab, refresh)
        }
    }

    private fun finishLoading(
        tab: Tab,
        refresh: Boolean,
    ) {
        loadingTabs -= tab
        if (refresh) {
            refreshingTabs -= tab
        }
        viewModelScope.launch {
            delay(200)
            _pageState.update {
                it.copy(refreshing = it.selectedTab in refreshingTabs)
            }
        }
    }
}
