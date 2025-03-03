from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/preferences/")
def add_user_preference(preference: schemas.PreferenceCreate, db: Session = Depends(database.get_db)):
    return crud.add_user_preference(db, preference)

@router.delete("/preferences/{user_id}/{tag_id}")
def remove_user_preference(user_id: str, tag_id: str, db: Session = Depends(database.get_db)):
    deleted_preference = crud.remove_user_preference(db, user_id, tag_id)
    if not deleted_preference:
        raise HTTPException(status_code=404, detail="User preference not found")
    return deleted_preference

@router.get("/preferences/{user_id}")
def get_tags_for_user(user_id: str, db: Session = Depends(database.get_db)):
    return crud.get_tags_for_user(db, user_id)
