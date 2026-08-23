package com.codekotliners.memify

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.vk.id.VKID
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

@HiltAndroidApp
class MemifyApplication : Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        VKID.init(this)
        VKID.instance.setLocale(Locale("ru"))
    }

    // Дефолтный ImageLoader Coil сам создаёт OkHttpClient с таймаутами по 10 секунд — на
    // эмуляторе (у него медленная/нестабильная виртуальная сеть, особенно когда грид
    // шаблонов пытается загрузить сразу 8-9 картинок параллельно) этого иногда не хватает,
    // и картинки падают в error-состояние, хотя точно такой же URL спокойно открывается
    // в браузере по одному запросу за раз.
    //
    // Плюс включаем DebugLogger — Coil сам пишет в Logcat (тег "Coil"), какая именно
    // картинка и почему не загрузилась (сеть/декодирование/что угодно ещё), без необходимости
    // руками искать нужный запрос в Network Inspector.
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .build()
            }
            .logger(DebugLogger())
            .build()
}
