from sqlalchemy import Column, String, Integer, Float, ForeignKey, TIMESTAMP, Text, Enum
from sqlalchemy.orm import relationship
from sqlalchemy.dialects.mysql import JSON
from database import Base
import uuid

class User(Base):
    __tablename__ = "Users"
    userID = Column(String(255), primary_key=True)
    email = Column(String(255), unique=True, nullable=False)
    username = Column(String(255), unique=True, nullable=False)
    firstName = Column(String(100), nullable=False)
    lastName = Column(String(100), nullable=False)

class Movie(Base):
    __tablename__ = "Movies"
    movieID = Column(String(255), primary_key=True)
    title = Column(String(255), nullable=False)
    releaseYear = Column(Integer, nullable=False)
    genres = Column(JSON, nullable=False)
    original_language = Column(String(10))
    rating = Column(Float)
    description = Column(Text)
    tmdb_id = Column(String(36))
    runtime = Column(Integer)
    poster_path = Column(Text)

class Group(Base):
    __tablename__ = "Groups"
    groupID = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    groupName = Column(String(255), nullable=False)
    adminUserID = Column(String(36), ForeignKey("Users.userID", ondelete="SET NULL"))
    
class Tag(Base):
    __tablename__ = "Tags"
    tagID = Column(String(36), primary_key=True)
    tagName = Column(String(255), unique=True, nullable=False)
    tagType = Column(Enum('genre', 'language', 'year'), nullable=False)

class GroupUser(Base):
    __tablename__ = "GroupUsers"
    groupID = Column(String(36), ForeignKey("Groups.groupID", ondelete="CASCADE"), primary_key=True)
    userID = Column(String(36), ForeignKey("Users.userID", ondelete="CASCADE"), primary_key=True)
    joinedAt = Column(TIMESTAMP, server_default="CURRENT_TIMESTAMP")

class MovieTag(Base):
    __tablename__ = "MovieTags"
    movieID = Column(String(255), ForeignKey("Movies.movieID", ondelete="CASCADE"), primary_key=True)
    tagID = Column(String(36), ForeignKey("Tags.tagID", ondelete="CASCADE"), primary_key=True)

class Preference(Base):
    __tablename__ = "Preferences"
    userID = Column(String(36), ForeignKey("Users.userID", ondelete="CASCADE"), primary_key=True)
    tagID = Column(String(36), ForeignKey("Tags.tagID", ondelete="CASCADE"), primary_key=True)

class Review(Base):
    __tablename__ = "Reviews"
    reviewID = Column(String(36), primary_key=True, default=lambda: str(uuid.uuid4()))
    rating = Column(Float, nullable=False)
    message = Column(String(255))
    timestamp = Column(TIMESTAMP, server_default="CURRENT_TIMESTAMP")
    userID = Column(String(36), ForeignKey("Users.userID", ondelete="CASCADE"))
    movieID = Column(String(255), ForeignKey("Movies.movieID", ondelete="CASCADE"))

class UserWatched(Base):
    __tablename__ = "UserWatched"
    userID = Column(String(36), ForeignKey("Users.userID", ondelete="CASCADE"), primary_key=True)
    movieID = Column(String(255), ForeignKey("Movies.movieID", ondelete="CASCADE"), primary_key=True)

class UserWatchlist(Base):
    __tablename__ = "UserWatchlist"
    userID = Column(String(36), ForeignKey("Users.userID", ondelete="CASCADE"), primary_key=True)
    movieID = Column(String(255), ForeignKey("Movies.movieID", ondelete="CASCADE"), primary_key=True)
