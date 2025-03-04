from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/groupusers/")
def add_user_to_group(group_user: schemas.GroupUserCreate, db: Session = Depends(database.get_db)):
    return crud.add_user_to_group(db, group_user)

@router.delete("/groupusers/{group_id}/{user_id}")
def remove_user_from_group(group_id: str, user_id: str, db: Session = Depends(database.get_db)):
    deleted_group_user = crud.remove_user_from_group(db, group_id, user_id)
    if not deleted_group_user:
        raise HTTPException(status_code=404, detail="Group user relationship not found")
    return deleted_group_user

@router.get("/groupusers/{group_id}")
def get_users_in_group(
    group_id: str,
    db: Session = Depends(database.get_db),
    limit: int = Query(10, ge=1, le=100),
    offset: int = Query(0, ge=0)
):
    return crud.get_users_in_group(db, group_id, limit=limit, offset=offset)
