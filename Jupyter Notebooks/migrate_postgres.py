"""
PostgreSQL Migration Script
Loads all cleaned CSVs into the HCI_Project database
Run: python migrate_postgres.py
"""

import pandas as pd
import psycopg2
from psycopg2.extras import execute_values
import ast
import os

# =============================================================================
# CONNECTION
# =============================================================================

CLEAN_PATH = '/Users/alexandrubudau/Desktop/HCI-PROJECT/cleaned datasets/'

conn = psycopg2.connect(
    dbname   = 'HCI_Project',
    user     = 'alexandrubudau',
    host     = 'localhost'
)
conn.autocommit = False
cur = conn.cursor()

print("Connected to PostgreSQL ✓\n")


# =============================================================================
# HELPERS
# =============================================================================

def parse_list(val):
    """Parse stringified list column safely."""
    try:
        parsed = ast.literal_eval(val)
        if isinstance(parsed, list):
            return parsed
    except:
        pass
    return []

def none_if_nan(val):
    """Convert NaN / empty string to None for SQL NULL."""
    if val is None:
        return None
    try:
        if pd.isna(val):
            return None
    except:
        pass
    return val if val != '' else None

def batch_insert(query, rows, batch_size=1000):
    """Insert rows in batches using execute_values."""
    for i in range(0, len(rows), batch_size):
        execute_values(cur, query, rows[i:i + batch_size])
    conn.commit()


# =============================================================================
# 1. ANIME  (details_clean.csv)
# =============================================================================

print("Migrating: anime...")

df = pd.read_csv(CLEAN_PATH + 'details_clean.csv')

rows = []
for _, r in df.iterrows():
    rows.append((
        int(r['mal_id']),
        none_if_nan(r.get('title')),
        none_if_nan(r.get('title_japanese')),
        none_if_nan(r.get('type')),
        none_if_nan(r.get('status')),
        none_if_nan(r.get('source')),
        none_if_nan(r.get('rating')),
        none_if_nan(r.get('episodes')),
        none_if_nan(r.get('season')),
        none_if_nan(r.get('year')),
        none_if_nan(r.get('start_date')),
        none_if_nan(r.get('end_date')),
        none_if_nan(r.get('synopsis')),
        none_if_nan(r.get('score')),
        none_if_nan(r.get('scored_by')),
        none_if_nan(r.get('rank')),
        none_if_nan(r.get('popularity')),
        none_if_nan(r.get('members')),
        none_if_nan(r.get('favorites')),
    ))

batch_insert("""
    INSERT INTO anime (
        mal_id, title, title_japanese, type, status, source, rating,
        episodes, season, year, start_date, end_date, synopsis,
        score, scored_by, rank, popularity, members, favorites
    ) VALUES %s
    ON CONFLICT (mal_id) DO NOTHING
""", rows)

print(f"  Inserted {len(rows):,} anime ✓")


# =============================================================================
# 2. GENRES  (from details_clean.csv genres column)
# =============================================================================

print("Migrating: genres...")

genre_set = set()
for val in df['genres'].dropna():
    for g in parse_list(val):
        if isinstance(g, str) and g.strip():
            genre_set.add(g.strip())

batch_insert("INSERT INTO genres (name) VALUES %s ON CONFLICT (name) DO NOTHING",
             [(g,) for g in genre_set])

# Reload genre name → id mapping
cur.execute("SELECT genre_id, name FROM genres")
genre_map = {name: gid for gid, name in cur.fetchall()}

# anime_genres junction
anime_genre_rows = []
for _, r in df.iterrows():
    for g in parse_list(r.get('genres', '[]')):
        if isinstance(g, str) and g.strip() in genre_map:
            anime_genre_rows.append((int(r['mal_id']), genre_map[g.strip()]))

batch_insert("""
    INSERT INTO anime_genres (mal_id, genre_id) VALUES %s
    ON CONFLICT DO NOTHING
""", anime_genre_rows)

print(f"  Inserted {len(genre_set):,} genres, {len(anime_genre_rows):,} links ✓")


# =============================================================================
# 3. ORGANISATIONS  (studios from details_clean.csv)
# =============================================================================

print("Migrating: organisations...")

org_set = set()
for val in df['studios'].dropna():
    for s in parse_list(val):
        if isinstance(s, str) and s.strip():
            org_set.add((s.strip(), 'studio'))

for val in df['producers'].dropna():
    for s in parse_list(val):
        if isinstance(s, str) and s.strip():
            org_set.add((s.strip(), 'producer'))

for val in df['licensors'].dropna():
    for s in parse_list(val):
        if isinstance(s, str) and s.strip():
            org_set.add((s.strip(), 'licensor'))

batch_insert("""
    INSERT INTO organisations (name, org_type) VALUES %s
    ON CONFLICT (name) DO NOTHING
""", list(org_set))

cur.execute("SELECT org_id, name FROM organisations")
org_map = {name: oid for oid, name in cur.fetchall()}

anime_org_rows = []
for _, r in df.iterrows():
    for col in ['studios', 'producers', 'licensors']:
        for s in parse_list(r.get(col, '[]')):
            if isinstance(s, str) and s.strip() in org_map:
                anime_org_rows.append((int(r['mal_id']), org_map[s.strip()]))

batch_insert("""
    INSERT INTO anime_organisations (mal_id, org_id) VALUES %s
    ON CONFLICT DO NOTHING
""", anime_org_rows)

print(f"  Inserted {len(org_set):,} organisations, {len(anime_org_rows):,} links ✓")


# =============================================================================
# 4. THEMES & DEMOGRAPHICS
# =============================================================================

print("Migrating: themes & demographics...")

theme_set = set()
for val in df['themes'].dropna():
    for t in parse_list(val):
        if isinstance(t, str) and t.strip():
            theme_set.add(t.strip())

batch_insert("INSERT INTO themes (name) VALUES %s ON CONFLICT (name) DO NOTHING",
             [(t,) for t in theme_set])

cur.execute("SELECT theme_id, name FROM themes")
theme_map = {name: tid for tid, name in cur.fetchall()}

anime_theme_rows = []
for _, r in df.iterrows():
    for t in parse_list(r.get('themes', '[]')):
        if isinstance(t, str) and t.strip() in theme_map:
            anime_theme_rows.append((int(r['mal_id']), theme_map[t.strip()]))

batch_insert("INSERT INTO anime_themes (mal_id, theme_id) VALUES %s ON CONFLICT DO NOTHING",
             anime_theme_rows)

demo_set = set()
for val in df['demographics'].dropna():
    for d in parse_list(val):
        if isinstance(d, str) and d.strip():
            demo_set.add(d.strip())

batch_insert("INSERT INTO demographics (name) VALUES %s ON CONFLICT (name) DO NOTHING",
             [(d,) for d in demo_set])

cur.execute("SELECT demographic_id, name FROM demographics")
demo_map = {name: did for did, name in cur.fetchall()}

anime_demo_rows = []
for _, r in df.iterrows():
    for d in parse_list(r.get('demographics', '[]')):
        if isinstance(d, str) and d.strip() in demo_map:
            anime_demo_rows.append((int(r['mal_id']), demo_map[d.strip()]))

batch_insert("INSERT INTO anime_demographics (mal_id, demographic_id) VALUES %s ON CONFLICT DO NOTHING",
             anime_demo_rows)

print(f"  Inserted {len(theme_set):,} themes, {len(demo_set):,} demographics ✓")


# =============================================================================
# 5. ANIME STATS  (stats_clean.csv)
# =============================================================================

print("Migrating: anime_stats...")

df_stats = pd.read_csv(CLEAN_PATH + 'stats_clean.csv')

# Only insert stats for anime that exist in the anime table
cur.execute("SELECT mal_id FROM anime")
valid_ids = {row[0] for row in cur.fetchall()}

stats_rows = []
for _, r in df_stats.iterrows():
    if int(r['mal_id']) not in valid_ids:
        continue
    stats_rows.append((
        int(r['mal_id']),
        int(r.get('watching', 0) or 0),
        int(r.get('completed', 0) or 0),
        int(r.get('on_hold', 0) or 0),
        int(r.get('dropped', 0) or 0),
        int(r.get('plan_to_watch', 0) or 0),
        int(r.get('total', 0) or 0),
        int(r.get('score_1_votes', 0) or 0),
        int(r.get('score_2_votes', 0) or 0),
        int(r.get('score_3_votes', 0) or 0),
        int(r.get('score_4_votes', 0) or 0),
        int(r.get('score_5_votes', 0) or 0),
        int(r.get('score_6_votes', 0) or 0),
        int(r.get('score_7_votes', 0) or 0),
        int(r.get('score_8_votes', 0) or 0),
        int(r.get('score_9_votes', 0) or 0),
        int(r.get('score_10_votes', 0) or 0),
    ))

batch_insert("""
    INSERT INTO anime_stats (
        mal_id, watching, completed, on_hold, dropped, plan_to_watch, total,
        score_1_votes, score_2_votes, score_3_votes, score_4_votes, score_5_votes,
        score_6_votes, score_7_votes, score_8_votes, score_9_votes, score_10_votes
    ) VALUES %s ON CONFLICT (mal_id) DO NOTHING
""", stats_rows)

print(f"  Inserted {len(stats_rows):,} stat rows ✓")


# =============================================================================
# 6. RECOMMENDATIONS
# =============================================================================

print("Migrating: recommendations...")

df_recs = pd.read_csv(CLEAN_PATH + 'recommendations_clean.csv')
df_recs = df_recs[
    df_recs['mal_id'].isin(valid_ids) &
    df_recs['recommendation_mal_id'].isin(valid_ids)
]

rec_rows = [(int(r['mal_id']), int(r['recommendation_mal_id'])) for _, r in df_recs.iterrows()]

batch_insert("""
    INSERT INTO recommendations (mal_id, recommendation_mal_id) VALUES %s
    ON CONFLICT DO NOTHING
""", rec_rows)

print(f"  Inserted {len(rec_rows):,} recommendations ✓")


# =============================================================================
# 7. CHARACTERS
# =============================================================================

print("Migrating: characters...")

df_chars = pd.read_csv(CLEAN_PATH + 'characters_clean.csv')

char_rows = []
for _, r in df_chars.iterrows():
    char_rows.append((
        int(r['character_mal_id']),
        none_if_nan(r.get('name')),
        none_if_nan(r.get('name_kanji')),
        none_if_nan(r.get('about')),
        int(r.get('favorites', 0) or 0),
    ))

batch_insert("""
    INSERT INTO characters (character_mal_id, name, name_kanji, about, favorites)
    VALUES %s ON CONFLICT (character_mal_id) DO NOTHING
""", char_rows)

print(f"  Inserted {len(char_rows):,} characters ✓")


# =============================================================================
# 8. CHARACTER NICKNAMES
# =============================================================================

print("Migrating: character_nicknames...")

df_nicks = pd.read_csv(CLEAN_PATH + 'character_nicknames_clean.csv')

cur.execute("SELECT character_mal_id FROM characters")
valid_char_ids = {row[0] for row in cur.fetchall()}

nick_rows = [
    (int(r['character_mal_id']), str(r['nickname']))
    for _, r in df_nicks.iterrows()
    if int(r['character_mal_id']) in valid_char_ids
]

batch_insert("""
    INSERT INTO character_nicknames (character_mal_id, nickname) VALUES %s
    ON CONFLICT DO NOTHING
""", nick_rows)

print(f"  Inserted {len(nick_rows):,} nicknames ✓")


# =============================================================================
# 9. CHARACTER ANIME WORKS
# =============================================================================

print("Migrating: character_anime...")

df_caw = pd.read_csv(CLEAN_PATH + 'character_anime_works_clean.csv')
df_caw = df_caw[
    df_caw['anime_mal_id'].isin(valid_ids) &
    df_caw['character_mal_id'].isin(valid_char_ids)
]

caw_rows = [
    (int(r['anime_mal_id']), int(r['character_mal_id']),
     none_if_nan(r.get('character_name')), none_if_nan(r.get('role')))
    for _, r in df_caw.iterrows()
]

batch_insert("""
    INSERT INTO character_anime (anime_mal_id, character_mal_id, character_name, role)
    VALUES %s ON CONFLICT DO NOTHING
""", caw_rows)

print(f"  Inserted {len(caw_rows):,} character-anime links ✓")


# =============================================================================
# 10. PEOPLE
# =============================================================================

print("Migrating: people...")

df_people = pd.read_csv(CLEAN_PATH + 'person_details_clean.csv')

people_rows = []
for _, r in df_people.iterrows():
    people_rows.append((
        int(r['person_mal_id']),
        none_if_nan(r.get('name')),
        none_if_nan(r.get('given_name')),
        none_if_nan(r.get('family_name')),
        none_if_nan(r.get('birthday')),
        none_if_nan(r.get('birth_year')),
        none_if_nan(r.get('relevant_location')),
        int(r.get('favorites', 0) or 0),
    ))

batch_insert("""
    INSERT INTO people (person_mal_id, name, given_name, family_name,
                        birthday, birth_year, relevant_location, favorites)
    VALUES %s ON CONFLICT (person_mal_id) DO NOTHING
""", people_rows)

print(f"  Inserted {len(people_rows):,} people ✓")


# =============================================================================
# 11. PERSON ALTERNATE NAMES
# =============================================================================

print("Migrating: person_alternate_names...")

df_altnames = pd.read_csv(CLEAN_PATH + 'person_alternate_names_clean.csv')

cur.execute("SELECT person_mal_id FROM people")
valid_person_ids = {row[0] for row in cur.fetchall()}

altname_rows = [
    (int(r['person_mal_id']), str(r['alt_name']))
    for _, r in df_altnames.iterrows()
    if int(r['person_mal_id']) in valid_person_ids
]

batch_insert("""
    INSERT INTO person_alternate_names (person_mal_id, alt_name) VALUES %s
    ON CONFLICT DO NOTHING
""", altname_rows)

print(f"  Inserted {len(altname_rows):,} alternate names ✓")


# =============================================================================
# 12. PERSON ANIME WORKS
# =============================================================================

print("Migrating: person_anime_works...")

df_paw = pd.read_csv(CLEAN_PATH + 'person_anime_works_clean.csv')
df_paw = df_paw[
    df_paw['person_mal_id'].isin(valid_person_ids) &
    df_paw['anime_mal_id'].isin(valid_ids)
]

paw_rows = [
    (int(r['person_mal_id']), int(r['anime_mal_id']), none_if_nan(r.get('position')))
    for _, r in df_paw.iterrows()
]

batch_insert("""
    INSERT INTO person_anime_works (person_mal_id, anime_mal_id, position)
    VALUES %s ON CONFLICT DO NOTHING
""", paw_rows)

print(f"  Inserted {len(paw_rows):,} person-anime works ✓")


# =============================================================================
# 13. PERSON VOICE WORKS
# =============================================================================

print("Migrating: person_voice_works...")

df_pvw = pd.read_csv(CLEAN_PATH + 'person_voice_works_clean.csv')
df_pvw = df_pvw[
    df_pvw['person_mal_id'].isin(valid_person_ids) &
    df_pvw['anime_mal_id'].isin(valid_ids) &
    df_pvw['character_mal_id'].isin(valid_char_ids)
]

pvw_rows = [
    (int(r['person_mal_id']), int(r['anime_mal_id']), int(r['character_mal_id']),
     none_if_nan(r.get('role')), none_if_nan(r.get('language')))
    for _, r in df_pvw.iterrows()
]

batch_insert("""
    INSERT INTO person_voice_works (person_mal_id, anime_mal_id, character_mal_id, role, language)
    VALUES %s ON CONFLICT DO NOTHING
""", pvw_rows)

print(f"  Inserted {len(pvw_rows):,} voice works ✓")


# =============================================================================
# 14. USERS  (profiles_clean.csv — static fields only)
# =============================================================================

print("Migrating: users...")

df_users = pd.read_csv(CLEAN_PATH + 'profiles_clean.csv')

def safe_date(val):
    """Return None if date is clearly invalid or out of range."""
    if val is None or (isinstance(val, float) and pd.isna(val)):
        return None
    try:
        d = pd.to_datetime(val, errors='coerce')
        if pd.isna(d):
            return None
        # reject dates before 1900 or in the far future
        if d.year < 1900 or d.year > 2025:
            return None
        return d.strftime('%Y-%m-%d')
    except:
        return None

user_rows = []
for _, r in df_users.iterrows():
    user_rows.append((
        str(r['username']),
        none_if_nan(r.get('gender')),
        safe_date(r.get('birthday')),
        none_if_nan(r.get('location')),
        safe_date(r.get('joined')),
        none_if_nan(r.get('joined_year')),
    ))

batch_insert("""
    INSERT INTO users (username, gender, birthday, location, joined, joined_year)
    VALUES %s ON CONFLICT (username) DO NOTHING
""", user_rows)

print(f"  Inserted {len(user_rows):,} users ✓")


# =============================================================================
# DONE
# =============================================================================

cur.close()
conn.close()
print("\nAll migrations complete ✓")