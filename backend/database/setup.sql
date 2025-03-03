-- Create Database
CREATE DATABASE IF NOT EXISTS flick_pick;
USE flick_pick;

-- Create Users Table
CREATE TABLE IF NOT EXISTS Users (
    userID VARCHAR(36) PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    username VARCHAR(255) UNIQUE NOT NULL
);

-- Create Movies Table
CREATE TABLE IF NOT EXISTS Movies (
    movieID VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    releaseYear INT NOT NULL,
    genres JSON NOT NULL, -- JSON array
    original_language VARCHAR(10),
    rating FLOAT,
    description TEXT,
    tmdb_id VARCHAR(36),
    runtime INT,
    poster_path TEXT
);

-- Create Groups Table
CREATE TABLE IF NOT EXISTS `Groups` (
    groupID VARCHAR(36) PRIMARY KEY,
    groupName VARCHAR(255) NOT NULL,
    adminUserID VARCHAR(36),
    FOREIGN KEY (adminUserID) REFERENCES Users(userID) ON DELETE SET NULL
);

-- Create GroupUsers Table
CREATE TABLE IF NOT EXISTS GroupUsers (
    groupID VARCHAR(36),
    userID VARCHAR(36),
    joinedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (groupID, userID),
    FOREIGN KEY (groupID) REFERENCES `Groups`(groupID) ON DELETE CASCADE,
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE
);

-- Create Tags Table
CREATE TABLE IF NOT EXISTS Tags (
    tagID VARCHAR(36) PRIMARY KEY,
    tagName VARCHAR(255) UNIQUE NOT NULL,
    tagType ENUM('genre', 'language', 'year') NOT NULL
);

-- Create MovieTags Table
CREATE TABLE IF NOT EXISTS MovieTags (
    movieID VARCHAR(255),
    tagID VARCHAR(36),
    PRIMARY KEY (movieID, tagID),
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE,
    FOREIGN KEY (tagID) REFERENCES Tags(tagID) ON DELETE CASCADE
);

-- Create Preferences Table
CREATE TABLE IF NOT EXISTS Preferences (
    userID VARCHAR(36),
    tagID VARCHAR(36),
    PRIMARY KEY (userID, tagID),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (tagID) REFERENCES Tags(tagID) ON DELETE CASCADE
);

-- Create Reviews Table
CREATE TABLE IF NOT EXISTS Reviews (
    reviewID VARCHAR(36) PRIMARY KEY,
    rating FLOAT NOT NULL,
    message VARCHAR(255),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    userID VARCHAR(36),
    movieID VARCHAR(255),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE
);

-- Create UserWatched Table
CREATE TABLE IF NOT EXISTS UserWatched (
    userID VARCHAR(36),
    movieID VARCHAR(255),
    PRIMARY KEY (userID, movieID),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE
);

-- Create UserWatchlist Table
CREATE TABLE IF NOT EXISTS UserWatchlist (
    userID VARCHAR(36),
    movieID VARCHAR(255),
    PRIMARY KEY (userID, movieID),
    FOREIGN KEY (userID) REFERENCES Users(userID) ON DELETE CASCADE,
    FOREIGN KEY (movieID) REFERENCES Movies(movieID) ON DELETE CASCADE
);
