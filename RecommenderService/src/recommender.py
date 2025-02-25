from surprise import dump
import pandas as pd
import numpy as np
import ast

class Recommender:
    def __init__(self):
        _, model = dump.load('./model/model')
        self.model = model
        self.to_inner_id = self.model.trainset.to_inner_iid
        self.to_raw_id = self.model.trainset.to_raw_iid
        df = pd.read_csv('./data/processed/movies_simple.csv')
        df['genres'] = df['genres'].apply(lambda x: set(ast.literal_eval(x)))
        self.movie_info_map = df.set_index('movie_id').to_dict('index')
        self.genres = set()
        self.languages = set()
        for movie_info in self.movie_info_map.values():
            self.genres.update(movie_info['genres'])
            self.languages.add(movie_info['original_language'])

    def has_language_code(self, language: str):
        return language in self.languages

    def has_movie_genre(self, genre: str):
        return genre in self.genres

    def has_movie_id(self, movie_id: str):
        return movie_id in self.movie_info_map.keys()

    def get_k_nearest_neightbours(self, movie_id: str, k: int):
        inner_id = self.to_inner_id(movie_id)
        neighbors_inner_ids = self.model.get_neighbors(inner_id, k=k)
        neighbors_movie_ids = [self.to_raw_id(inner_id) for inner_id in neighbors_inner_ids]
        return neighbors_movie_ids

    def get_k_recommendations(self, ratings: list, filters: dict, k: int):
        rating_inner_ids = {self.to_inner_id(rating['movie_id']) for rating in ratings}
        scores = {}
        for inner_id in self.filter_inner_ids(filters):
            if inner_id in rating_inner_ids:
                continue
            avg_sim = np.mean([self.weighted_similarity(rating, inner_id) for rating in ratings])
            scores[inner_id] = avg_sim
        sorted_scores = sorted(scores.items(), key=lambda x: x[1], reverse=True)
        return [(self.to_raw_id(inner_id), score) for inner_id, score in sorted_scores[:k]]

    def filter_inner_ids(self, filters: dict):
        if filters == None:
            # If no filter, simply go through all inner_ids
            for inner_id in range(len(self.model.sim)):
                yield inner_id
            return

        included_genres = filters['included_genres']
        excluded_genres = filters['excluded_genres']
        min_year = filters['min_year']
        max_year = filters['max_year']
        languages = filters['languages']
        if languages != None:
            languages = set(languages)
        max_runtime = filters['max_runtime']
        min_score = filters['min_score']

        for inner_id in range(len(self.model.sim)):
            movie_id = self.to_raw_id(inner_id)
            movie_info = self.movie_info_map[movie_id]

            genres = movie_info['genres']
            year = movie_info['year']
            original_language = movie_info['original_language']
            runtime = movie_info['runtime']
            vote_average = movie_info['vote_average']

            if included_genres != None and genres.isdisjoint(included_genres):
                continue
            if excluded_genres != None and not genres.isdisjoint(excluded_genres):
                continue
            if min_year != None and min_year > year:
                continue
            if max_year != None and max_year < year:
                continue
            if languages != None and original_language not in languages:
                continue
            if max_runtime != None and max_runtime < runtime:
                continue
            if min_score != None and min_score > vote_average:
                continue
            yield inner_id

    def weighted_similarity(self, rating: dict, inner_id: int):
        weight = rating['score'] / 5
        similarity = self.model.sim[inner_id, self.to_inner_id(rating['movie_id'])]
        return weight * similarity

