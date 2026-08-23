# План: полностью убрать Firebase, регистрация — свой бэкенд + Google

Дополняет `firebase-migration-plan.md`. Меняет Фазу 3 того плана: там Firebase Auth оставался как есть, здесь — заменяется полностью собственным сервисом авторизации на Ktor-бэкенде. Email/password и Google остаются единственными способами входа.

## 1. Бэкенд: своя авторизация

- **Таблица `Users`**: добавить `password_hash` (bcrypt/argon2 — не хранить пароль в открытом виде) и `google_sub` (уникальный id пользователя в Google, nullable — не у всех вход через Google).
- **JWT**: зависимость `ktor-server-auth-jwt` уже подключена в `build.gradle.kts`, но пока не используется (сейчас есть только проверка Firebase-токена). Нужно завести свой issuer/secret (или пару ключей), выдавать access-токен (короткоживущий, 15–60 мин) и refresh-токен (долгоживущий, хранится в БД или как отдельная таблица `refresh_tokens` с возможностью отозвать).
- **Эндпоинты**:
  - `POST /auth/register` — email + пароль, хеширование, создание пользователя.
  - `POST /auth/login` — email + пароль, проверка хеша, выдача access+refresh токенов.
  - `POST /auth/google` — принимает Google ID-токен от клиента; бэкенд проверяет его сам (через `google-api-client`/`google-auth-library-oauth2-http`, `GoogleIdTokenVerifier`), достаёт `sub`/`email`/`name`/`picture`, ищет пользователя по `google_sub` или создаёт нового — дальше выдаёт свои JWT-токены так же, как `/auth/login`.
  - `POST /auth/refresh` — по refresh-токену выдаёт новый access-токен.
  - `POST /auth/logout` — отзывает refresh-токен.
  - `POST /auth/forgot-password` + `POST /auth/reset-password` — генерация одноразового токена сброса, отправка письма.
- **Роут `authenticate("firebase-auth")`** в `Security.kt` заменяется на свой JWT-провайдер (`authenticate("jwt-auth")`), Firebase Admin SDK и `firebase-service-account.json` больше не нужны вообще — убрать зависимость `com.google.firebase:firebase-admin` из `backend/build.gradle.kts`.
- **Письма для сброса пароля** — единственная по-настоящему новая инфраструктура: нужен email-провайдер (SMTP-релей, SendGrid, Yandex Cloud Postbox и т.п.) и небольшой шаблон письма со ссылкой/кодом.

## 2. Клиент: Google без Firebase

Хорошая новость — сам `GoogleSignInClient`/`GoogleSignInOptions` в `AuthRepositoryImpl` и так не завязан на Firebase, это чистый Google Sign-In SDK. Убирается только последний шаг:

- Было: `GoogleAuthProvider.getCredential(idToken) → auth.signInWithCredential(credential)` (обмен на Firebase-сессию).
- Станет: полученный от Google `account.idToken` отправляется напрямую на `POST /auth/google` бэкенда, в ответ приходят свои access/refresh токены.

## 3. Клиент: хранение сессии и токена

Firebase SDK сейчас бесплатно даёт: локальное хранение сессии, автообновление токена, `AuthStateListener`. Всё это нужно сделать самим:

- Хранить access/refresh токены в `EncryptedSharedPreferences` (или DataStore + Android Keystore) — не в обычных `SharedPreferences`, которые сейчас в `core:prefs`.
- `AuthRepositoryImpl.getAuthState` — заменить `FirebaseAuth.AuthStateListener` на свой `StateFlow`, который читает наличие валидного токена из хранилища.
- HTTP-клиент (Ktor Client, уже нужен по основному плану) — добавить плагин, который на 401 пытается обновить токен через `/auth/refresh` и повторяет запрос; если refresh тоже не сработал — разлогинивает.

## 4. Что убирается из проекта

- Зависимости: `firebase-auth`, `firebase-firestore`, `firebase-storage*`, `google-services.json`, плагин `com.google.gms.google-services`.
- На бэкенде: `firebase-admin`, `firebase-service-account.json`, весь код в `Security.kt`, завязанный на Firebase Admin SDK.
- Google Sign-In SDK (`com.google.android.gms.auth.api.signin`) и его зависимость — остаётся, это не Firebase.

## 5. Безопасность — на что обратить внимание отдельно

- Rate limiting на `/auth/login` и `/auth/register` (защита от подбора пароля) — Firebase это делал сам, теперь ответственность на вас.
- Правильный алгоритм хеширования (bcrypt с достаточным cost-фактором, либо argon2id).
- Ротация/отзыв refresh-токенов, короткий TTL access-токена.
- HTTPS обязателен для всех auth-эндпоинтов в проде (сейчас бэкенд крутится локально по http — до продакшена понадобится TLS-терминация, например через reverse proxy).

## Порядок работ

1. Бэкенд: миграция схемы (`password_hash`, `google_sub`), JWT issuer, эндпоинты `/auth/*`.
2. Бэкенд: email-провайдер для сброса пароля.
3. Клиент: заменить обмен Google-токена на вызов `/auth/google`, убрать `signInWithCredential`.
4. Клиент: защищённое хранилище токенов + свой `AuthState` + refresh-интерцептор в HTTP-клиенте.
5. Перенести email/password флоу (`firebaseCreateAccount`/`firebaseSignIn`/`firebaseForgotPassword`) на новые эндпоинты.
6. Прогнать все сценарии (регистрация email, вход email, Google, забыл пароль, разлогин, протухший токен) вручную.
7. Убрать Firebase-зависимости и файлы конфигурации (п. 4 выше) из клиента и бэкенда.
