# УСТАРЕЛО — этот план не применялся

Этот документ был первым черновиком плана переноса с Firebase на свой бэкенд и описывал
**гибридный** вариант: оставить Firebase Auth на клиенте как есть, а бэкенд — только
проверять присланный Firebase ID-токен через Firebase Admin SDK.

**От этого варианта отказались.** Итоговое решение — полностью убрать Firebase и с клиента,
и с бэкенда (email/password + Google через собственную авторизацию на бэке). Актуальный план
см. в `auth-remove-firebase-plan.md` в этой же папке.

Что реально сделано (ветка `feature/backend-migration`):
- Бэкенд (`backend/src/main/kotlin/Security.kt`, `routes/AuthRoutes.kt`): своя JWT-авторизация
  (bcrypt для паролей, `com.auth0:java-jwt`, `GoogleIdTokenVerifier` для проверки Google-токена
  напрямую, без Firebase Admin SDK). Зависимость `com.google.firebase:firebase-admin` убрана.
- Android-клиент (`feature/auth/.../AuthRepositoryImpl.kt` и весь остальной код): `FirebaseAuth`,
  `FirebaseFirestore`, `FirebaseStorage`, Crashlytics — убраны полностью. Все запросы идут через
  REST к `/auth/*`, `/users/*`, `/posts/*`, `/templates/*`, `/upload` на своём бэке.
  Google Sign-In (`play-services-auth`) остался — он и раньше не был частью Firebase.

Этот файл оставлен как есть (просто с пометкой об устаревании), чтобы не пропадала история —
удалить его вручную можно в любой момент, на код это не влияет.
