from fastapi import APIRouter, Depends, Query
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/users/")
def create_user(user: schemas.UserCreate, db: Session = Depends(database.get_db)):
    return crud.create_user(db, user)

@router.get("/users/")
def read_users(
    db: Session = Depends(database.get_db),
    limit: int = Query(10, alias="limit", ge=1, le=100),  # Page size (1-100)
    offset: int = Query(0, alias="offset", ge=0)  # Start index
):
    return crud.get_users(db, limit=limit, offset=offset)

@router.get("/users/{user_id}")
def read_user(user_id: str, db: Session = Depends(database.get_db)):
    return crud.get_user(db, user_id)

@router.delete("/users/{user_id}")
def delete_user(user_id: str, db: Session = Depends(database.get_db)):
    return crud.delete_user(db, user_id)

@router.put("/users/{user_id}")
def update_user(user_id: str, user_update: schemas.UserCreate, db: Session = Depends(database.get_db)):
    updated_user = crud.update_user(db, user_id, user_update)
    if updated_user is None:
        raise HTTPException(status_code=404, detail="User not found")
    return updated_user

@router.get("/users/search/", response_model=schemas.PaginatedUsersResponse)
def search_users(
    username_query: str = Query(..., description="Search query for usernames"),
    limit: int = Query(10, description="Number of results per page"),
    offset: int = Query(0, description="Pagination offset"),
    db: Session = Depends(database.get_db)
):
    return crud.search_users(db, username_query, limit, offset)
