from pydantic import BaseModel
from typing import Optional, List, Literal
from datetime import datetime

# --- USERS SCHEMA ---
class UserBase(BaseModel):
    email: str
    username: str

class UserCreate(UserBase):
    userID: str  # Primary key, required when creating a user

class UserResponse(UserBase):
    userID: str

    class Config:
        orm_mode = True

# --- MOVIES SCHEMA ---
class MovieBase(BaseModel):
    title: str
    releaseYear: int
    genres: List[str]  # JSON array stored as a list
    original_language: Optional[str]
    rating: Optional[float]
    description: Optional[str]
    tmdb_id: Optional[str]
    runtime: Optional[int]
    poster_path: Optional[str]

class MovieCreate(MovieBase):
    movieID: str  # Primary key, required when creating a movie

class MovieResponse(MovieBase):
    movieID: str

    class Config:
        orm_mode = True

# --- GROUPS SCHEMA ---
class GroupBase(BaseModel):
    groupName: str
    adminUserID: Optional[str]

class GroupCreate(GroupBase):
    groupID: str  # Primary key

class GroupResponse(GroupBase):
    groupID: str

    class Config:
        orm_mode = True

# --- GROUP USERS SCHEMA ---
class GroupUserBase(BaseModel):
    groupID: str
    userID: str

class GroupUserCreate(GroupUserBase):
    pass

class GroupUserResponse(GroupUserBase):
    joinedAt: datetime

    class Config:
        orm_mode = True

# --- TAGS SCHEMA ---
class TagBase(BaseModel):
    tagName: str
    tagType: Literal["genre", "language", "year"]

class TagCreate(TagBase):
    tagID: str  # Primary key

class TagResponse(TagBase):
    tagID: str

    class Config:
        orm_mode = True

# --- MOVIE TAGS SCHEMA ---
class MovieTagBase(BaseModel):
    movieID: str
    tagID: str

class MovieTagCreate(MovieTagBase):
    pass

class MovieTagResponse(MovieTagBase):
    class Config:
        orm_mode = True

# --- PREFERENCES SCHEMA ---
class PreferenceBase(BaseModel):
    userID: str
    tagID: str

class PreferenceCreate(PreferenceBase):
    pass

class PreferenceResponse(PreferenceBase):
    class Config:
        orm_mode = True

# --- REVIEWS SCHEMA ---
class ReviewBase(BaseModel):
    rating: float
    message: Optional[str]

class ReviewCreate(ReviewBase):
    userID: str
    movieID: str

class ReviewResponse(ReviewBase):
    reviewID: str
    timestamp: datetime
    userID: str
    movieID: str

    class Config:
        orm_mode = True

# --- USER WATCHED SCHEMA ---
class UserWatchedBase(BaseModel):
    userID: str
    movieID: str

class UserWatchedCreate(UserWatchedBase):
    pass

class UserWatchedResponse(UserWatchedBase):
    class Config:
        orm_mode = True

# --- USER WATCHLIST SCHEMA ---
class UserWatchlistBase(BaseModel):
    userID: str
    movieID: str

class UserWatchlistCreate(UserWatchlistBase):
    pass

class UserWatchlistResponse(UserWatchlistBase):
    class Config:
        orm_mode = True
