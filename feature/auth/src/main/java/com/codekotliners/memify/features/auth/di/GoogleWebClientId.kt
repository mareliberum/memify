package com.codekotliners.memify.features.auth.di

import javax.inject.Qualifier

/**
 * Qualifier for the OAuth web client id used for Google Sign-In.
 *
 * The actual value comes from `R.string.default_web_client_id`, which is generated
 * by the google-services Gradle plugin only in the `:app` module (the only module
 * with `google-services.json` applied). This feature module cannot read that
 * generated resource directly (module-scoped R classes), so `:app` provides the
 * value through this qualifier instead.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleWebClientId
