package com.codekotliners.memify.core.prefs.di

import com.codekotliners.memify.core.prefs.SharedPreferencesThemePreferences
import com.codekotliners.memify.core.prefs.ThemePreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ThemePreferencesModule {
    @Binds
    @Singleton
    abstract fun bindThemePreferences(implementation: SharedPreferencesThemePreferences): ThemePreferences
}
