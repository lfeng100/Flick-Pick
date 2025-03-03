from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/movietags/")
def add_movie_tag(movie_tag: schemas.MovieTagCreate, db: Session = Depends(database.get_db)):
    return crud.add_movie_tag(db, movie_tag)

@router.delete("/movietags/{movie_id}/{tag_id}")
def remove_movie_tag(movie_id: str, tag_id: str, db: Session = Depends(database.get_db)):
    deleted_movie_tag = crud.remove_movie_tag(db, movie_id, tag_id)
    if not deleted_movie_tag:
        raise HTTPException(status_code=404, detail="Movie tag relationship not found")
    return deleted_movie_tag

@router.get("/movietags/{movie_id}")
def get_tags_for_movie(movie_id: str, db: Session = Depends(database.get_db)):
    return crud.get_tags_for_movie(db, movie_id)
