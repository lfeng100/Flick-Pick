from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/groups/")
def create_group(group: schemas.GroupCreate, db: Session = Depends(database.get_db)):
    return crud.create_group(db, group)

@router.get("/groups/")
def read_groups(
    db: Session = Depends(database.get_db),
    limit: int = Query(10, alias="limit", ge=1, le=100),
    offset: int = Query(0, alias="offset", ge=0)
):
    return crud.get_groups(db, limit=limit, offset=offset)

@router.get("/groups/{group_id}")
def read_group(group_id: str, db: Session = Depends(database.get_db)):
    group = crud.get_group_by_id(db, group_id)
    if not group:
        raise HTTPException(status_code=404, detail="Group not found")
    return group

@router.get("/groups/search/")
def search_groups(query: str, db: Session = Depends(database.get_db)):
    return crud.search_groups(db, query)

@router.delete("/groups/{group_id}")
def delete_group(group_id: str, db: Session = Depends(database.get_db)):
    deleted_group = crud.delete_group(db, group_id)
    if not deleted_group:
        raise HTTPException(status_code=404, detail="Group not found")
    return deleted_group
