from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/tags/")
def create_tag(tag: schemas.TagCreate, db: Session = Depends(database.get_db)):
    return crud.create_tag(db, tag)

@router.get("/tags/")
def read_tags(db: Session = Depends(database.get_db)):
    return crud.get_tags(db)
