import pandas as pd
import json
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../flick_pick')))
from models import Movie, Tag
from database import Base, get_db

# Database connection settings
DATABASE_URL = "mysql+pymysql://root:password@localhost/flick_pick"

# Create database engine
engine = create_engine(DATABASE_URL)
Base.metadata.create_all(engine)
SessionLocal = sessionmaker(bind=engine)
session = SessionLocal()

# Load CSV
csv_file = "movie_catalogue.csv"
df = pd.read_csv(csv_file)

unique_years = set()
unique_genres = set()
unique_languages = set()

# Extract unique tag values
for _, row in df.iterrows():
    # Extract release year
    if pd.notna(row['release_date']):
        release_year = row['release_date'].split("-")[0]
        unique_years.add(release_year)

    # Extract genres
    if pd.notna(row['genres']):
        try:
            genres_list = json.loads(row['genres'].replace("'", '"'))  # Convert from string to list
            unique_genres.update(genres_list)
        except json.JSONDecodeError:
            print(f"Error parsing genres: {row['genres']}")

    # Extract original language
    if pd.notna(row['original_language']):
        unique_languages.add(row['original_language'])

# Insert tags into the database
for tag_name in unique_years:
    tag_id = tag_name.lower().replace(" ", "_")
    tag = Tag(tagID=tag_id, tagName=tag_name, tagType="year")
    session.add(tag)

for tag_name in unique_genres:
    tag_id = tag_name.lower().replace(" ", "_")
    tag = Tag(tagID=tag_id, tagName=tag_name, tagType="genre")
    session.add(tag)

for tag_name in unique_languages:
    tag_id = tag_name.lower().replace(" ", "_")
    tag = Tag(tagID=tag_id, tagName=tag_name, tagType="language")
    session.add(tag)

print(f"Inserted {len(unique_years | unique_genres | unique_languages)} tags.")

# ---------------- Populate Movies Table ----------------
for _, row in df.iterrows():
    try:
        # Extract release year
        release_year = int(row['release_date'].split("-")[0]) if pd.notna(row['release_date']) else None

        # Convert genres from string format to actual JSON
        genres_list = json.loads(row['genres'].replace("'", '"')) if pd.notna(row['genres']) else []

        # Create a Movie object
        movie = Movie(
            movieID=row['movie_id'],
            tmdb_id=row['tmdb_id'],
            releaseYear=release_year,
            genres=genres_list,
            original_language=row['original_language'] if pd.notna(row['original_language']) else None,
            runtime=row['runtime'] if pd.notna(row['runtime']) else None,
            rating=row['vote_average'] if pd.notna(row['vote_average']) else None,
            poster_path=row['poster_path'] if pd.notna(row['poster_path']) else None,
            title=row['title'],
            description=row['overview'] if pd.notna(row['overview']) else None
        )

        # Add to session
        session.add(movie)
    except Exception as e:
        print(f"Error inserting movie {row['movie_id']}: {e}")

print("Movies and tags table populated successfully!")

# Commit all changes
session.commit()

# Close session
session.close()