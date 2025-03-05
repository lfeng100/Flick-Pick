from fastapi import FastAPI
from src.recommender import Recommender
from pydantic import BaseModel, field_validator
from typing import List, Optional

recommender = Recommender()
app = FastAPI()

class Rating(BaseModel):
    movie_id: str
    score: float

    @field_validator("movie_id")
    @classmethod
    def check_movie_id(cls, movie_id):
        if not recommender.has_movie_id(movie_id):
            raise ValueError("invalid movie_id")
        return movie_id

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
    included_genres: Optional[List[str]] = None
    excluded_genres: Optional[List[str]] = None
    min_year: Optional[int] = None
    max_year: Optional[int] = None
    languages: Optional[List[str]] = None
    max_runtime: Optional[int] = None
    min_score: Optional[float] = None

    @field_validator("included_genres", "excluded_genres")
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

    @field_validator("min_score")
    @classmethod
    def check_score(cls, score):
        if score < 0:
            raise ValueError("minimum score must be >= 0.0")
        if score > 5.0:
            raise ValueError("minimum score must be <= 5.0")
        return score

class Query(BaseModel):
    ratings: List[Rating]
    filters: Optional[Filters] = None

@app.post('/recommend')
async def recommend(query: Query):
    query_dict = query.model_dump()
    recommendations = recommender.get_k_recommendations(query_dict['ratings'], query_dict['filters'], 13)
    result = [reccomendation[0] for reccomendation in recommendations]
    return {"recommendations": result}

@app.get('/genres')
async def genres():
    return {"genres": list(recommender.genres)}

@app.get('/languages')
async def languages():
    return {"languages": list(recommender.languages)}