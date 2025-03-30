from fastapi import APIRouter, Depends, Query, HTTPException
from sqlalchemy.orm import Session
import schemas, database, crud

router = APIRouter()

@router.post("/reviews/")
def create_review(review: schemas.ReviewCreate, db: Session = Depends(database.get_db)):
    return crud.create_review(db, review)

@router.get("/reviews/")
def read_reviews(db: Session = Depends(database.get_db)):
    return crud.get_reviews(db)

@router.put("/reviews/{review_id}")
def update_review(review_id: str, review_update: schemas.ReviewCreate, db: Session = Depends(database.get_db)):
    updated_review = crud.update_review(db, review_id, review_update)
    if updated_review is None:
        raise HTTPException(status_code=404, detail="Review not found")
    return updated_review

@router.delete("/reviews/{review_id}")
def delete_review(review_id: str, db: Session = Depends(database.get_db)):
    deleted_review = crud.delete_review(db, review_id)
    if not deleted_review:
        raise HTTPException(status_code=404, detail="Review not found")
    return deleted_review

@router.get("/reviews/user/{user_id}", response_model=schemas.PaginatedReviewsWithMovies)
def get_reviews_by_user(
    user_id: str,
    db: Session = Depends(database.get_db),
    limit: int = Query(10, ge=1, le=100),
    offset: int = Query(0, ge=0)
):
    return crud.get_reviews_by_user(db, user_id, limit=limit, offset=offset)

@router.get("/reviews/movie/{movie_id}")
def get_reviews_by_movie(
    movie_id: str,
    db: Session = Depends(database.get_db),
    limit: int = Query(10, ge=1, le=100),
    offset: int = Query(0, ge=0)
):
    return crud.get_reviews_by_movie(db, movie_id, limit=limit, offset=offset)
