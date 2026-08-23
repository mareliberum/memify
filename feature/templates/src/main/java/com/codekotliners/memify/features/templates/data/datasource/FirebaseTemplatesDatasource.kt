package com.codekotliners.memify.features.templates.data.datasource

// УСТАРЕЛО: старая реализация на Cloud Firestore. Заменена на TemplatesRestDatasource.kt,
// который ходит в собственный бэкенд по REST (GET /templates, POST /templates/{id}/toggle-like).
// Этот файл больше нигде не используется (см. di/DatasourceModule.kt) и его можно удалить
// вручную — сессия, в которой это писалось, не может сама удалять файлы на диске.
