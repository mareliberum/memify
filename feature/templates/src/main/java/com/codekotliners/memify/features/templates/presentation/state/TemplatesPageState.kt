package com.codekotliners.memify.features.templates.presentation.state

import com.codekotliners.memify.core.models.Template

data class TemplatesPageState(
    val refreshing: Boolean,
    val selectedTab: Tab,
    val bestTemplatesState: TabState = TabState.None,
    val favouriteTemplatesState: TabState = TabState.None,
    val newTemplatesState: TabState = TabState.None,
    val vkTemplatesState: TabState = TabState.None,
) {
    fun getTabs(): List<Tab> = Tab.entries.toList()

    fun getTabState(tab: Tab): TabState =
        when (tab) {
            Tab.BEST -> bestTemplatesState
            Tab.NEW -> newTemplatesState
            Tab.FAVOURITE -> favouriteTemplatesState
            Tab.VK_IMAGES -> vkTemplatesState
        }

    fun getCurrentTabState(): TabState = getTabState(selectedTab)

    fun getIsLoadingMoreByState(state: TabState): Boolean =
        when (state) {
            is TabState.Content -> state.isLoadingMore
            else -> false
        }

    fun getReachedEndByState(state: TabState): Boolean =
        when (state) {
            is TabState.Content -> state.reachedEnd
            else -> false
        }

    fun getTemplatesByState(state: TabState): List<Template> {
        return when (state) {
            is TabState.Content -> {
                return state.templates
            }
            else -> emptyList<Template>()
        }
    }

    fun getTemplatesOfSelectedState(): List<Template> {
        val state = getCurrentTabState()
        return getTemplatesByState(state)
    }

    fun updatedTabState(
        tab: Tab,
        newState: TabState,
    ): TemplatesPageState =
        when (tab) {
            Tab.BEST -> copy(bestTemplatesState = newState)
            Tab.NEW -> copy(newTemplatesState = newState)
            Tab.FAVOURITE -> copy(favouriteTemplatesState = newState)
            Tab.VK_IMAGES -> copy(vkTemplatesState = newState)
        }

    fun updatedCurrentTabState(newState: TabState): TemplatesPageState = updatedTabState(selectedTab, newState)

    fun updatedTabContent(
        tab: Tab,
        newTemplates: List<Template>,
        loadMode: Boolean? = null,
    ): TemplatesPageState {
        val updatedContent = { currentState: TabState ->
            val currentTemplates = getTemplatesByState(currentState)
            TabState.Content(
                currentTemplates + (newTemplates - currentTemplates),
                loadMode ?: getIsLoadingMoreByState(currentState),
                false,
            )
        }

        return updatedTabState(
            tab,
            updatedContent(getTabState(tab)),
        )
    }

    fun updatedCurrentContent(
        newTemplates: List<Template>,
        loadMode: Boolean? = null,
    ): TemplatesPageState = updatedTabContent(selectedTab, newTemplates, loadMode)
}
