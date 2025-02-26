import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from src.recommender import Recommender

recommender = Recommender()
ratings = []
filters = None

def actionAddRating():
    while(True):
        movie_id = input("Enter movie id: ")
        if not recommender.has_movie_id(movie_id):
            print("Invalid movie id")
        else:
            break
    while True:
        score = input("Enter rating: ")
        try:
            score = float(score)
            break
        except:
            print("Invalid score, try again")
    ratings.append({"movie_id": movie_id, "score": score})

def actionIncludeGenre():
    if filters["included_genres"] == None:
        filters["included_genres"] = []
    genre = input("Enter genre: ")
    filters["included_genres"].append(genre)

def actionExcludeGenre():
    if filters["excluded_genres"] == None:
        filters["excluded_genres"] = []
    genre = input("Enter genre: ")
    filters["excluded_genres"].append(genre)

def actionSetMinimumYear():
    while True:
        year = input("Enter year: ")
        try:
            year = int(year)
            break
        except:
            print("Invalid year, try again")
    filters["min_year"] = year

def actionSetMaximumYear():
    while True:
        year = input("Enter year: ")
        try:
            year = int(year)
            break
        except:
            print("Invalid year, try again")
    filters["max_year"] = year

def actionAddFilter():
    global filters
    if filters == None:
        filters = {
            "included_genres": None,
            "excluded_genres": None,
            "min_year": None,
            "max_year": None
        }
    print('Filters:')
    print("'+g' - add included genre")
    print("'-g' - add excluded genre")
    print("'+y' - set maximum year")
    print("'-y' - set minimum year")
    print("'s' - stop editing")
    while(True):
        action = input("Edit filter: ")
        if action == '+g':
            actionIncludeGenre()
        elif action == '-g':
            actionExcludeGenre()
        elif action == '-y':
            actionSetMinimumYear()
        elif action == '+y':
            actionSetMaximumYear()
        elif action == 's':
            break
        else:
            print("Invalid filter")

def actionClear():
    global filters
    ratings.clear()
    filters = None

def actionGetRecommendations():
    recs = recommender.get_k_recommendations(ratings, filters, 10)
    for i, rec in enumerate(recs):
        print('{:<2} {:<56} Score={:>12}'.format(i+1, rec[0], rec[1]))

def printActions():
    print('Actions:')
    print("'m' - add movie rating")
    print("'f' - edit filter")
    print("'r' - get recommendations")
    print("'c' - clear ratings and filters")
    print("'q' - quit")

printActions()
while(True):
    action = input("Enter action: ")
    if action == 'm':
        actionAddRating()
    elif action == 'f':
        actionAddFilter()
        printActions()
    elif action == 'r':
        actionGetRecommendations()
    elif action == 'c':
        actionClear()
    elif action == 'q':
        break
    else:
        print("Invalid action")