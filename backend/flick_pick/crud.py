from sqlalchemy.orm import Session
from sqlalchemy.sql import func
import models, schemas
import json
import uuid

# --- USERS CRUD ---
def create_user(db: Session, user: schemas.UserCreate):
    user_dict = user.dict()
    user_dict["userID"] = user_dict["userID"] or str(uuid.uuid4())

    db_user = models.User(**user_dict)
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

def get_users(db: Session, limit: int = 10, offset: int = 0):
    total = db.query(models.User).count()
    users = db.query(models.User).offset(offset).limit(limit).all()
    return {
        "items": users,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def get_user(db: Session, user_id: str):
    return db.query(models.User).filter(models.User.userID == user_id).first()

def delete_user(db: Session, user_id: str):
    user = db.query(models.User).filter(models.User.userID == user_id).first()
    if user:
        db.delete(user)
        db.commit()
    return user

def update_user(db: Session, user_id: str, user_update: schemas.UserCreate):
    db_user = db.query(models.User).filter(models.User.userID == user_id).first()
    if not db_user:
        return None
    db_user.email = user_update.email
    db_user.username = user_update.username
    db.commit()
    db.refresh(db_user)
    return db_user

def search_users(db: Session, username_query: str, limit: int = 10, offset: int = 0):
    query = db.query(models.User).filter(models.User.username.ilike(f"%{username_query}%"))

    total = query.count()
    users = query.offset(offset).limit(limit).all()

    return {
        "items": users,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

# --- MOVIES CRUD ---
def create_movie(db: Session, movie: schemas.MovieCreate):
    # Ensure genres is stored as JSON format
    movie_dict = movie.dict()
    movie_dict["genres"] = json.dumps(movie.genres)  # Convert list to JSON string
    db_movie = models.Movie(**movie_dict)
    db.add(db_movie)
    db.commit()
    db.refresh(db_movie)
    return db_movie

def get_movies(db: Session, limit: int = 10, offset: int = 0):
    total = db.query(models.Movie).count()
    movies = db.query(models.Movie).offset(offset).limit(limit).all()
    return {
        "items": movies,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def get_movie(db: Session, movie_id: str):
    movie = db.query(models.Movie).filter(models.Movie.movieID == movie_id).first()
    if movie:
        if isinstance(movie.genres, str):
            movie.genres = json.loads(movie.genres)

    return movie

def update_movie(db: Session, movie_id: str, movie_update: schemas.MovieCreate):
    db_movie = db.query(models.Movie).filter(models.Movie.movieID == movie_id).first()
    if not db_movie:
        return None

    db_movie.title = movie_update.title
    db_movie.releaseYear = movie_update.releaseYear
    db_movie.genres = json.dumps(movie_update.genres)  # Convert genres to JSON
    db_movie.rating = movie_update.rating
    db_movie.description = movie_update.description
    db_movie.tmdb_id = movie_update.tmdb_id
    db_movie.runtime = movie_update.runtime
    db_movie.poster_path = movie_update.poster_path

    db.commit()
    db.refresh(db_movie)
    return db_movie

def delete_movie(db: Session, movie_id: str):
    movie = db.query(models.Movie).filter(models.Movie.movieID == movie_id).first()
    if movie:
        db.delete(movie)
        db.commit()
    return movie

# --- GROUPS CRUD ---
def create_group(db: Session, group: schemas.GroupCreate):
    db_group = models.Group(**group.dict())
    db.add(db_group)
    db.commit()
    db.refresh(db_group)
    return db_group

def get_groups(db: Session, limit: int = 10, offset: int = 0):
    total = db.query(models.Group).count()  # Total groups count
    groups = db.query(models.Group).offset(offset).limit(limit).all()
    return {
        "items": groups,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def get_group_by_id(db: Session, group_id: str):
    """Retrieve a single group by its groupID, including group size and admin username."""
    group = db.query(models.Group).filter(models.Group.groupID == group_id).first()
    if not group:
        return None

    # Count members in the group
    group_size = db.query(models.GroupUser).filter(models.GroupUser.groupID == group_id).count()

    # Fetch admin's username
    admin_user = db.query(models.User.username).filter(models.User.userID == group.adminUserID).first()
    admin_username = admin_user.username if admin_user else None

    return {
        "groupID": group.groupID,
        "groupName": group.groupName,
        "adminUserID": group.adminUserID,
        "adminUsername": admin_username,
        "groupSize": group_size
    }

def search_groups(db: Session, query: str, limit: int = 10, offset: int = 0):
    search_query = db.query(models.Group).filter(models.Group.groupName.ilike(f"%{query}%"))

    total = search_query.count()
    groups = search_query.offset(offset).limit(limit).all()

    return {
        "items": groups,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def delete_group(db: Session, group_id: str):
    group = db.query(models.Group).filter(models.Group.groupID == group_id).first()
    if group:
        db.delete(group)
        db.commit()
    return group

# --- GROUP USERS CRUD ---
def add_user_to_group(db: Session, group_user: schemas.GroupUserCreate):
    db_group_user = models.GroupUser(**group_user.dict())
    db.add(db_group_user)
    db.commit()
    return db_group_user

def remove_user_from_group(db: Session, group_id: str, user_id: str):
    db_group_user = db.query(models.GroupUser).filter(
        models.GroupUser.groupID == group_id,
        models.GroupUser.userID == user_id
    ).first()
    if db_group_user:
        db.delete(db_group_user)
        db.commit()
    return db_group_user

def get_users_in_group(db: Session, group_id: str, limit: int = 10, offset: int = 0):
    query = db.query(models.User).join(models.GroupUser).filter(models.GroupUser.groupID == group_id)

    total = query.count()
    users = query.offset(offset).limit(limit).all()

    return {
        "items": users,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

# --- TAGS CRUD ---
def create_tag(db: Session, tag: schemas.TagCreate):
    db_tag = models.Tag(**tag.dict())
    db.add(db_tag)
    db.commit()
    return db_tag

def get_tags(db: Session):
    return db.query(models.Tag).all()

# --- MOVIE TAGS CRUD ---
def add_movie_tag(db: Session, movie_tag: schemas.MovieTagCreate):
    db_movie_tag = models.MovieTag(**movie_tag.dict())
    db.add(db_movie_tag)
    db.commit()
    return db_movie_tag

def remove_movie_tag(db: Session, movie_id: str, tag_id: str):
    db_movie_tag = db.query(models.MovieTag).filter(
        models.MovieTag.movieID == movie_id,
        models.MovieTag.tagID == tag_id
    ).first()
    if db_movie_tag:
        db.delete(db_movie_tag)
        db.commit()
    return db_movie_tag

def get_tags_for_movie(db: Session, movie_id: str):
    return db.query(models.Tag).join(models.MovieTag).filter(models.MovieTag.movieID == movie_id).all()

# --- PREFERENCES CRUD ---
def add_user_preference(db: Session, preference: schemas.PreferenceCreate):
    db_preference = models.Preference(**preference.dict())
    db.add(db_preference)
    db.commit()
    return db_preference

def remove_user_preference(db: Session, user_id: str, tag_id: str):
    db_preference = db.query(models.Preference).filter(
        models.Preference.userID == user_id,
        models.Preference.tagID == tag_id
    ).first()
    if db_preference:
        db.delete(db_preference)
        db.commit()
    return db_preference

def get_tags_for_user(db: Session, user_id: str):
    return db.query(models.Tag).join(models.Preference).filter(models.Preference.userID == user_id).all()

# --- REVIEWS CRUD ---
def create_review(db: Session, review: schemas.ReviewCreate):
    db_review = models.Review(**review.dict())
    db.add(db_review)
    db.commit()
    db.refresh(db_review) # Need this or the json is empty when returned
    return db_review

def get_reviews(db: Session):
    return db.query(models.Review).all()

def delete_review(db: Session, review_id: str):
    review = db.query(models.Review).filter(models.Review.reviewID == review_id).first()
    if review:
        db.delete(review)
        db.commit()
    return review

def get_reviews_by_user(db: Session, user_id: str, limit: int = 10, offset: int = 0):
    query = db.query(models.Review).filter(models.Review.userID == user_id)

    total = query.count()
    reviews = query.offset(offset).limit(limit).all()

    return {
        "items": reviews,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def get_reviews_by_movie(db: Session, movie_id: str, limit: int = 10, offset: int = 0):
    query = db.query(models.Review).filter(models.Review.movieID == movie_id)

    total = query.count()
    reviews = query.offset(offset).limit(limit).all()

    return {
        "items": reviews,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def update_review(db: Session, review_id: str, review_update: schemas.ReviewCreate):
    db_review = db.query(models.Review).filter(models.Review.reviewID == review_id).first()
    if not db_review:
        return None
    db_review.rating = review_update.rating
    db_review.message = review_update.message
    db.commit()
    db.refresh(db_review)
    return db_review

# --- USER WATCHED CRUD ---
def add_user_watched(db: Session, watched: schemas.UserWatchedCreate):
    db_watched = models.UserWatched(**watched.dict())
    db.add(db_watched)
    db.commit()
    return db_watched

def get_user_watched(db: Session, user_id: str, limit: int = 10, offset: int = 0):
    """Retrieve all movies a user has watched."""
    query = db.query(models.Movie).join(models.UserWatched).filter(models.UserWatched.userID == user_id)

    total = query.count()
    movies = query.offset(offset).limit(limit).all()

    movie_responses = [schemas.MovieResponse.from_orm(movie) for movie in movies]

    return {
        "items": movie_responses,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def delete_user_watched(db: Session, user_id: str, movie_id: str):
    watched = db.query(models.UserWatched).filter(
        models.UserWatched.userID == user_id, models.UserWatched.movieID == movie_id
    ).first()
    if watched:
        db.delete(watched)
        db.commit()
    return watched

# --- USER WATCHLIST CRUD ---
def add_user_watchlist(db: Session, watchlist: schemas.UserWatchlistCreate):
    db_watchlist = models.UserWatchlist(**watchlist.dict())
    db.add(db_watchlist)
    db.commit()
    return db_watchlist

def get_user_watchlist(db: Session, user_id: str, limit: int = 10, offset: int = 0):
    """Retrieve all movies in a user's watchlist."""
    query = db.query(models.Movie).join(models.UserWatchlist).filter(models.UserWatchlist.userID == user_id)

    total = query.count()
    movies = query.offset(offset).limit(limit).all()

    movie_responses = [schemas.MovieResponse.from_orm(movie) for movie in movies]

    return {
        "items": movie_responses,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }

def delete_user_watchlist(db: Session, user_id: str, movie_id: str):
    watchlist = db.query(models.UserWatchlist).filter(
        models.UserWatchlist.userID == user_id, models.UserWatchlist.movieID == movie_id
    ).first()
    if watchlist:
        db.delete(watchlist)
        db.commit()
    return watchlist

def search_movies(db: Session, title_query: str = None, tag_ids: list = None, limit: int = 10, offset: int = 0):
    query = db.query(models.Movie)

    # Filter by title if provided
    if title_query:
        query = query.filter(models.Movie.title.ilike(f"%{title_query}%"))

    # Filter by tags if provided
    if tag_ids and isinstance(tag_ids, list) and len(tag_ids) > 0:
        # Count number of matching tags per movie
        subquery = (
            db.query(models.MovieTag.movieID)
            .filter(models.MovieTag.tagID.in_(tag_ids))
            .group_by(models.MovieTag.movieID)
            .having(func.count(models.MovieTag.tagID) == len(tag_ids))  # Ensure all tags are matched
            .subquery()
        )
        query = query.filter(models.Movie.movieID.in_(subquery))

    total = query.count()  # Count total results before applying limit/offset
    movies = query.offset(offset).limit(limit).all()

    return {
        "items": movies,
        "total": total,
        "page": (offset // limit) + 1,
        "pages": (total + limit - 1) // limit
    }