from datetime import datetime
import pandas as pd
import numpy as np
from pathlib import Path
import requests
import urllib
import time
from tqdm import tqdm

TMDB_API_KEY = '35ab778cf8216cf10027c6442345d4b6'

def process_ratings():
    # Combine the ratings in /data/raw/ratings_export.csv and /data/raw/ratings.csv
    df1 = pd.read_csv('./data/raw/ratings.csv')
    df1.rename(columns={'film_id':'movie_id', 'user_name':'user_id'}, inplace=True)

    df2 = pd.read_csv('./data/raw/ratings_export.csv', usecols=['user_id', 'movie_id', 'rating_val'])
    df2.rename(columns={'rating_val':'rating'}, inplace=True)
    df2['rating'] = df2['rating'] / 2
    df2 = df2[['user_id', 'movie_id', 'rating']]

    ratings_df = pd.concat([df1, df2]).drop_duplicates(subset=['user_id', 'movie_id'], ignore_index=True)

    # only include films with >= 500 reviews
    counts = ratings_df['movie_id'].value_counts()
    ratings_df.drop(ratings_df[~ratings_df['movie_id'].isin(counts[counts >= 500].index)].index, inplace=True)

    Path('./data/temp').mkdir(exist_ok=True)
    ratings_df.to_csv('./data/temp/ratings.csv', index=False)

    print('Finished preparing ratings')

def process_movies():
    # Combine the movies in /data/raw/movie_data.csv and /data/raw/films.csv
    cols = ['movie_id', 'tmdb_id', 'movie_title', 'year_released']
    df1 = pd.read_csv('./data/raw/movie_data.csv', usecols=cols, engine="python")
    df1.rename(columns={'year_released':'year'}, inplace=True)

    df2 = pd.read_csv('./data/raw/films.csv', usecols=['film_id', 'film_name', 'year'])
    df2.rename(columns={'film_id':'movie_id', 'film_name':'movie_title'}, inplace=True)

    movies_df = pd.concat([df1, df2]).drop_duplicates(subset=['movie_id'], ignore_index=True)

    # drop all rows for movies that don't have ratings (e.g. if there weren't enough reviews, we may have dropped them)
    ratings_df = pd.read_csv('./data/temp/ratings.csv')
    movies_df.drop(movies_df[~movies_df['movie_id'].isin(ratings_df['movie_id'].unique())].index, inplace=True)

    Path('./data/temp').mkdir(exist_ok=True)
    movies_df.to_csv('./data/temp/movies.csv', index=False)

    print('Finished preparing movies')

def enrich_movies():
    # Enriches data with poster path, overview, genres, vote average, original language, genres, runtime from tmdb
    movies_df = pd.read_csv('./data/temp/movies.csv')
    enriched_rows = []
    success_cnt = 0
    with requests.Session() as session:
        for row in tqdm(movies_df.itertuples()):
            while True:
                if np.isnan(row.tmdb_id):
                    payload = {
                        'api_key': TMDB_API_KEY,
                        'query': row.movie_title,
                        'year': row.year
                    }
                    param = urllib.parse.urlencode(payload, quote_via=urllib.parse.quote)
                    response = session.get('https://api.themoviedb.org/3/search/movie', params=param)
                    time.sleep(0.02)
                    if response.status_code == 429:
                        time.sleep(2) # Sleep and retry if we sent too many requests
                        continue
                    if response.status_code != 200:
                        break  # Can't enrich data for this movie, skip
                    responsejson = response.json()
                    if len(responsejson['results']) == 0:
                        break
                    movie_info = responsejson['results'][0]
                    if movie_info['title'] != row.movie_title:
                        break
                    tmdb_id = movie_info['id']
                else:
                    tmdb_id = row.tmdb_id
                response = session.get(f'https://api.themoviedb.org/3/movie/{tmdb_id}?api_key={TMDB_API_KEY}')
                time.sleep(0.02)
                if response.status_code == 429:
                    time.sleep(2) # Sleep and retry if we sent too many requests
                    continue
                elif response.status_code != 200:
                    break # Can't enrich data for this movie, skip
                responsejson = response.json()
                genres = [genre['name'] for genre in responsejson['genres']]
                new_row = [
                    row.movie_id,
                    int(tmdb_id),
                    responsejson['release_date'],
                    genres,
                    responsejson['original_language'],
                    responsejson['runtime'],
                    responsejson['vote_average'] / 2, # 10 point scale to 5 point scale
                    responsejson['poster_path'],
                    responsejson['title'],
                    responsejson['overview'],
                ]
                enriched_rows.append(new_row)
                success_cnt += 1
                break

    enriched_df = pd.DataFrame(enriched_rows,
        columns=['movie_id', 'tmdb_id', 'release_date', 'genres', 'original_language',
                 'runtime', 'vote_average', 'poster_path', 'title', 'overview'])

    Path('./data/temp').mkdir(exist_ok=True)
    enriched_df.to_csv('./data/temp/enriched.csv', index=False)

    print('Successfully enriched data for', success_cnt, 'movies')
    print('Finished enriching data')

def clean_movies_and_ratings():
    ratings_df = pd.read_csv('./data/temp/ratings.csv')
    movies_df = pd.read_csv('./data/temp/enriched.csv')

    # drop movie_ids with missing data
    required_cols = ['movie_id', 'tmdb_id', 'title', 'genres']
    movies_df.dropna(subset=required_cols, inplace=True)
    movies_df.drop(movies_df[movies_df['genres'].astype(str) == '[]'].index, inplace=True)

    # if a movie_id is dropped, we should drop all ratings for it too
    ratings_df.drop(ratings_df[~ratings_df['movie_id'].isin(movies_df['movie_id'].unique())].index, inplace=True)

    movies_df['tmdb_id'] = movies_df['tmdb_id'].astype(int)
    movies_df['vote_average'] = movies_df['vote_average'].apply(lambda x : round(x, 1))

    movies_df.sort_values(by='movie_id', ignore_index=True, inplace=True)

    # drop columns not relevant to recommender filters
    movies_simple_df = movies_df.drop(columns=['tmdb_id', 'poster_path', 'title', 'overview'])
    movies_simple_df['release_date'] = movies_simple_df['release_date'].apply(
        lambda x : datetime.strptime(x, "%Y-%m-%d").year)
    movies_simple_df.rename(columns={'release_date': 'year'}, inplace=True)

    Path('./data/processed').mkdir(exist_ok=True)
    movies_simple_df.to_csv('./data/processed/movies_simple.csv', index=False)
    movies_df.to_csv('./data/processed/movies.csv', index=False)
    ratings_df.to_csv('./data/processed/ratings.csv', index=False)

    print('Unique users: ', len(ratings_df['user_id'].unique()))
    print('Unique films: ', len(ratings_df['movie_id'].unique()))
    print('Number of ratings: ', len(ratings_df))

    print('Finished cleaning up')

# process_ratings()
# process_movies()
enrich_movies()
clean_movies_and_ratings()

# Can delete /temp/ dir after running this