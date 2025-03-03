import pandas as pd
import json
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm import Session

import sys
import os
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '../flick_pick')))
from models import Movie, Tag, MovieTag
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

def table_has_data(db_session: Session, model):
    """ Check if the table has existing entries """
    return db_session.query(model).first() is not None

# ---------------- Populate Tags Table ----------------
if not table_has_data(session, Tag):
    # Extract unique tag values
    unique_years = set()
    unique_genres = set()
    unique_languages = set()

    for _, row in df.iterrows():
        if pd.notna(row['release_date']):
            release_year = row['release_date'].split("-")[0]
            unique_years.add(release_year)

        if pd.notna(row['genres']):
            try:
                genres_list = json.loads(row['genres'].replace("'", '"'))
                unique_genres.update(genres_list)
            except json.JSONDecodeError:
                print(f"Error parsing genres: {row['genres']}")

        if pd.notna(row['original_language']):
            unique_languages.add(row['original_language'])

    for tag_name in unique_years | unique_genres | unique_languages:
        tag_id = tag_name.lower().replace(" ", "_")
        tag = Tag(tagID=tag_id, tagName=tag_name, tagType="year" if tag_name in unique_years else "genre" if tag_name in unique_genres else "language")
        session.add(tag)

    print(f"Inserted {len(unique_years | unique_genres | unique_languages)} tags.")
else:
    print("Skipping Tag population: Table already has data.")

# ---------------- Populate Movies Table ----------------
if not table_has_data(session, Movie):
    for _, row in df.iterrows():
        try:
            release_year = int(row['release_date'].split("-")[0]) if pd.notna(row['release_date']) else None
            genres_list = json.loads(row['genres'].replace("'", '"')) if pd.notna(row['genres']) else []

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

            session.add(movie)

        except Exception as e:
            print(f"Error inserting movie {row['movie_id']}: {e}")

    print("Movies table populated successfully!")
else:
    print("Skipping Movie population: Table already has data.")

# ---------------- Populate MovieTags Table ----------------
if not table_has_data(session, MovieTag):
    for _, row in df.iterrows():
        try:
            genres_list = json.loads(row['genres'].replace("'", '"')) if pd.notna(row['genres']) else []

            for genre in genres_list:
                tag_id = genre.lower().replace(" ", "_")
                movie_tag = MovieTag(movieID=row['movie_id'], tagID=tag_id)
                session.add(movie_tag)

            if pd.notna(row['release_date']):
                release_year = row['release_date'].split("-")[0]
                movie_tag = MovieTag(movieID=row['movie_id'], tagID=release_year)
                session.add(movie_tag)

            if pd.notna(row['original_language']):
                tag_id = row['original_language'].lower()
                movie_tag = MovieTag(movieID=row['movie_id'], tagID=tag_id)
                session.add(movie_tag)

        except Exception as e:
            print(f"Error inserting MovieTag for {row['movie_id']}: {e}")

    print("MovieTags table populated successfully!")
else:
    print("Skipping MovieTags population: Table already has data.")

print("Movies, Tags, and MovieTags table populated successfully!")

# Commit all changes
session.commit()

# Close session
session.close()