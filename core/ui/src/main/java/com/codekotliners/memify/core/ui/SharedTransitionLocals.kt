package com.codekotliners.memify.core.ui

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.compositionLocalOf

/**
 * Shared-element transition scopes used across screens (e.g. home feed post -> image viewer).
 * Provided once at the composition root (App.kt, inside SharedTransitionLayout/NavHost) and
 * consumed by any screen/component participating in the shared transition. Moved here from
 * App.kt so that feature modules using it (e.g. :feature:viewer) don't have to depend on :app.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
val LocalSharedTransitionScope = compositionLocalOf<SharedTransitionScope?> { null }

val LocalNavAnimatedVisibilityScope = compositionLocalOf<AnimatedVisibilityScope?> { null }
