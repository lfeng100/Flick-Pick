from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/groups/")
def create_group(group: schemas.GroupCreate, db: Session = Depends(database.get_db)):
    return crud.create_group(db, group)

@router.get("/groups/")
def read_groups(db: Session = Depends(database.get_db)):
    return crud.get_groups(db)

@router.get("/groups/search/")
def search_groups(query: str, db: Session = Depends(database.get_db)):
    return crud.search_groups(db, query)

@router.delete("/groups/{group_id}")
def delete_group(group_id: str, db: Session = Depends(database.get_db)):
    deleted_group = crud.delete_group(db, group_id)
    if not deleted_group:
        raise HTTPException(status_code=404, detail="Group not found")
    return deleted_group
