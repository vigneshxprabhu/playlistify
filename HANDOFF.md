# Playlistify — Complete Handoff Document

> This document is intended for another AI assistant (or developer) to take over this
> project **without needing any previous conversation history**. It is comprehensive by
> design. Read it fully before touching the codebase.

---

## 1. Project Overview

- **Project name:** Playlistify
- **What the project does:** A Spring Boot REST backend that connects to the Spotify Web
  API, reads a user's Liked Songs, discovers the genres of the artists behind those songs,
  and (in the vision) automatically builds genre-based Spotify playlists (e.g. "your
  favourite rock tracks").
- **Why it exists:** The developer is learning Java, Spring Boot, REST APIs, OAuth, DTO
  mapping, dependency injection, caching, and layered architecture. Building a real app with
  a real external API (Spotify) is the learning vehicle. It is a personal/learning project,
  not a commercial product.
- **Target users:** Currently a single user (the developer, using their own Spotify
  account). The app is single-user by design at this stage (tokens held in memory, no
  database).
- **Overall vision:** Full pipeline: authenticate -> fetch liked songs -> fetch artist genre
  metadata from an external provider -> group songs by genre -> generate & populate Spotify
  playlists per genre. Along the way: robust caching, performance metrics, a configurable
  metadata-provider abstraction, and good REST API design.
- **Current development stage:** Early, learning-stage prototype.
  - Working: OAuth login/callback, user profile, liked-songs fetch (paginated), basic
    playlist creation, artist-genre analysis pipeline with disk cache + performance metrics.
  - In progress / incomplete: `createGenrePlaylist()` fetches artist details but **does not
    yet build the genre map or actually create/populate the playlist**; the
    artist-metadata provider is an **abstraction with a stub implementation** that throws
    `UnsupportedOperationException`.
  - The app compiles and the Spring context loads (`mvnw compile` and `mvnw test` pass).

---

## 2. Tech Stack

- **Java 17** (language / runtime)
- **Spring Boot 3.5.13** (parent POM in `pom.xml`)
  - `spring-boot-starter-web` (REST controllers, embedded Tomcat)
  - `spring-boot-starter-actuator` (health/metrics endpoints; used minimally)
  - `spring-boot-starter-test` (JUnit 5 for the context-load smoke test)
- **Maven** with the Maven Wrapper (`mvnw` / `mvnw.cmd`) — build tool
- **Lombok** (annotations: `@Getter`, `@Setter`, `@Data`, `@NoArgsConstructor`,
  `@AllArgsConstructor`) — reduces boilerplate. Configured as an annotation processor in
  `pom.xml`.
- **Jackson (ObjectMapper)** — JSON serialization/deserialization for DTOs and for the
  artist cache file. (There is also a **Gson** dependency in `pom.xml` but it is unused /
  dead weight.)
- **Java built-in `java.net.http.HttpClient`** — all external HTTP calls (Spotify auth +
  API). No Apache HttpClient, no Retrofit.
- **Spotify Web API** — OAuth 2.0 Authorization Code flow; endpoints for profile, liked
  songs, playlists, tracks.
- **File-based caching** — JSON cache file at `src/main/resources/cache/artist-cache.json`
  (no Redis / no DB yet).
- **No database** — no JPA/Hibernate, no PostgreSQL, no SQL at all.
- **No Docker** — not used yet.
- **No frontend** — pure JSON REST API. Flow is exercised manually: browser (login
  redirect) + curl/Postman for endpoints.
- **IDE/editor config**: `.idea/` (IntelliJ), `.vscode/`, `.gitattributes`, `.gitignore`.

---

## 3. Folder Structure

Root: `C:\Users\vigne\Downloads\playlistify`

```
playlistify/
`-- pom.xml                          # Maven build, deps, Lombok annotation processing
`-- mvnw / mvnw.cmd                  # Maven wrapper scripts
`-- HELP.md                          # Spring Initializr boilerplate
`-- learning.md                      # Developer's personal notes:
                                     #   "DTO, nested DTO, file structure, generic methods"
`-- .gitignore  / .gitattributes
`-- src/
    `-- main/
    |   `-- java/com/example/playlistify/
    |   |   `-- Main/
    |   |   |   `-- PlaylistifyApplication.java   # Spring Boot entry point @SpringBootApplication
    |   |   `-- Util/
    |   |   |   `-- SpotifyHttpUtil.java          # Generic HTTP layer for Spotify API (Bearer + 401 refresh)
    |   |   `-- Metrics/
    |   |   |   `-- PerformanceMetrics.java       # Counters + timing + report printer (@Component)
    |   |   `-- controller/
    |   |   |   `-- AuthController/AuthController.java
    |   |   |   `-- SpotifyController/SpotifyController.java   # EMPTY stub (dead code)
    |   |   |   `-- playlistcontroller/PlaylistController.java
    |   |   |   `-- UserController/UserController.java
    |   |   `-- service/
    |   |   |   `-- ArtistMetadataProvider.java       # NEW abstraction interface
    |   |   |   `-- NoOpArtistMetadataProvider.java   # NEW stub impl (throws UnsupportedOperationException)
    |   |   |   `-- ArtistCacheService.java           # disk cache (unchanged by the refactor)
    |   |   |   `-- AuthService.java                  # OAuth token exchange/refresh
    |   |   |   `-- MusicAnalysisService.java         # analysis + provider abstraction
    |   |   |   `-- PlaylistService.java              # Spotify playlist ops (createGenrePlaylist incomplete)
    |   |   |   `-- SpotifyService.java               # dead stub
    |   |   |   `-- TrackService.java                 # liked-songs fetch (paginated)
    |   |   |   `-- UserService.java                  # user profile fetch
    |   |   |   `-- GetUserService.java               # empty placeholder (dead)
    |   |   `-- model/
    |   |   |   `-- response/
    |   |   |       `-- Track.java                    # legacy/dead plain-Java model (no Lombok)
    |   |   `-- dto/
    |   |       `-- request/
    |   |       |   `-- AddTracksRequest.java         # { uris: [...] }
    |   |       |   `-- CreatePlaylistRequest.java    # name/description/public/collaborative
    |   |       |   `-- GenrePlaylistRequest.java     # genre, playlistName
    |   |       `-- response/
    |   |           `-- analysis/
    |   |           |   `-- ArtistDetails.java        # domain model for artist metadata
    |   |           |   `-- ArtistsResponse.java      # batch wrapper (kept for reference)
    |   |           `-- likedsongs/                   # Spotify liked-songs DTOs
    |   |           |   `-- LikedSongsResponse.java   # pagination wrapper (+ next)
    |   |           |   `-- Item.java                 # { added_at, track }
    |   |           |   `-- Track.java / Artist.java / Album.java / Image.java / ExternalUrls.java
    |   |           `-- playlistresponse/
    |   |           |   `-- PlaylistResponse.java     # id/name/href/uri
    |   |           `-- tokenresponse/
    |   |           |   `-- TokenResponse.java        # access/refresh/token_type/expires_in
    |   |           `-- UserProfileResponse/          # nested DTOs for Spotify /me
    |   |               `-- UserProfileResponse.java + ImageDto, FollowersDto,
    |   |                   ExternalUrlsDto, ExplicitContentDto
    |   `-- resources/
    |       `-- application.properties           # Spotify creds + cache path (GITIGNORED)
    |       `-- application.properties.example  # template with placeholders (committed)
    |       `-- cache/
    |           `-- artist-cache.json            # disk cache (~400+ artist entries)
    `-- test/java/com/example/playlistify/
        `-- PlaylistifyApplicationTests.java    # @SpringBootTest context smoke test
`-- target/                                    # Maven build output (gitignored)
```

### Package responsibility notes

- `Main/` — the Spring Boot entry point. **Note:** it was moved out of the root package
  `com.example.playlistify` into `com.example.playlistify.Main`. This still works because
  `@SpringBootApplication` scans the package subtree `com.example.playlistify`, and `Main`
  is inside it, so all `@Service`/`@RestController`/`@Component` beans are discovered.
- `Util/` — generic reusable HTTP helper (Spotify-specific at the moment).
- `Metrics/` — a `@Component` holding counters (cache hits/misses, apiCalls, artists
  requested) plus a timer and a console report printer.
- `controller/` — thin REST layer delegating to services.
- `service/` — business logic. `ArtistMetadataProvider` (interface) and
  `NoOpArtistMetadataProvider` (stub) live here; `MusicAnalysisService` depends on the
  interface.
- `dto/response/analysis/` — artist metadata DTOs. **Provider-agnostic** now.
- `dto/` — request/response payload mapping; every class has
  `@JsonIgnoreProperties(ignoreUnknown = true)` for resilience.
- `model/` — a legacy plain-Java `Track.java` that is **not used** anywhere. Harmless; could
  be deleted later.

---

## 4. Features

### Completed features

1. **Spotify OAuth (Authorization Code flow)**
   - `GET /playlistify/login` -> builds the Spotify authorize URL and 302-redirects the
     browser to Spotify. Scopes: `user-library-read playlist-modify-private
     playlist-modify-public`.
   - `GET /playlistify/callback?code=...` -> exchanges the code for access + refresh tokens
     via `AuthService.exchangeCodeForToken`. Tokens are printed to console (dev-only).
2. **User Profile** — `GET /playlistify/profile` -> maps Spotify `/v1/me` into
   `UserProfileResponse` (nested DTOs).
3. **Liked Songs** — `GET /playlistify/likedsongs` -> `TrackService.getLikedSongs()` follows
   pagination (`next` URL, `limit=50`) and returns `List<Item>`.
4. **Playlist creation** — `GET /playlistify/playlist/create` — creates a hard-coded "Rock
   Playlist" via Spotify `/v1/me/playlists`. Returns `PlaylistResponse`.
5. **Genre analysis pipeline (core logic in place)**
   - `MusicAnalysisService.getUniqueArtistIds(List<Item>)` — dedupes artist IDs from liked
     songs.
   - `MusicAnalysisService.fetchArtistDetails(...)` — per-artist fetch with cache-first
     policy, performance metrics, cache persistence. Depends on `ArtistMetadataProvider`.
   - `MusicAnalysisService.buildGenreMap(...)` — `Map<String genre, Set<Track>>`.
6. **Artist metadata caching on disk**
   - `ArtistCacheService` loads/saves a JSON map `artistId -> ArtistDetails` at
     `playlistify.cache.path`. Loads at startup (`@PostConstruct`), saves when the fetch loop
     finishes.
7. **Performance metrics** — `PerformanceMetrics` prints a report (artists requested, cache
   hits/misses, API calls, execution time) after each analysis run.
8. **Auto token-refresh on 401** — `SpotifyHttpUtil` retries the request once after calling
   `AuthService.refreshAccessToken()` when it receives HTTP 401.
9. **Provider abstraction (new architecture)** — `ArtistMetadataProvider` interface +
   `NoOpArtistMetadataProvider` stub that throws `UnsupportedOperationException`.

### Features currently being built

1. **MusicBrainz (or other) provider implementation** — `NoOpArtistMetadataProvider` must be
   replaced by a real implementation that queries an external metadata service (MusicBrainz
   is the leading candidate) and returns `ArtistDetails` (name + genres). **Not written.**
2. **Genre playlist generation** — `PlaylistService.createGenrePlaylist(String genre)`
   currently only fetches liked songs + artist details and logs timings. It does **not** call
   `buildGenreMap` or create/populate a Spotify playlist. `POST /playlistify/genre` exists
   and calls it, so only the service side needs finishing.

### Planned features

1. Real `MusicBrainzProvider` implementing `ArtistMetadataProvider` (HTTP client + DTOs +
   JSON mapping + cache integration).
2. Finish genre playlist: pick a genre -> build genre map -> create playlist -> extract URIs
   -> add tracks.
3. Security hardening: stop printing tokens to console; store tokens securely; probably add
   Spring Security and CSRF/session handling.

### Future ideas (not yet scoped)

1. Multi-user support (per-user tokens, possibly a database).
2. Mood/era/tempo-based playlist generation in addition to genre.
3. A small web frontend (HTML/JS or React) instead of raw browser navigation.
4. Redis for in-memory caching.
5. Scheduled/refreshable cache or TTL-based invalidation.
6. AI-generated playlist names/descriptions (an AI API was discussed as a possibility).

---

## 5. Architecture

### Layering overview

```
REST Controllers  ->  Services  ->  External APIs (Spotify, [future: metadata provider])
                              (no repository layer, no database yet)
```

There are **no repositories, no JPA entities and no database** yet. The only persistence is
`ArtistCacheService` writing a JSON file.

### Components

**Controllers (`controller/`)**
- `AuthController` — `GET /playlistify/login` (redirect), `GET /playlistify/callback`
  (token exchange). Hard-codes `clientId`, `redirectUri`, `state` inline (could be cleaned
  up to use properties).
- `PlaylistController` — `GET /playlistify/playlist/create`,
  `POST /playlistify/genre` (body `GenrePlaylistRequest`).
- `UserController` — `GET /playlistify/profile`, `GET /playlistify/likedsongs`.
- `SpotifyController` — empty; injected services unused (dead).

**Services (`service/`)**
- `AuthService` — OAuth. `@Value` injected client-id/client-secret/redirect-uri. Holds the
  current access + refresh tokens as in-memory instance fields (single-user). Methods:
  `exchangeCodeForToken(code)`, `refreshAccessToken()`.
- `SpotifyHttpUtil` — generic `get/post/put/delete(url, body, Class<T>)`; adds
  `Authorization: Bearer <token>`; on 401 -> refresh + retry once; throws `RuntimeException`
  on any HTTP >= 400. Used by TrackService, UserService, PlaylistService.
- `TrackService` — pages through `/v1/me/tracks?limit=50` collecting all liked songs.
- `UserService` — `GET /v1/me` -> `UserProfileResponse`.
- `PlaylistService` — `createPlaylist(name, desc, isPublic)`,
  `addTracksToPlaylist(playlistId, uris)`, `createGenrePlaylist(genre)` (incomplete).
- `MusicAnalysisService` — the analysis engine. Depends on `ArtistMetadataProvider`,
  `ArtistCacheService`, `PerformanceMetrics`. Methods: `getUniqueArtistIds`,
  `fetchArtistDetails(String)`, `fetchArtistDetails(Set<String>)`, `buildGenreMap`. **No
  direct Spotify dependency for artist metadata.**
- `ArtistCacheService` — in-memory `Map<String, ArtistDetails>` backed by a JSON file.
- `ArtistMetadataProvider` (interface) — `ArtistDetails fetchArtistDetails(String artistId)
  throws Exception`.
- `NoOpArtistMetadataProvider` — stub `@Service` implementing the interface; throws
  `UnsupportedOperationException("Artist metadata provider not implemented yet. ...")`.
- `SpotifyService`, `GetUserService` — dead placeholders, unused.

**DTOs (`dto/`)** — Requests (`*Request`) and Responses (`*Response`). Analysis DTOs are
provider-agnostic.

**Utility (`Util/`)** — `SpotifyHttpUtil`.

**Configuration** — No dedicated `@Configuration` class. All config is properties-based via
`@Value`: `spotify.client-id`, `spotify.client-secret`, `spotify.redirect-uri`,
`playlistify.cache.path`.

**Error handling** — Minimal and implicit:
- HTTP layer: 401 -> refresh+retry; >=400 -> `RuntimeException` with status + body.
- Services/controllers mostly `throws Exception`.
- No `@ControllerAdvice` / `@ExceptionHandler` / custom error JSON yet. A provider failure
  currently surfaces as an exception (e.g. `UnsupportedOperationException` from the stub).

**Caching** — see section 9.

**External APIs** — see section 8.

### Authentication flow (data movement)

1. Browser hits `GET /playlistify/login`.
2. `AuthController` returns `302 Location=...accounts.spotify.com/authorize?...`.
3. User approves on Spotify; Spotify redirects to
   `http://127.0.0.1:8080/playlistify/callback?code=...`.
4. `AuthController.callback` -> `AuthService.exchangeCodeForToken(code)`.
5. `AuthService` POSTs `grant_type=authorization_code&code=...&redirect_uri=...` with Basic
   auth (`clientId:clientSecret` Base64) to `https://accounts.spotify.com/api/token`.
6. Token JSON deserialized into `TokenResponse`; `accessToken`/`refreshToken` stored in
   `AuthService` fields.
7. Every subsequent Spotify API call (`SpotifyHttpUtil`) adds
   `Authorization: Bearer <accessToken>`.
8. On 401, `SpotifyHttpUtil` calls `authService.refreshAccessToken()`
   (`grant_type=refresh_token`), updates the stored access token (and refresh token only if a
   new one is returned), rebuilds and re-sends the request.

### Request flow example: liked songs -> genres

1. `GET /playlistify/likedsongs` -> `TrackService` -> `SpotifyHttpUtil` -> Spotify
   `/v1/me/tracks`, paging via `next` -> `List<Item>`.
2. `POST /playlistify/genre` -> `PlaylistService.createGenrePlaylist("rock")`.
3. `TrackService.getLikedSongs()`.
4. `MusicAnalysisService.getUniqueArtistIds(likedSongs)` -> deduped `Set<String>`.
5. `MusicAnalysisService.fetchArtistDetails(Set)` -> per ID: cache lookup; on miss call
   `artistMetadataProvider.fetchArtistDetails(artistId)`; store in cache; count metrics;
   persist cache; print report -> `Map<String artistId, ArtistDetails>`.
6. *(Future)* `buildGenreMap(...)` -> `Map<String genre, Set<Track>>` -> filter for the
   requested genre -> `createPlaylist` + `addTracksToPlaylist`.

---

## 6. Database

**There is no database.** No SQL, no JPA entities, no schema.

- The only "persistence" is the artist cache JSON file — a **performance cache** of
  `artistId -> ArtistDetails` keyed by provider ID, **not** a real data source.
- Planned/considered schema (when a real DB arrives for multi-user support):

```
users           (id, spotify_username, display_name, email, country, product,
                 spotify_access_token, spotify_refresh_token, created_at)
sessions        handled via Spring Security / JWT when introduced
playlists       (id, user_id -> users, name, description, is_public,
                 spotify_playlist_id, genre, created_at)
tracks          (id, spotify_track_id, name, uri, artist_names, genre_tags, added_at)
playlist_tracks (playlist_id -> playlists, track_id -> tracks, position)
```

Audience is currently one user; treat a DB as forward-planning only. Do not implement a DB
unless the user asks — the current design is intentionally DB-free for learning.

---

## 7. Authentication

- **Protocol:** Spotify OAuth 2.0 **Authorization Code** flow.
  - `GET /playlistify/login` issues `302` to
    `https://accounts.spotify.com/authorize?client_id=...&response_type=code&redirect_uri=...&scope=...&state=...`.
  - Scopes requested: `user-library-read`, `playlist-modify-private`,
    `playlist-modify-public`.
  - `state` is hard-coded to `"12345"` (temporary; no CSRF validation yet).
- **Token storage:** in-memory fields on the `AuthService` Spring singleton
  (`@Getter private String accessToken/refreshToken`). Single-user assumption.
- **Refresh logic:** `refreshAccessToken()` uses `grant_type=refresh_token` with the stored
  refresh token + Basic auth; stores the new access token; **only** replaces the refresh
  token if Spotify returns a new one (Spotify may not return one on refresh).
- **401 handling:** centralized in `SpotifyHttpUtil` — refresh once, retry once.
- **Security decisions & rationale:**
  1. *Memory-only tokens* — simplest design; fine for a personal learning app; every restart
     requires a fresh login. Not for production.
  2. *No Spring Security yet* — learning app; the only "security" is Spotify's OAuth for the
     Spotify API. Your own endpoints are unauthenticated.
  3. *Client secret in `application.properties`* — the file is **gitignored**; the example
     file has placeholders. Real credentials were present in the working file (client id
     `77cbc600bcff4fc7a57269fb6912b909`, secret `94451c03ffd349e7ae33bbe84d6cbfd3`), and the
     client id is also hard-coded in `AuthController` — treat as secrets; consider moving to
     environment variables.
  4. *Tokens printed to console in `callback`* — dev convenience; should be removed before
     anything resembling production.

---

## 8. External APIs

### Spotify Web API

- **Why:** data source for authentication, user profile, liked songs, playlist read/write.
- **Current implementation status:** fully implemented for the parts used. `AuthService`,
  `SpotifyHttpUtil`, `TrackService`, `UserService`, `PlaylistService` talk to Spotify.
- **Endpoints used:**
  - `POST https://accounts.spotify.com/api/token` (code exchange + refresh)
  - `GET https://api.spotify.com/v1/me` (profile)
  - `GET https://api.spotify.com/v1/me/tracks?limit=50&...` (liked songs, paginated)
  - `POST https://api.spotify.com/v1/me/playlists` (create)
  - `POST https://api.spotify.com/v1/playlists/{id}/tracks` (add tracks)
- **IMPORTANT CHANGE:** Spotify **no longer returns artist `genres`/`popularity`/`followers`
  for Development Mode applications.** This is the entire reason artist-metadata fetching was
  extracted from Spotify into the `ArtistMetadataProvider` abstraction. Spotify is now used
  **only for** OAuth, profile, liked songs, playlist creation/modification and track
  management — **not** for artist metadata.
- **Limitations:** per-IP and per-app rate limits (429 on bursts; see Spotify docs); artist
  genre fields gated for dev apps; single in-memory token => single-user.
- **Alternatives for metadata specifically:** MusicBrainz (favored), Deezer, ListenBrainz,
  Last.fm, Discogs — details below.

### MusicBrainz (leading candidate for the new provider)

- **Why:** open, **free, requires NO API key**, actively maintained by the MetaBrainz
  Foundation, and has a curated official genre list.
- **Current implementation status:** **not implemented.** Only the abstraction exists.
- **Planned implementation (by developer / next AI):**
  - `MusicBrainzProvider implements ArtistMetadataProvider`.
  - Resolve MBID by search: `GET https://musicbrainz.org/ws/2/artist?query=artist:"NAME"&fmt=json`
    (only search works without an MBID).
  - Fetch genres: `GET https://musicbrainz.org/ws/2/artist/{mbid}?inc=genres+tags&fmt=json`
    -> map `genres[]` (official genre list) into `ArtistDetails.genres`.
  - `@JsonIgnoreProperties(ignoreUnknown = true)` on DTOs already tolerates the rich
    MusicBrainz response.
- **Limitations / rate limits (important):**
  - **Strictly 1 request/second per IP.** Exceeding -> `503 Service Unavailable` and your IP
    may be blocked. A `MusicBrainzProvider` MUST throttle (~1.1s between requests). For
    ~500-2000 artists that is roughly **10-35 minutes serial**.
  - **No API key**, but you MUST send a **meaningful `User-Agent`** header
    (e.g. `Playlistifier/1.0 (mailto:you@example.com)`); a generic `Java/` UA is treated as
    "anonymous" and subject to throttling.
  - No batching for artist detail; `inc=genres+tags` keeps it to one request per artist on
    lookup. Search + lookup = up to 2 requests per artist on first fetch.
  - Docs: https://musicbrainz.org/doc/MusicBrainz_API and .../MusicBrainz_API/Rate_Limiting
- **Live evidence from this project's earlier investigation (155-artist sample across pop,
  rap, rock, jazz, metal, indie, EDM, classical, K-pop, Latin, Afrobeats, regional):**
  - Found: 136/155 (~88%); of those found, the top search hit matched exactly 100%.
  - With genres: 112/155 (~72% of total; ~82% of found).
  - Average tags per artist: ~11.2. Avg search latency: ~700 ms.
  - Notable misses: Harry Styles, Lady Gaga, Kanye West, Future, Arctic Monkeys, Deftones,
    Bjork, Pyotr Ilyich Tchaikovsky, Igor Stravinsky — mostly search-query or accent/alias
    cases (Lucene escaping matters; e.g. querying `Future` returns the wrong artist first).

### Last.fm

- **Why considered:** rich community tag data including genres.
- **Current implementation status:** not implemented, not currently planned.
- **Planned (if chosen):** `LastFmArtistMetadataProvider` calling
  `http://ws.audioscrobbler.com/2.0/?method=artist.getInfo&artist=...&autocorrect=1&format=json`
  and/or `artist.getTopTags`.
- **Limitations / rate limits:**
  - **Requires a free API key** (no key -> `error 6 "Invalid parameters"`; confirmed live).
  - No documented numeric rate limit; docs say "be reasonable", error `29` = rate limit
    exceeded. Community guideline ~5 req/sec per key.
  - Tags are **community/user-generated**: noisy (moods, "favourites", "seen live", country,
    years) -> requires filtering to isolate real genres.
  - API largely in maintenance mode for years, though the endpoint still works.
- **Conclusion:** MusicBrainz is preferred over Last.fm for genres (no key, actively
  maintained, curated genre list vs raw community tags).

### Deezer API

- **Why considered:** free, key-less artist/album lookups with some genre data.
- **Current implementation status:** not implemented.
- **Planned (if chosen):** `https://api.deezer.com/search/artist?q=...` ->
  `https://api.deezer.com/artist/{id}`.
- **Limitations / rate limits:** community-documented ~50 requests per 5 seconds per IP (not
  in official docs). Live test on 155 artists: 155/155 found, 147 exact match (95%), avg
  latency ~344 ms. Genre quality weaker than MusicBrainz for niche/classical/K-pop.
- **Conclusion:** decent fallback, not preferred.

### ListenBrainz

- **Why considered:** open metadata project built on MusicBrainz IDs; has **batch** metadata
  endpoints.
- **Current implementation status:** not implemented.
- **Planned (if chosen):**
  `GET https://api.listenbrainz.org/1/metadata/artist?artist_mbids=...&inc=tag` (key-less,
  **accepts multiple comma-separated MBIDs per request — the only surveyed API with true
  batching**), falling back to MusicBrainz search to resolve MBIDs.
- **Limitations / rate limits:** rate-limited via `X-RateLimit-*` headers; `429` on exceed;
  higher limits with an auth token (requires account).
- **Conclusion:** good complement to MusicBrainz (same MBID ecosystem); data derived from
  MusicBrainz tags/relations.

### Discogs API

- **Why considered:** large community database with genres/styles.
- **Current implementation status:** not implemented.
- **Planned (if chosen):** `GET https://api.discogs.com/database/search?q=...` +
  `GET /releases/{id}` / `/masters/{id}` to read `genres`/`styles`.
- **Limitations / rate limits:** unauthenticated **25 requests/min**, authenticated **60/min**;
  requires a User-Agent; images require a token; **artist entities do not carry genres
  directly** — genres live on releases/masters, meaning ~2 calls per artist plus aggregation.
  Heavier and slower than MusicBrainz for this use case.
- **Conclusion:** lowest fit for lightweight artist-genre enrichment.

### AI APIs (mentioned in future ideas)

- **Why:** auto-generate playlist names/descriptions or infer genres for artists with no
  genre data.
- **Current implementation status:** not implemented, only discussed.
- **Limitations:** cost, latency, key management; overkill for v1.

---

## 9. Caching Strategy

- **What is cached:** `ArtistDetails` objects (id, name, genres, + optional popularity /
  followers) keyed by artist ID.
- **Why:** artist metadata is expensive (HTTP round-trips + strict provider rate limits,
  e.g. MusicBrainz 1 req/sec). Caching makes repeat runs nearly free and avoids hammering the
  provider.
- **Cache keys:** the artist ID string as passed through the pipeline. For Spotify it was the
  Spotify artist ID; for MusicBrainz it will be the MBID. **Caveat:** the existing cache file
  was populated with Spotify IDs; switching providers means the cache is stale-keyed data —
  expect to clear/re-key `artist-cache.json`.
- **Cache storage:** two layers — in-memory `HashMap<String, ArtistDetails>` (fast) persisted
  to the JSON file `src/main/resources/cache/artist-cache.json`
  (`playlistify.cache.path`).
- **Lifecycle:**
  - `@PostConstruct initialize()` -> `load()` reads the file (or starts empty).
  - `get(artistId)` / `put(artistId, artist)` mutate the map.
  - `save()` writes the whole map pretty-printed at the end of a fetch loop.
- **Cache invalidation:** **none implemented** — entries are permanent until the file is
  manually deleted/edited. This is acceptable for static artist metadata but will matter as
  the cache grows. Consider a TTL or a "last updated" timestamp later.
- **Planned Redis usage:** none implemented. Discussed as a future idea — Redis would allow
  shared, fast in-memory caching across restarts/servers and TTL-based eviction, replacing
  the hand-rolled JSON-file cache.

---

## 10. Current TODO

Work that still needs to be built, roughly in order:

1. **Implement `MusicBrainzProvider implements ArtistMetadataProvider`**
   - HTTP client (Java `HttpClient`) with `User-Agent` header and 1.1s throttle.
   - MusicBrainz artist search (resolve MBID) + lookup with `inc=genres+tags`.
   - DTOs for the MusicBrainz JSON response (search + lookup shapes).
   - JSON mapping into `ArtistDetails` (`name`, `genres[]`).
   - Decide how caching integrates (call `ArtistCacheService` or keep the cache layer in
     `MusicAnalysisService` as it is now).
   - Replace/retire `NoOpArtistMetadataProvider` (e.g. delete it or annotate your provider
     `@Primary`, depending on which bean should win).
2. **Finish `PlaylistService.createGenrePlaylist(String genre)`**
   - After fetching `Map<String, ArtistDetails>`: call
     `musicAnalysisService.buildGenreMap(likedSongs, artistDetails)`.
   - Filter the map for the requested genre.
   - `createPlaylist(playlistName, description, isPublic)` using `GenrePlaylistRequest.genre`
     and `playlistName`.
   - `extractTrackUris(...)` + `addTracksToPlaylist(playlistId, uris)`.
3. **Wire `GenrePlaylistRequest.playlistName` through** — the field exists but is not passed
   to `createPlaylist`.
4. **Security cleanup** — stop printing tokens; externalize the hard-coded client id in
   `AuthController`; consider env-var config for the Spotify secret.
5. **Tests** — currently only the `@SpringBootTest` context smoke test exists. Add unit tests
   for `MusicAnalysisService` (cache-hit/miss paths) and `PlaylistService` once the flow is
   complete.

### Unfinished classes / endpoints

- `NoOpArtistMetadataProvider` — to be replaced by a real implementation.
- `PlaylistService.createGenrePlaylist` — partial (fetch + timing only).
- `SpotifyController`, `SpotifyService`, `GetUserService`, `model/response/Track.java` — dead
  code stubs; decide whether to delete or repurpose.

---

## 11. Important Design Decisions

1. **Spotify no longer the source of artist genres.** Because Spotify deprives Development
   Mode apps of artist `genres`/`popularity`/`followers`, all artist-metadata fetching was
   pulled out of Spotify. Trade-off: Spotify data is simple (artist ID available directly on
   each liked song) but no longer provides genres, so a provider abstraction was introduced.
2. **`ArtistMetadataProvider` abstraction.** `MusicAnalysisService` now depends on an
   interface instead of `SpotifyHttpUtil` for artist data. Trade-off: an extra seam/indirection
   in exchange for future provider swapability (MusicBrainz, Deezer, Last.fm, etc.) and
   testability (mock provider).
3. **Interface method takes an artist ID string.** `fetchArtistDetails(String artistId)`.
   Rationale: the pipeline already works in artist-ID space (deduped from liked songs); the
   provider is responsible for resolving that ID within its own system. Trade-off: the ID
   semantics are provider-dependent (Spotify ID vs MBID), which is why the disk cache should
   be re-keyed when switching providers.
4. **`NoOpArtistMetadataProvider` stub with `UnsupportedOperationException`.** Keeps the
   project compiling and the dependency graph intact while the real provider is a learning
   exercise for the developer. Trade-off: the genre flow cannot execute until the stub is
   replaced.
5. **`ArtistDetails` kept as a provider-agnostic domain model.** Its `id`, `name`, `genres`
   come from whichever provider fills them; optional `popularity`/`followers` fields were
   restored because future providers may populate them. Trade-off: a slightly "loose" model
   vs. a strict per-provider DTO; chosen for simplicity.
6. **`ArtistCacheService` left completely unchanged.** The cache reads/writes
   `Map<String, ArtistDetails>`, which is already provider-agnostic. Trade-off: cache keys are
   provider-specific (see #3) but the structure needs no change.
7. **`PerformanceMetrics.spotifyApiCalls` renamed to `apiCalls`.** Now that the artist data
   path no longer calls Spotify, the metric name should be provider-agnostic. Trade-off:
   cosmetic rename; no behaviour change.
8. **`artists` batch DTO (`ArtistsResponse`) removed then restored.** Originally deleted as
   dead code tied to the Spotify batch endpoint; restored because the developer wants it kept
   for future compatibility/reference even though it is unused. Trade-off: a little dead code
   in exchange for future convenience.
9. **File-based JSON caching instead of Redis.** Simplest possible cache for a learning app;
   survives restarts but rewrites the whole file each save. Trade-off: slow for very large
   caches; Redis is the planned future upgrade.
10. **In-memory single-user tokens.** Acceptable for local personal use; explicitly rejected
    for production. Trade-off: simplicity vs. scalability/security.
11. **Provider recommendation: MusicBrainz over Last.fm/Deezer/Discogs.** Evidence-driven
    (see section 8): MusicBrainz = free/no key, actively maintained, curated genres, but
    strict 1 req/sec. Choose based on the "how long can enrichment take" trade-off; the
    abstraction keeps the door open to swap later.
12. **No database yet.** DB was deferred to keep the learning scope tight. Trade-off: no user
    persistence; a DB becomes necessary for multi-user support.

---

## 12. Resume Impact

Resume-worthy features and transferable skills demonstrable by this project:

- **Spotify OAuth 2.0 integration** — Authorization Code flow, token exchange, refresh
  rotation, 401 auto-retry.
- **REST API design** — controllers/services/DTO layering, `@RequestMapping("/playlistify")`,
  JSON (de)serialization with Jackson, `@JsonIgnoreProperties`.
- **Paginated external API consumption** — liked-songs paging via `next` URLs.
- **Provider abstraction / dependency inversion** — `ArtistMetadataProvider` interface,
  making a service testable and provider-swappable.
- **Caching design** — cache-first reads, disk persistence, cache-hit/miss metrics.
- **Performance instrumentation** — timed reports of cache hits/misses/API calls.
- **Java 17 + Spring Boot 3** — modern stack: `java.net.http.HttpClient`, Lombok, Maven
  wrapper, bean wiring.
- **Measurable metrics from live testing:**
  - Enrichment performance tracked via `PerformanceMetrics` (artists requested, cache
    hits/misses, API calls, execution time).
  - Provider evaluation research: 155-artist benchmark — MusicBrainz ~88% found / ~72% with
    genres / ~11.2 tags per artist (~700 ms latency); Deezer 100% found / 95% exact match
    (~344 ms latency).
- **Research & comparison evaluation** — systematically compared 5 music-metadata APIs on
  coverage, quality, rate limits, reliability (a strong "systems thinking" talking point).

---

## 13. Lessons Learned

Important concepts exercised in this project:

- **OAuth 2.0 Authorization Code flow** — redirect, code exchange, scopes, refresh tokens,
  Basic auth header encoding (Base64).
- **DTO mapping / nested DTOs** — Spotify object shapes (Item/Track/Artist/Album/Image)
  mapped to Java classes; `@JsonProperty` for snake_case.
- **Dependency Injection** — constructor injection, `@Service`, Spring bean wiring; why
  depending on an interface beats depending on a concrete class.
- **Pagination** — following `next` URLs in a loop; cursor-style traversal.
- **REST APIs / API design** — path conventions, `@RestController`, `@RequestBody`,
  HTTP status codes (302 redirect, 400/401/4xx errors).
- **Layered architecture** — controller -> service -> util; separation of concerns.
- **Caching** — cache-first pattern, persistence to disk, cache keys, hit-vs-miss metrics.
- **Error handling** — auto-refresh on 401, throwing vs. returning errors, resilience in the
  presence of external API failures.
- **External API rate limiting** — first-hand experience (MusicBrainz 1 req/sec, 503s;
  Deezer ~50/5s; Last.fm key requirement; ListenBrainz header-based limits; Discogs
  25-60/min).
- **Generics in Java** — `SpotifyHttpUtil.<T> get(...)` uses a generic method returning a
  typed response.
- **Lombok** — annotations reducing boilerplate; how it integrates as an annotation
  processor.
- **Maven** — wrapper (`./mvnw`), dependency management, Spring Boot parent POM.
- **Interfaces & polymorphism** — the provider abstraction pattern.
- Learning goals the developer explicitly noted (`learning.md`): DTOs, nested DTOs, file
  structure, generic methods.

---

## 14. Coding Style

- **Language/format:** Java 17, default Spring Boot formatting (4-space indent, braces on new
  lines as generated by the IDE/templates). Keep import ordering clean.
- **Naming conventions:**
  - Classes: UpperCamelCase. Packages: all lower-case, sub-package per concern
    (`controller.playlistcontroller`, `dto.response.likedsongs`, `Util`, `Metrics`).
    **Note:** package names here are inconsistent (mixed single/multi-word,
    e.g. `service.playlistcontroller` path `controller/playlistcontroller`). Preserve
    existing structure; don't do a sweeping rename without asking.
  - Methods/fields: camelCase. DTO fields mirror JSON keys (e.g. `added_at`, `duration_ms`).
  - Lombok annotations used liberally (`@Getter/@Setter/@Data`, all/required-args
    constructors).
- **Project conventions observed:**
  - Every response DTO carries `@JsonIgnoreProperties(ignoreUnknown = true)` so future JSON
    fields don't break parsing.
  - `@RestController` + `@RequestMapping("/playlistify")` at class level; HTTP verbs via
    `@GetMapping`/`@PostMapping`.
  - Services are `@Service`; the metrics holder is `@Component`; configuration reads via
    `@Value`.
  - External calls use Java's `HttpClient` with typed `Class<T>` responses.
  - Business logic lives in `service/`; controllers stay thin; HTTP details live in
    `Util/SpotifyHttpUtil`.
- **Conventions to follow going forward:** keep the layering strict (controller -> service ->
  provider), put new provider code under `service/`, keep `ArtistDetails` provider-agnostic,
  and don't delete files the developer explicitly wants to keep (e.g. `ArtistsResponse`,
  dead stubs) without asking. Add TODO comments at integration points the developer will
  implement later (see `NoOpArtistMetadataProvider`).

---

## 15. Future Roadmap

### Version 1 (finish the current pipeline)
1. Implement `MusicBrainzProvider` (search + lookup, throttle, DTOs, mapping).
2. Retire `NoOpArtistMetadataProvider` so the real provider is active.
3. Complete `createGenrePlaylist`: build genre map -> create playlist -> add tracks.
4. Wire `GenrePlaylistRequest.playlistName` into playlist creation.
5. Minimal error handling (`@ControllerAdvice`) and security cleanup (stop printing tokens).

### Version 2 (robustness & breadth)
1. Add `spring-boot-starter-security` or at least a simple session/token guard; env-var based
   secrets.
2. Real persistence (PostgreSQL + JPA/Hibernate) for users, playlists, tracks.
3. Redis cache with TTL for artist metadata; automatic cache invalidation/re-keying.
4. Multi-user support (per-user OAuth tokens, per-user playlists).
5. More playlist modes: mood, era, tempo, "discover similar".
6. Scheduled/offline enrichment so first-run latency is hidden from users.

### Version 3 (polish & productization)
1. Web frontend (React/Next or server-rendered) for a friendly UX.
2. Generative AI for playlist names/descriptions and genre inference for missing data.
3. Background job queue (e.g. for enrichment batches) and proper observability
   (actuator metrics, structured logging).
4. Containerization (Docker) and deployment (cloud), CI/CD.

---

## 16. Current Status

**Where exactly we stopped:**

- The refactor is **complete and the build is green**: `./mvnw compile` -> BUILD SUCCESS
  (37 source files), `./mvnw test` -> BUILD SUCCESS (1/1 tests pass).
- **Finished:**
  - `ArtistMetadataProvider` interface created.
  - `NoOpArtistMetadataProvider` stub created (throws `UnsupportedOperationException`).
  - `MusicAnalysisService` now depends on `ArtistMetadataProvider` (no Spotify artist calls;
    cache + metrics + genre map preserved).
  - `PerformanceMetrics.spotifyApiCalls` renamed to `apiCalls`.
  - `ArtistDetails` restored to include `popularity` and `followers` (domain model).
  - `ArtistsResponse` restored (kept for reference).
  - `ArtistCacheService` untouched; `PlaylistService` untouched by the refactor.
  - The user (outside this session) also moved `PlaylistifyApplication.java` into a new
    `Main/` package and relocated `GetUserService` into `service/`; the build still passes.
- **The last file worked on:** `dto/response/analysis/ArtistsResponse.java` and
  `dto/response/analysis/ArtistDetails.java` (restoration step), immediately followed by a
  successful `mvnw compile`.
- **What should be done next (in order):**
  1. Implement `MusicBrainzProvider` (the developer wants to attempt this themselves as a
     learning exercise — provide guidance, not the full implementation).
  2. Finish `PlaylistService.createGenrePlaylist` end-to-end.

---

## 17. Context About Me

- **Experience level:** beginner-to-intermediate Java developer actively learning Spring Boot.
  Comfortable with the basics of Java, Maven, and reading existing code, but still building
  muscle memory for Spring concepts (beans, DI, binding), REST conventions, and OAuth.
- **How I prefer to learn:** hands-on. I learn best by making small changes and seeing them
  compile/run, and by having each change explained before it is made. I like incremental
  steps and clear reasoning rather than large unexplained refactors.
- **How I like code explained:** plainly. Show what a piece of code does, *why* it exists, and
  how it connects to the rest of the app. Prefer concrete endpoints/classes over abstract
  theory. Use real examples from this codebase.
- **Things to avoid:**
  - Implementing the MusicBrainz integration (or the new provider) for me — it is my
    learning exercise.
  - Making sweeping refactors, deleting files I want to keep, or rewriting classes wholesale
    without explicit approval.
  - Writing lots of code without first explaining the plan.
  - Adding libraries/dependencies unless clearly justified.
- **What kind of guidance I prefer:** pointers, sketches and TODO scaffolding that I can
  complete myself; explanations of trade-offs; and honest assessments of when something is
  a bad idea (for example, Spotify no longer returning genres, committing secrets, printing
  tokens).

---

## 18. Important Conversation Context

- This project was built as a **personal learning project**. Correct, well-explained code
  matters more than speed or production-readiness.
- **Origin of the current architecture:** Spotify removed artist `genres`/`popularity`/
  `followers` for Development Mode apps. The developer explicitly decided to keep Spotify for
  OAuth/profile/liked-songs/playlists but stop using it for artist metadata, and to add an
  `ArtistMetadataProvider` abstraction. The developer asked that this refactor **not** include
  implementing the provider (that's theirs).
- **The provider choice was discussed at length.** A live, evidence-based evaluation of
  Last.fm vs MusicBrainz vs Deezer vs ListenBrainz vs Discogs was performed (results in
  section 8). Last.fm needs an API key (missing key causes `error 6`), MusicBrainz is free +
  no key + curated genres but strictly 1 req/sec, Deezer is fast but weaker on niche genres,
  ListenBrainz has the only true batching (by MBID), and Discogs doesn't expose genres on the
  artist entity. **MusicBrainz is the preferred pick.**
- **Cache caveat:** `src/main/resources/cache/artist-cache.json` is keyed by **Spotify artist
  IDs**. Switching to MusicBrainz (MBIDs) means the existing cache entries will not match —
  plan to clear/re-key the cache during the migration.
- **The developer'd own file reorganizations** (moved `PlaylistifyApplication` into `Main/`,
  moved `GetUserService` to `service/`) are intentional; preserve them.
- **Files the developer explicitly wants kept even when unused:** `ArtistsResponse.java` and,
  by extension, the dead-placeholder service classes (`SpotifyService`, `GetUserService`,
  `model/response/Track.java`). Ask before deleting.
- **Removed/restored during refactors:** `ArtistDetails.popularity`/`followers` were removed
  once and then restored. The developer now treats `ArtistDetails` as an internal domain
  model — future providers may populate those fields. Do not remove them again.
- **Known rough edges the developer is aware of (do not silently "fix"):** hard-coded client
  id in `AuthController`, real credentials in (gitignored) `application.properties`, console
  token printing, `CreatePlaylistRequest`/`GenrePlaylistRequest` unused fields
  (`playlistName`), `createGenrePlaylist` not yet building/populating the playlist,
  commented-out batch fetch code removed by the refactor.
- **Build/verify commands:** `./mvnw compile` and `./mvnw test` on this machine
  (`C:\Users\vigne\Downloads\playlistify`). The only output noise is a Lombok/JDK
  `sun.misc.Unsafe` warning — harmless.
- **Helpful doc files:** `HELP.md` (Spring Initializr boilerplate), `learning.md` (the
  developer's notes: DTOs, nested DTOs, file structure, generic methods).

---

*End of handoff document. Write with the assumption the reader knows nothing from previous
conversations.*