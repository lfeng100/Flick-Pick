from surprise import KNNBasic, Dataset, Reader
from surprise import dump
import pandas as pd

df = pd.read_csv('data/ratings.csv')
cnts = df['film_id'].value_counts()
relevant_films = cnts[cnts > 100].index
filtered_df = df[df['film_id'].isin(relevant_films)]
print("Unique users: ", len(filtered_df['user_name'].unique()))
print("Unique films: ", len(filtered_df['film_id'].unique()))
print("Number of ratings: ", len(filtered_df))

reader = Reader(rating_scale=(0.5, 5))
dataset = Dataset.load_from_df(filtered_df[['user_name', 'film_id', 'rating']], reader)

sim_options = {
    'name': 'cosine',
    'user_based': False
}
model = KNNBasic(sim_options=sim_options)
model.fit(dataset.build_full_trainset())

dump.dump("./model", algo=model)