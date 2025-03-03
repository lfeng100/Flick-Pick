from fastapi import FastAPI
from routes import users, movies, groups, reviews, groupusers, movietags, preferences, tags, userwatched, userwatchlist

app = FastAPI()

app.include_router(users.router)
app.include_router(movies.router)
app.include_router(groups.router)
app.include_router(reviews.router)
app.include_router(groupusers.router)
app.include_router(movietags.router)
app.include_router(preferences.router)
app.include_router(tags.router)
app.include_router(userwatched.router)
app.include_router(userwatchlist.router)