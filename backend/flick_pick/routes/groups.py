from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/groups/")
def create_group(group: schemas.GroupCreate, db: Session = Depends(database.get_db)):
    return crud.create_group(db, group)

@router.get("/groups/", response_model=schemas.PaginatedGroupsResponse)
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

@router.get("/groups/search/", response_model=schemas.PaginatedGroupsResponse)
def search_groups(
    query: str = Query(..., description="Search query for group names"),
    limit: int = Query(10, description="Number of results per page", ge=1, le=100),
    offset: int = Query(0, description="Pagination offset", ge=0),
    db: Session = Depends(database.get_db)
):
    return crud.search_groups(db, query, limit, offset)

@router.delete("/groups/{group_id}")
def delete_group(group_id: str, db: Session = Depends(database.get_db)):
    deleted_group = crud.delete_group(db, group_id)
    if not deleted_group:
        raise HTTPException(status_code=404, detail="Group not found")
    return deleted_group

@router.get("/groups/{group_id}/activity")
def get_group_activity(group_id: str, db: Session = Depends(database.get_db)):
    result = crud.get_group_activity(db, group_id)
    if not result["activity"]:
        raise HTTPException(status_code=404, detail="No activity found for this group")
    return result
