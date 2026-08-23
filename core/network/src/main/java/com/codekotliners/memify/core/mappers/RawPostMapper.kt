package com.codekotliners.memify.core.mappers

// УСТАРЕЛО: раньше тут парсился Firestore DocumentSnapshot в PostDto. Больше не используется —
// посты теперь приходят с бэка уже готовым JSON (см. PostDto.kt + PostsRestDatasource.kt),
// парсинг делает kotlinx.serialization автоматически. Этот файл можно удалить вручную.
