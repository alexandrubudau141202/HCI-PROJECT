"""
MongoDB Migration Script
Loads ratings_clean.csv and favs_clean.csv into anime_db
Run: python migrate_mongo.py
"""

import pandas as pd
from pymongo import MongoClient, UpdateOne
from pymongo.errors import BulkWriteError
import math

CLEAN_PATH = '/Users/alexandrubudau/Desktop/HCI-PROJECT/cleaned datasets/'
BATCH_SIZE = 10_000

# =============================================================================
# CONNECTION
# =============================================================================

client = MongoClient('mongodb://localhost:27017/')
db     = client['anime_db']

print("Connected to MongoDB ✓\n")


# =============================================================================
# HELPER
# =============================================================================

def clean_value(val):
    """Convert NaN/float NaN to None for MongoDB."""
    if val is None:
        return None
    try:
        if isinstance(val, float) and math.isnan(val):
            return None
    except:
        pass
    return val

def bulk_upsert(collection, docs, filter_keys):
    """Upsert documents in batches, skipping duplicates."""
    operations = [
        UpdateOne(
            {k: doc[k] for k in filter_keys},
            {'$set': doc},
            upsert=True
        )
        for doc in docs
    ]
    try:
        result = collection.bulk_write(operations, ordered=False)
        return result.upserted_count + result.modified_count
    except BulkWriteError as e:
        # log but don't crash on duplicate key errors
        print(f"  Warning: {len(e.details['writeErrors'])} write errors skipped")
        return 0


# =============================================================================
# RATINGS
# =============================================================================

print("Migrating: ratings...")
print("  (this will take a few minutes — 124M rows)")

ratings_col = db['ratings']
total_inserted = 0
chunk_num = 0

for chunk in pd.read_csv(CLEAN_PATH + 'ratings_clean.csv', chunksize=1_000_000):
    chunk_num += 1

    docs = []
    for _, r in chunk.iterrows():
        docs.append({
            'username'             : clean_value(r.get('username')),
            'anime_id'             : clean_value(r.get('anime_id')),
            'status'               : clean_value(r.get('status')),
            'score'                : clean_value(r.get('score')),
            'is_rewatching'        : int(r.get('is_rewatching', 0) or 0),
            'num_watched_episodes' : int(r.get('num_watched_episodes', 0) or 0),
        })

    try:
        ratings_col.insert_many(docs, ordered=False)
    except Exception as e:
        pass  # skip duplicates silently

    total_inserted += len(docs)
    print(f"  Chunk {chunk_num} — {total_inserted:,} rows processed...")


# =============================================================================
# FAVS
# =============================================================================

print("\nMigrating: favs...")

favs_col = db['favs']
df_favs  = pd.read_csv(CLEAN_PATH + 'favs_clean.csv')

total_favs = 0
for i in range(0, len(df_favs), BATCH_SIZE):
    batch = df_favs.iloc[i:i + BATCH_SIZE]

    docs = []
    for _, r in batch.iterrows():
        docs.append({
            'username' : clean_value(r.get('username')),
            'fav_type' : clean_value(r.get('fav_type')),
            'id'       : clean_value(r.get('id')),
        })

    bulk_upsert(favs_col, docs, filter_keys=['username', 'fav_type', 'id'])
    total_favs += len(docs)

print(f"  Done — {total_favs:,} favs inserted ✓")


# =============================================================================
# VERIFY
# =============================================================================

print("\nVerification:")
print(f"  ratings count : {ratings_col.estimated_document_count():,}")
print(f"  favs count    : {favs_col.estimated_document_count():,}")

client.close()
print("\nAll MongoDB migrations complete ✓")