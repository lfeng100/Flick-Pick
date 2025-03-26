from fastapi import FastAPI
from src.recommender import Recommender
from pydantic import BaseModel, field_validator
from typing import List, Optional

recommender = Recommender()
app = FastAPI()

class Rating(BaseModel):
    movieID: str
    score: float

    @field_validator("movieID")
    @classmethod
    def check_movieID(cls, movieID):
        if not recommender.has_movie_id(movieID):
            raise ValueError("invalid movieID")
        return movieID

    @field_validator("score")
    @classmethod
    def check_score(cls, score):
        if score < 0.5:
            raise ValueError("rating scores must be >= 0.5")
        if score > 5.0:
            raise ValueError("rating scores must be <= 5.0")
        if score % 0.5 != 0:
            raise ValueError("rating scores must be a multiple of 0.5")
        return score

class Filters(BaseModel):
    includedGenres: Optional[List[str]] = None
    excludedGenres: Optional[List[str]] = None
    minYear: Optional[int] = None
    maxYear: Optional[int] = None
    languages: Optional[List[str]] = None
    maxRuntime: Optional[int] = None
    minScore: Optional[float] = None
    excludedMovieIDs: Optional[List[str]] = None

    @field_validator("includedGenres", "excludedGenres")
    @classmethod
    def check_genres(cls, genres):
        if genres == None:
            return genres
        for genre in genres:
            if not recommender.has_movie_genre(genre):
                raise ValueError('invalid movie genre')
        return genres

    @field_validator("languages")
    @classmethod
    def check_languages(cls, languages):
        if languages == None:
            return languages
        for language in languages:
            if not recommender.has_language_code(language):
                raise ValueError('invalid language code')
        return languages

    @field_validator("minScore")
    @classmethod
    def check_score(cls, score):
        if score < 0:
            raise ValueError("minimum score must be >= 0.0")
        if score > 5.0:
            raise ValueError("minimum score must be <= 5.0")
        return score

    @field_validator("excludedMovieIDs")
    @classmethod
    def check_movieIDs(cls, movieIDs):
        if movieIDs == None:
            return movieIDs
        for movie_id in movieIDs:
            if not recommender.has_movie_id(movie_id):
                raise ValueError('invalid movie_id')
        return movieIDs

class Query(BaseModel):
    ratings: List[Rating]
    filters: Optional[Filters] = None

class GroupRecQuery(BaseModel):
    groupRatings: List[List[Rating]]
    filters: Optional[Filters] = None

@app.post('/recommend')
async def recommend(query: Query):
    query_dict = query.model_dump()
    recommendations = recommender.get_k_recommendations(query_dict['ratings'], query_dict['filters'], 12)
    result = [recomendation[0] for recomendation in recommendations]
    return {"recommendations": result}

@app.post('/grouprec')
async def grouprec(query: GroupRecQuery):
    query_dict = query.model_dump()
    combined_ratings = recommender.combine_group_rating_weights(query_dict['groupRatings'])
    recommendations = recommender.get_k_recommendations(combined_ratings, query_dict['filters'], 12)
    result = [recomendation[0] for recomendation in recommendations]
    return {"recommendations": result}