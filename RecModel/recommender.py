from surprise import dump
import pandas as pd
import numpy as np
import ast

class Recommender:
    def __init__(self):
        _, model = dump.load('./model')
        self.model = model
        self.to_inner_id = self.model.trainset.to_inner_iid
        self.to_raw_id = self.model.trainset.to_raw_iid
        df = pd.read_csv('./data/movies.csv')
        df['genres'] = df['genres'].apply(ast.literal_eval)
        self.genre_map = df.set_index('movie_id')['genres'].to_dict()


    def hasMovieId(self, movie_id: str):
        return movie_id in self.genre_map.keys()


    def getGenres(self, movie_id: str):
        return self.genre_map.get(movie_id)


    def getKNearestNeighbours(self, movie_id: str, k: int):
        inner_id = self.to_inner_id(movie_id)
        neighbors_inner_ids = self.model.get_neighbors(inner_id, k=k)
        neighbors_movie_ids = [self.to_raw_id(inner_id) for inner_id in neighbors_inner_ids]
        return neighbors_movie_ids


    # ratings is a dictionary from movie ids to a numeric score
    # filters is a set of tags
    def getKRecommendations(self, ratings: dict, filter: set, k: int):
        rating_inner_ids = [self.to_inner_id(i) for i in ratings.keys()]
        scores = {}
        for inner_id in range(len(self.model.sim)):
            if inner_id in rating_inner_ids:
                continue
            if filter and filter.isdisjoint(self.getGenres(self.to_raw_id(inner_id))):
                continue
            avg_sim = np.mean([self.model.sim[inner_id, rating_id] for rating_id in rating_inner_ids])
            scores[inner_id] = avg_sim
        sorted_scores = sorted(scores.items(), key=lambda x: x[1], reverse=True)
        return [(self.to_raw_id(inner_id), score) for inner_id, score in sorted_scores[:k]]
