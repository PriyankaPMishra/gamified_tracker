# Postman Collection — Gamified Tracker

`gamified-tracker.postman_collection.json` is a self-contained Postman collection (v2.1 schema) covering every endpoint documented in [API.md](../API.md): the API Gateway's public surface (port 8080), plus direct-hit folders for Activity Service (8081) and Gamification Service (8082) for internal debugging.

## Import

Postman → **Import** → select `gamified-tracker.postman_collection.json`. No separate environment file needed — all variables (base URLs, tokens, generated test data) live on the collection itself.

## Run

1. Start the stack: `docker-compose up --build` (Gateway, Activity Service, Gamification Service, Eureka, Postgres all need to be up).
2. In Postman, open the collection and use **Run collection** (Collection Runner), keeping the default top-to-bottom folder order:
   `Auth → Activity → Activity Log → Level Tracker → Activity Level Threshold → Security - IDOR Verification → Misc → Activity Service (Internal) → Gamification Service (Internal)`.

   Order matters — later folders read variables (tokens, ids, the created activity name) captured by earlier ones. A collection-level pre-request script generates fresh, unique test emails/activity names/ids the first time any request runs, so re-running the whole collection repeatedly won't collide with previous runs.
3. Individual requests can also be run standalone once the `Auth` folder has populated `userAToken` / `userBToken` / `adminToken` in the collection variables.

## Folders

| Folder | Covers |
|---|---|
| Auth | register (user + admin), login, invalid-password 401 |
| Activity | list/get/create, admin-only 403 enforcement |
| Activity Log | create, get by id, list by user, open-read behavior, 404 |
| Level Tracker | create/update XP, list, get by user/activity, negative-XP 400 |
| Activity Level Threshold | create, list, composite-key lookup, 404 |
| **Security - IDOR Verification** | the userId-from-JWT fix: write-binds-to-caller, forged `userId` header is neutralized, reads stay open by design, unauthenticated requests are rejected |
| Misc | `/api/hello` |
| Activity Service (Internal :8081) | same endpoints hit directly, bypassing the Gateway |
| Gamification Service (Internal :8082) | same, for gamification-service |

## Known caveats baked into the tests

- **`POST /api/activitylog` (Gateway and direct) can 500.** Pre-existing, unrelated bug: `RandomGenerator.getDefault()` in `ActivityLogServiceImpl`'s XP-bonus roll fails to resolve the `L32X64MixRandom` algorithm on some JVM/container images. If it fails, that's this known issue — not an IDOR regression. The core write-binding fix is independently verified in the Security folder via `POST /api/level`, which shares the identical Gateway header-injection mechanism but has no RNG dependency. Downstream requests that need `activityLogId` are guarded to skip rather than crash the run.
- **No-token requests return `403`, not `401`.** Default Spring Security behavior (anonymous auth, no custom `AuthenticationEntryPoint`) — pre-existing, unrelated to the IDOR fix. Tests accept either code.
- **`GET /level/{id}` has no reachable happy path.** `LevelTrackerDto` never exposes its internal numeric id in any response, so only the not-found case (`404`) is scriptable without direct DB access.
- **Reads are intentionally open.** `GET .../{id}` and `GET .../user/{id}` let any authenticated user view any other user's data — a social/leaderboard feature, not an access-control gap. Tests in the Security folder assert `200`, not `403`, when User A reads User B's data.
