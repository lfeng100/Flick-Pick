from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import schemas, database, crud
from typing import Dict

router = APIRouter()

@router.post("/userwatchlist/")
def add_user_watchlist(watchlist: schemas.UserWatchlistCreate, db: Session = Depends(database.get_db)):
    return crud.add_user_watchlist(db, watchlist)

@router.get("/userwatchlist/{user_id}", response_model=schemas.PaginatedUserWatchlistResponse)
def get_user_watchlist(user_id: str, db: Session = Depends(database.get_db), limit: int = 10, offset: int = 0):
    """Retrieve all movies in a user's watchlist."""
    result = crud.get_user_watchlist(db, user_id, limit, offset)
    return result

@router.delete("/userwatchlist/{user_id}/{movie_id}")
def delete_user_watchlist(user_id: str, movie_id: str, db: Session = Depends(database.get_db)):
    deleted_entry = crud.delete_user_watchlist(db, user_id, movie_id)
    if not deleted_entry:
        raise HTTPException(status_code=404, detail="User watchlist entry not found")
    return deleted_entry
