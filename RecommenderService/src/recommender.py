from surprise import dump
import pandas as pd
import numpy as np
import ast

from src.filter import *

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

    def combine_group_rating_weights(self, group_ratings: list):
        # Harmonic mean of group ratings
        movie_weights = {}
        for user_ratings in group_ratings:
            for rating in user_ratings:
                movie_id = rating['movieID']
                score = rating['score']
                if movie_id not in movie_weights:
                    movie_weights[movie_id] = []
                movie_weights[movie_id].append(score)
        epsilon = 1e-9
        combined_ratings = []
        for movie_id, weights in movie_weights.items():
            combined_score = len(weights) / sum(1 / (w + epsilon) for w in weights)
            combined_ratings.append({'movieID': movie_id, 'score': combined_score})
        return combined_ratings

    def get_k_nearest_neightbours(self, movie_id: str, k: int):
        inner_id = self.to_inner_id(movie_id)
        neighbors_inner_ids = self.model.get_neighbors(inner_id, k=k)
        neighbors_movie_ids = [self.to_raw_id(inner_id) for inner_id in neighbors_inner_ids]
        return neighbors_movie_ids

    def get_k_recommendations(self, ratings: list, filters: dict, k: int):
        rating_inner_ids = {self.to_inner_id(rating['movieID']) for rating in ratings}
        scores = {}
        for inner_id in self.filter_inner_ids(filters):
            if inner_id in rating_inner_ids:
                continue
            avg_sim = np.mean([self.weighted_similarity(rating, inner_id) for rating in ratings])
            scores[inner_id] = avg_sim
        sorted_scores = sorted(scores.items(), key=lambda x: x[1], reverse=True)
        return [(self.to_raw_id(inner_id), score) for inner_id, score in sorted_scores[:k]]

    def filter_inner_ids(self, filters: dict):
        filterList = []
        if filters != None:
            if filters['minYear'] != None:
                filterList.append(MinYearFilter(filters['minYear']))
            if filters['maxYear'] != None:
                filterList.append(MaxYearFilter(filters['maxYear']))
            if filters['languages'] != None:
                filterList.append(LanguagesFilter(filters['languages']))
            if filters['maxRuntime'] != None:
                filterList.append(RuntimeFilter(filters['maxRuntime']))
            if filters['minScore'] != None:
                filterList.append(ScoreFilter(filters['minScore']))
            if filters['excludedMovieIDs'] != None:
                filterList.append(ExcludedMovieIdFilter(filters['excludedMovieIDs']))
            if filters['includedGenres'] != None:
                filterList.append(IncludedGenresFilter(filters['includedGenres']))
            if filters['excludedGenres'] != None:
                filterList.append(ExcludedGenresFilter(filters['excludedGenres']))

        def allow_movie(movie_id: str, movie_info: dict) -> bool:
            for filter in filterList:
                if not filter.allow(movie_id, movie_info):
                    return False
            return True

        for inner_id in range(len(self.model.sim)):
            movie_id = self.to_raw_id(inner_id)
            movie_info = self.movie_info_map[movie_id]
            if allow_movie(movie_id, movie_info):
                yield inner_id

    def weighted_similarity(self, rating: dict, inner_id: int):
        weight = rating['score'] / 5
        similarity = self.model.sim[inner_id, self.to_inner_id(rating['movieID'])]
        return weight * similarity