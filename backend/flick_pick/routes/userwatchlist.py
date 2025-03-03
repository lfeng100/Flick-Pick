from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/userwatchlist/")
def add_user_watchlist(watchlist: schemas.UserWatchlistCreate, db: Session = Depends(database.get_db)):
    return crud.add_user_watchlist(db, watchlist)

@router.delete("/userwatchlist/{user_id}/{movie_id}")
def delete_user_watchlist(user_id: str, movie_id: str, db: Session = Depends(database.get_db)):
    deleted_entry = crud.delete_user_watchlist(db, user_id, movie_id)
    if not deleted_entry:
        raise HTTPException(status_code=404, detail="User watchlist entry not found")
    return deleted_entry
