import pandas as pd
import psycopg2

conn = psycopg2.connect(
    dbname='HCI_Project',
    user='alexandrubudau',
    host='localhost'
)
cur = conn.cursor()
print("Connected ✓")

RAW_PATH = '/Users/alexandrubudau/Desktop/HCI-PROJECT/datasets/'

# Anime images
print("Updating anime images...")
df = pd.read_csv(RAW_PATH + 'details.csv', usecols=['mal_id', 'image_url'])
df = df.dropna(subset=['image_url'])
for _, r in df.iterrows():
    cur.execute("UPDATE anime SET image_url = %s WHERE mal_id = %s",
                (r['image_url'], int(r['mal_id'])))
conn.commit()
print(f"  Done: {len(df):,} anime images ✓")

# Character images
print("Updating character images...")
df = pd.read_csv(RAW_PATH + 'characters.csv', usecols=['character_mal_id', 'image'])
df = df.dropna(subset=['image'])
for _, r in df.iterrows():
    cur.execute("UPDATE characters SET image_url = %s WHERE character_mal_id = %s",
                (r['image'], int(r['character_mal_id'])))
conn.commit()
print(f"  Done: {len(df):,} character images ✓")

# People images
print("Updating people images...")
df = pd.read_csv(RAW_PATH + 'person_details.csv', usecols=['person_mal_id', 'image_url'])
df = df.dropna(subset=['image_url'])
for _, r in df.iterrows():
    cur.execute("UPDATE people SET image_url = %s WHERE person_mal_id = %s",
                (r['image_url'], int(r['person_mal_id'])))
conn.commit()
print(f"  Done: {len(df):,} people images ✓")

cur.close()
conn.close()
print("\nAll image migrations complete ✓")