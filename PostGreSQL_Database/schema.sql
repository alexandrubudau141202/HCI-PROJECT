-- =============================================================================
-- PostgreSQL Schema — Anime Project
-- Static data: anime details, characters, people, stats, recommendations
-- =============================================================================

-- -----------------------------------------------------------------------------
-- EXTENSIONS
-- -----------------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS unaccent;


-- =============================================================================
-- ANIME
-- =============================================================================

CREATE TABLE anime (
    mal_id              INTEGER         PRIMARY KEY,
    title               TEXT            NOT NULL,
    title_japanese      TEXT,
    type                VARCHAR(20),                       
    status              VARCHAR(30),                       
    source              VARCHAR(50),                       
    rating              VARCHAR(20),                       
    episodes            INTEGER,
    season              VARCHAR(10),                       
    year                INTEGER,
    start_date          DATE,
    end_date            DATE,
    synopsis            TEXT,
    score               NUMERIC(4, 2),
    scored_by           INTEGER,
    rank                INTEGER,
    popularity          INTEGER,
    members             INTEGER,
    favorites           INTEGER,
    created_at          TIMESTAMP       DEFAULT NOW()
);

CREATE INDEX idx_anime_type       ON anime(type);
CREATE INDEX idx_anime_year       ON anime(year);
CREATE INDEX idx_anime_score      ON anime(score DESC NULLS LAST);
CREATE INDEX idx_anime_popularity ON anime(popularity ASC NULLS LAST);
CREATE INDEX idx_anime_rank       ON anime(rank ASC NULLS LAST);
CREATE INDEX idx_anime_season     ON anime(season, year);


-- =============================================================================
-- GENRES  (normalised — many-to-many with anime)
-- =============================================================================

CREATE TABLE genres (
    genre_id    SERIAL          PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL UNIQUE
);

CREATE TABLE anime_genres (
    mal_id      INTEGER         NOT NULL REFERENCES anime(mal_id)  ON DELETE CASCADE,
    genre_id    INTEGER         NOT NULL REFERENCES genres(genre_id) ON DELETE CASCADE,
    PRIMARY KEY (mal_id, genre_id)
);

CREATE INDEX idx_anime_genres_genre ON anime_genres(genre_id);


-- =============================================================================
-- STUDIOS / PRODUCERS / LICENSORS  (normalised)
-- =============================================================================

CREATE TABLE organisations (
    org_id      SERIAL          PRIMARY KEY,
    name        TEXT            NOT NULL UNIQUE,
    org_type    VARCHAR(20)     NOT NULL    -- studio, producer, licensor
);

CREATE TABLE anime_organisations (
    mal_id      INTEGER         NOT NULL REFERENCES anime(mal_id) ON DELETE CASCADE,
    org_id      INTEGER         NOT NULL REFERENCES organisations(org_id) ON DELETE CASCADE,
    PRIMARY KEY (mal_id, org_id)
);

CREATE INDEX idx_anime_orgs_org ON anime_organisations(org_id);


-- =============================================================================
-- THEMES & DEMOGRAPHICS  (normalised)
-- =============================================================================

CREATE TABLE themes (
    theme_id    SERIAL          PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL UNIQUE
);

CREATE TABLE anime_themes (
    mal_id      INTEGER         NOT NULL REFERENCES anime(mal_id)   ON DELETE CASCADE,
    theme_id    INTEGER         NOT NULL REFERENCES themes(theme_id) ON DELETE CASCADE,
    PRIMARY KEY (mal_id, theme_id)
);

CREATE TABLE demographics (
    demographic_id  SERIAL      PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE anime_demographics (
    mal_id          INTEGER     NOT NULL REFERENCES anime(mal_id)              ON DELETE CASCADE,
    demographic_id  INTEGER     NOT NULL REFERENCES demographics(demographic_id) ON DELETE CASCADE,
    PRIMARY KEY (mal_id, demographic_id)
);


-- =============================================================================
-- ANIME STATS  (one-to-one with anime)
-- =============================================================================

CREATE TABLE anime_stats (
    mal_id              INTEGER     PRIMARY KEY REFERENCES anime(mal_id) ON DELETE CASCADE,
    watching            INTEGER     DEFAULT 0,
    completed           INTEGER     DEFAULT 0,
    on_hold             INTEGER     DEFAULT 0,
    dropped             INTEGER     DEFAULT 0,
    plan_to_watch       INTEGER     DEFAULT 0,
    total               INTEGER     DEFAULT 0,
    -- score vote counts
    score_1_votes       INTEGER     DEFAULT 0,
    score_2_votes       INTEGER     DEFAULT 0,
    score_3_votes       INTEGER     DEFAULT 0,
    score_4_votes       INTEGER     DEFAULT 0,
    score_5_votes       INTEGER     DEFAULT 0,
    score_6_votes       INTEGER     DEFAULT 0,
    score_7_votes       INTEGER     DEFAULT 0,
    score_8_votes       INTEGER     DEFAULT 0,
    score_9_votes       INTEGER     DEFAULT 0,
    score_10_votes      INTEGER     DEFAULT 0
);


-- =============================================================================
-- RECOMMENDATIONS  (anime-to-anime)
-- =============================================================================

CREATE TABLE recommendations (
    mal_id                  INTEGER     NOT NULL REFERENCES anime(mal_id) ON DELETE CASCADE,
    recommendation_mal_id   INTEGER     NOT NULL REFERENCES anime(mal_id) ON DELETE CASCADE,
    PRIMARY KEY (mal_id, recommendation_mal_id)
);

CREATE INDEX idx_recs_target ON recommendations(recommendation_mal_id);


-- =============================================================================
-- CHARACTERS
-- =============================================================================

CREATE TABLE characters (
    character_mal_id    INTEGER     PRIMARY KEY,
    name                TEXT        NOT NULL,
    name_kanji          TEXT,
    about               TEXT,
    favorites           INTEGER     DEFAULT 0
);

CREATE INDEX idx_characters_favorites ON characters(favorites DESC);

CREATE TABLE character_nicknames (
    character_mal_id    INTEGER     NOT NULL REFERENCES characters(character_mal_id) ON DELETE CASCADE,
    nickname            TEXT        NOT NULL,
    PRIMARY KEY (character_mal_id, nickname)
);

-- Character appearances in anime
CREATE TABLE character_anime (
    anime_mal_id        INTEGER     NOT NULL REFERENCES anime(mal_id)             ON DELETE CASCADE,
    character_mal_id    INTEGER     NOT NULL REFERENCES characters(character_mal_id) ON DELETE CASCADE,
    character_name      TEXT,
    role                VARCHAR(20),        -- Main, Supporting
    PRIMARY KEY (anime_mal_id, character_mal_id)
);

CREATE INDEX idx_char_anime_char ON character_anime(character_mal_id);


-- =============================================================================
-- PEOPLE  (voice actors, directors, staff)
-- =============================================================================

CREATE TABLE people (
    person_mal_id       INTEGER     PRIMARY KEY,
    name                TEXT        NOT NULL,
    given_name          TEXT,
    family_name         TEXT,
    birthday            DATE,
    birth_year          INTEGER,
    relevant_location   TEXT,
    favorites           INTEGER     DEFAULT 0
);

CREATE INDEX idx_people_favorites ON people(favorites DESC);

CREATE TABLE person_alternate_names (
    person_mal_id   INTEGER     NOT NULL REFERENCES people(person_mal_id) ON DELETE CASCADE,
    alt_name        TEXT        NOT NULL,
    PRIMARY KEY (person_mal_id, alt_name)
);

-- Person roles in anime (director, music, etc.)
CREATE TABLE person_anime_works (
    person_mal_id   INTEGER     NOT NULL REFERENCES people(person_mal_id) ON DELETE CASCADE,
    anime_mal_id    INTEGER     NOT NULL REFERENCES anime(mal_id)         ON DELETE CASCADE,
    position        TEXT,
    PRIMARY KEY (person_mal_id, anime_mal_id)
);

CREATE INDEX idx_person_anime_anime ON person_anime_works(anime_mal_id);

-- Voice acting roles
CREATE TABLE person_voice_works (
    person_mal_id       INTEGER     NOT NULL REFERENCES people(person_mal_id)        ON DELETE CASCADE,
    anime_mal_id        INTEGER     NOT NULL REFERENCES anime(mal_id)                ON DELETE CASCADE,
    character_mal_id    INTEGER     NOT NULL REFERENCES characters(character_mal_id) ON DELETE CASCADE,
    role                VARCHAR(20),
    language            VARCHAR(30),
    PRIMARY KEY (person_mal_id, anime_mal_id, character_mal_id)
);

CREATE INDEX idx_voice_works_anime ON person_voice_works(anime_mal_id);
CREATE INDEX idx_voice_works_char  ON person_voice_works(character_mal_id);


-- =============================================================================
-- USERS  (base profile — static part only)
-- =============================================================================

CREATE TABLE users (
    username        TEXT            PRIMARY KEY,
    gender          VARCHAR(20),
    birthday        DATE,
    location        TEXT,
    joined          DATE,
    joined_year     INTEGER
);

CREATE INDEX idx_users_location    ON users(location);
CREATE INDEX idx_users_joined_year ON users(joined_year);