from surprise import KNNBaseline, Dataset, Reader
from surprise import dump
import pandas as pd

df = pd.read_csv('./data/ratings.csv')
reader = Reader(rating_scale=(0.5, 5))
dataset = Dataset.load_from_df(df[['user_id', 'movie_id', 'rating']], reader)

print("Unique users: ", len(df['user_id'].unique()))
print("Unique movies: ", len(df['movie_id'].unique()))
print("Number of ratings: ", len(df))

sim_options = {
    'name': 'pearson_baseline',
    'user_based': False
}
model = KNNBaseline(sim_options=sim_options)
model.fit(dataset.build_full_trainset())

dump.dump("./model", algo=model)