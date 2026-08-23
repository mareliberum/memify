package com.codekotliners.memify.core.network.postsdatasource

// УСТАРЕЛО: старая реализация на Firebase Firestore + Cloud Storage. Заменена на
// PostsRestDatasource.kt, который ходит в собственный бэкенд по REST (/posts, /upload).
// Этот файл больше нигде не используется (см. di/PostsDatasourceModule.kt) и его можно
// удалить вручную — сессия, в которой это писалось, не может сама удалять файлы на диске.
