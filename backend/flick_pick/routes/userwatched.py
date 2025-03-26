from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import schemas, database, crud
from typing import Dict

router = APIRouter()

@router.post("/userwatched/")
def add_user_watched(watched: schemas.UserWatchedCreate, db: Session = Depends(database.get_db)):
    return crud.add_user_watched(db, watched)

@router.get("/userwatched/{user_id}", response_model=schemas.PaginatedUserWatchedResponse)
def get_user_watched(user_id: str, db: Session = Depends(database.get_db), limit: int = 10, offset: int = 0):
    """Retrieve all movies a user has watched."""
    result = crud.get_user_watched(db, user_id, limit, offset)
    return result

@router.delete("/userwatched/{user_id}/{movie_id}")
def delete_user_watched(user_id: str, movie_id: str, db: Session = Depends(database.get_db)):
    deleted_entry = crud.delete_user_watched(db, user_id, movie_id)
    if not deleted_entry:
        raise HTTPException(status_code=404, detail="User watched entry not found")
    return deleted_entry
