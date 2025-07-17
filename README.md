# Team 19: United

## Members

| Name               | Quest ID   | Student # | Github User   |
|--------------------|------------|-----------|---------------|
| Leo Feng           | l47feng    | 20886652  | lfeng100      |
| Sajeen Selvakamalan| sselvaka   | 20908482  | sselvaka      |
| Aryan Ved          | aved       | 20905476  | aryanxved     |
| Alex Yoshida       | a3yoshid   | 20905834  | Alex-Yosh     |
| Ashvin Grewal      | a37grewa   | 20851001  | ag3722        |
| Jerry Wu           | j553wu     | 20883021  | jwooser       |

# 🎬 FlickPick

FlickPick is a social movie recommendation platform where users can review, track, and discuss movies with friends. It supports group-based viewing, personalized preferences, watchlists, and real-time updates.

---

## 📌 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Database Schema](#database-schema)
- [API Endpoints](#api-endpoints)
- [Seed Data](#seed-data)
- [Project Structure](#project-structure)
- [Future Work](#future-work)

---

## ✅ Features

- 🔐 User Authentication (via Firebase)
- 👤 User Profile Management
- 🧠 Personalized Movie Preferences (by tags/genres)
- 📝 Movie Reviews with Rating, Message, Timestamp
- 🎞️ Watchlist & Watch History
- 👥 Group Management (create/join/leave)
- 🧵 Group Timeline with shared reviews/watch activity
- 🔍 Advanced Search & Sorting (by rating, title, release year)
- ⚡ FastAPI backend with SQLAlchemy and Pydantic

---

## 💻 Tech Stack

| Layer          | Technology                           |
|----------------|---------------------------------------|
| Backend        | Python, FastAPI, SQLAlchemy, MySQL    |
| Frontend       | Kotlin, Jetpack Compose               |
| Auth           | Firebase Authentication               |
| DB             | MySQL (with relational constraints)   |
| ORM            | SQLAlchemy ORM                        |
| Testing        | Pytest (planned)                      |
| Deployment     | Docker (planned), Railway/Vercel (optional) |

---

## 🚀 Getting Started

### 🔧 Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/flickpick.git
   cd flickpick/backend
   ```

2. **Install dependencies**
   ```bash
   pip install -r requirements.txt
   ```

3. **Setup MySQL database**
   ```bash
   mysql -u root -p
   SOURCE schema.sql;
   SOURCE seed.sql;
   ```

4. **Run the backend server**
   ```bash
   uvicorn main:app --reload
   ```

5. Visit: [http://localhost:8000/docs](http://localhost:8000/docs) for interactive Swagger UI.

---

## 🗃️ Database Schema (Simplified)

- **Users** (`userID`, `email`, `username`, `firstName`, `lastName`)
- **Movies** (`movieID`, `title`, `releaseYear`, `rating`, `posterPath`, etc.)
- **Reviews** (`reviewID`, `userID`, `movieID`, `rating`, `message`, `timestamp`)
- **Preferences** (`userID`, `tagID`)
- **UserWatchlist** (`userID`, `movieID`, `timestamp`)
- **UserWatched** (`userID`, `movieID`, `timestamp`)
- **Groups** (`groupID`, `groupName`, `adminUserID`)
- **GroupUsers** (`groupID`, `userID`, `joinedAt`)

---

## 📡 API Endpoints

All endpoints are available under `/docs` using Swagger UI.

### 🔑 User

- `POST /users/` – Create user (optional custom userID)
- `GET /users/{user_id}` – Get user info
- `PUT /users/{user_id}` – Update user info (first/last name only)

### 🎞️ Movies

- `GET /movies/` – List all movies
- `GET /movies/search/` – Search movies with filters:
  - `query` (title substring)
  - `tag_ids` (must match all tags)
  - `sort_by`: 
    - `title_asc` | `title_desc`  
    - `rating_high` | `rating_low`  
    - `year_asc` | `year_desc`

### 📝 Reviews

- `POST /reviews/` – Add a review  
  (Auto-adds to watched list. Enforces 1 review per user/movie)
- `GET /reviews/user/{user_id}` – Get all reviews by user (with movie info)
- `GET /group/activity/{group_id}` – Combined timeline of reviews, watchlist, watched per group

### 📺 Watchlist & Watched

- `POST /watchlist/` – Add to watchlist
- `POST /watched/` – Mark as watched
- `GET /userwatchlist/{user_id}` – Get user's watchlist (timestamp + movie details)
- `GET /userwatched/{user_id}` – Get user's watched (timestamp + movie details)

### 👥 Groups

- `POST /groups/` – Create group (must have unique and non-empty name)
- `POST /groups/{group_id}/join` – Join group
- `GET /groups/{group_id}` – Get group info (includes group size and admin username)
- `GET /groups/search/` – Search groups (supports pagination)
- `GET /group/activity/{group_id}` – View all reviews, watched, and watchlist activity for all users in a group

---

## 🌱 Seed Data

The `seed.sql` script includes:

- 20 mock users with preferences
- 50+ reviews across users and movies
- Randomized watchlist and watched history
- 5 groups and group memberships

To reset and reseed:

```bash
mysql -u root -p flick_pick < schema.sql
mysql -u root -p flick_pick < seed.sql
```

---

## 📁 Project Structure

```
flickpick/
├── backend/
│   ├── main.py              # FastAPI entrypoint
│   ├── models.py            # SQLAlchemy models
│   ├── schemas.py           # Pydantic request/response schemas
│   ├── crud.py              # DB logic functions
│   ├── routes/              # FastAPI routers by module
│   ├── database.py          # DB connection setup
│   ├── schema.sql           # DB schema
│   ├── seed.sql             # Mock data
│   └── requirements.txt
└── frontend/
    └── ... (Kotlin Jetpack Compose UI files)
```

---

## 🛠 Future Work

- ✅ Firebase Authentication integration
- ✅ ML Recommendation engine
- ⬜ Docker support for backend + MySQL
- ⬜ Full test suite with Pytest
- ⬜ Movie popularity/trending pipeline
- ⬜ Group messaging & shared viewing experience

---

## 🤝 Contributing

Pull requests and issues are welcome! Please open a discussion for major features.

---

## 📄 License

MIT License © 2025

