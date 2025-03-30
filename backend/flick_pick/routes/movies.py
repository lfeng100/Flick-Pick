from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.orm import Session
import schemas, database, crud
from typing import Optional, List

router = APIRouter()

@router.post("/movies/")
def create_movie(movie: schemas.MovieCreate, db: Session = Depends(database.get_db)):
    return crud.create_movie(db, movie)

@router.get("/movie/{movie_id}")
def read_movie(movie_id: str, db: Session = Depends(database.get_db)):
    movie = crud.get_movie(db, movie_id)
    if not movie:
        raise HTTPException(status_code=404, detail="Movie not found")
    return movie

@router.get("/movies/", response_model=schemas.PaginatedMovieResponse)
def read_movies(
    db: Session = Depends(database.get_db),
    limit: int = Query(10, alias="limit", ge=1, le=100),
    offset: int = Query(0, alias="offset", ge=0)
):
    return crud.get_movies(db, limit=limit, offset=offset)

@router.delete("/movies/{movie_id}")
def delete_movie(movie_id: str, db: Session = Depends(database.get_db)):
    return crud.delete_movie(db, movie_id)

@router.get("/movies/search/", response_model=schemas.PaginatedMovieResponse)
def search_movies(
    db: Session = Depends(database.get_db),
    title_query: Optional[str] = None,
    tag_ids: Optional[List[str]] = Query(None),
    sort_by: Optional[str] = Query(None, regex="^(title|rating|releaseYear)$"),
    sort_order: Optional[str] = Query("asc", regex="^(asc|desc)$"),
    limit: int = Query(10, ge=1, le=100),
    offset: int = Query(0, ge=0)
):
    return crud.search_movies(db, title_query, tag_ids, limit=limit, offset=offset, sort_by=sort_by, sort_order=sort_order)