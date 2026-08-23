package com.codekotliners.memify.core.network.api

object ApiConfig {
    // 10.0.2.2 — специальный адрес, по которому Android-эмулятор видит localhost компьютера,
    // на котором запущен backend (ktor слушает :8080, см. папку backend/ рядом с этим проектом).
    // На реальном устройстве по Wi-Fi замени на "http://<IP компьютера в локальной сети>:8080/",
    // а после деплоя бэкенда — на его публичный адрес (например, из Yandex Cloud).
    var baseUrl: String = "http://10.0.2.2:8080/"
}
