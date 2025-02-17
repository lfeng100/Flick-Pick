import pandas as pd
from pathlib import Path
import requests
import urllib
import time
from tqdm import tqdm

TMDB_API_KEY = '35ab778cf8216cf10027c6442345d4b6'

def prep_ratings():
    # Combine the ratings in /raw/ratings_export.csv and /raw/ratings.csv

    df1 = pd.read_csv('./raw/ratings.csv')
    df1.rename(columns={'film_id':'movie_id', 'user_name':'user_id'}, inplace=True)

    df2 = pd.read_csv('./raw/ratings_export.csv', usecols=['user_id', 'movie_id', 'rating_val'])
    df2.rename(columns={'rating_val':'rating'}, inplace=True)
    df2['rating'] = df2['rating'] / 2
    df2 = df2[['user_id', 'movie_id', 'rating']]

    ratings_df = pd.concat([df1, df2]).drop_duplicates(subset=['user_id', 'movie_id'], ignore_index=True)

    # only include films with >= 500 reviews
    counts = ratings_df['movie_id'].value_counts()
    ratings_df.drop(ratings_df[~ratings_df['movie_id'].isin(counts[counts >= 500].index)].index, inplace=True)

    Path("./temp").mkdir(exist_ok=True)
    ratings_df.to_csv("./temp/ratings_prepped.csv", index=False)

    print("Finished preparing ratings")


def prep_movies():
    ratings_df = pd.read_csv('./temp/ratings_prepped.csv')

    # /raw/movie_data.csv columns
    # _id genres image_url imdb_id imdb_link movie_id movie_title original_language
    # overview popularity production_countries release_date runtime spoken_languages
    # tmdb_id tmdb_link vote_average vote_count year_released

    cols = ['movie_id', 'tmdb_id', 'movie_title', 'genres', 'original_language', 'overview', 'year_released']
    movies_df = pd.read_csv('./raw/movie_data.csv', usecols=cols, engine="python")
    movies_df = movies_df[cols]
    movies_df.rename(columns={'year_released':'year'}, inplace=True)

    # drop all rows for movies that don't have ratings (e.g. if there weren't enough reviews, we may have dropped them)
    movies_df.drop(movies_df[~movies_df['movie_id'].isin(ratings_df['movie_id'].unique())].index, inplace=True)

    Path("./temp").mkdir(exist_ok=True)
    movies_df.to_csv("./temp/movies_prepped.csv", index=False)

    print("Finished preparing movies")


def fill_missing_data():
    # query tmdb for movie data that is not already in /temp/movie_data.csv
    ratings_df = pd.read_csv('./temp/ratings_prepped.csv')
    movies_df = pd.read_csv('./temp/movies_prepped.csv')
    missing_df = pd.read_csv('./raw/films.csv', usecols=['film_id', 'film_name', 'year'])
    missing_df.rename(columns={'film_id':'movie_id', 'film_name':'movie_title'}, inplace=True)
    missing_df.drop(missing_df[~missing_df['movie_id'].isin(ratings_df['movie_id'].unique())].index, inplace=True)
    missing_df.drop(missing_df[missing_df['movie_id'].isin(movies_df['movie_id'].unique())].index, inplace=True)

    genre_url = f"https://api.themoviedb.org/3/genre/movie/list?api_key={TMDB_API_KEY}"
    genre_response = requests.get(genre_url).json()
    genre_dict = {genre["id"]: genre["name"] for genre in genre_response["genres"]}

    url = 'https://api.themoviedb.org/3/search/movie'
    cnt = 0

    for row in tqdm(missing_df.itertuples()):
        payload = {
            'api_key': TMDB_API_KEY,
            'query': row.movie_title,
            'year': row.year
        }
        param = urllib.parse.urlencode(payload, quote_via=urllib.parse.quote)
        response = requests.get(url, params=param)
        time.sleep(0.03) # prevent going over api limit
        if response.status_code != 200:
            continue
        responsejson = response.json()
        if len(responsejson['results']) == 0:
            continue
        movie_info = responsejson['results'][0]
        genres = [genre_dict.get(genre_id) for genre_id in movie_info['genre_ids']]
        new_row = [row.movie_id, movie_info['id'], row.movie_title, genres,
                   movie_info['original_language'], movie_info['overview'], row.year]
        movies_df.loc[len(movies_df)] = new_row
        cnt += 1

    print("Number of successful updates: ", cnt)
    movies_df = pd.concat([movies_df, missing_df], ignore_index=True)

    Path("./temp").mkdir(exist_ok=True)
    movies_df.to_csv("./temp/movies_missing_filled.csv", index=False)

    print("Finished requesting missing movie information")


def clean_movies_and_ratings():
    ratings_df = pd.read_csv('./temp/ratings_prepped.csv')
    movies_df = pd.read_csv('./temp/movies_missing_filled.csv')

    # drop movie_ids with missing data
    required_cols = ['movie_id', 'tmdb_id', 'movie_title', 'genres']
    movies_df.dropna(subset=required_cols, inplace=True)
    movies_df.drop(movies_df[movies_df['genres'].astype(str) == '[]'].index, inplace=True)

    # if a movie_id is dropped, we should drop all ratings for it too
    ratings_df.drop(ratings_df[~ratings_df['movie_id'].isin(movies_df['movie_id'].unique())].index, inplace=True)

    movies_df['tmdb_id'] = movies_df['tmdb_id'].astype(int)
    movies_df['year'] = movies_df['year'].astype('Int64')
    movies_df.sort_values(by='movie_id', ignore_index=True, inplace=True)

    Path("./data").mkdir(exist_ok=True)
    movies_df.to_csv("./data/movies.csv", index=False)
    ratings_df.to_csv("./data/ratings.csv", index=False)

    print("Unique users: ", len(ratings_df['user_id'].unique()))
    print("Unique films: ", len(ratings_df['movie_id'].unique()))
    print("Number of ratings: ", len(ratings_df))

    print("Finished cleaning up")


def sanity_check():
    ratings_df = pd.read_csv('./data/ratings.csv')
    movies_df = pd.read_csv('./data/movies.csv')
    if len(ratings_df['movie_id'].unique()) != len(movies_df):
        print("Ratings and movies don't match")


prep_ratings()
prep_movies()
fill_missing_data()
clean_movies_and_ratings()
sanity_check()

# Can delete /temp/ dir after running this