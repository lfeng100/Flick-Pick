from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import schemas, database, crud
from typing import Optional, List

router = APIRouter()

@router.post("/movies/")
def create_movie(movie: schemas.MovieCreate, db: Session = Depends(database.get_db)):
    return crud.create_movie(db, movie)

@router.get("/movies/")
def read_movies(db: Session = Depends(database.get_db)):
    return crud.get_movies(db)

@router.delete("/movies/{movie_id}")
def delete_movie(movie_id: str, db: Session = Depends(database.get_db)):
    return crud.delete_movie(db, movie_id)

@router.get("/movies/search/")
def search_movies(
    title_query: Optional[str] = None, tag_ids: Optional[List[str]] = None, db: Session = Depends(database.get_db)
):
    return crud.search_movies(db, title_query, tag_ids)
