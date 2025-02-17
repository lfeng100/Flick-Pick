import recommender as r

recommender = r.Recommender()

ratings = {}
filters = set()

print('Actions:')
print("'m' - add movie rating")
print("'f' - add filter")
print("'r' - get recommendations")
print("'c' - clear ratings and filters")
print("'q' - quit")

def actionAddRating():
    while(True):
        movie_id = input("Enter movie id: ")
        if not recommender.hasMovieId(movie_id):
            print("Invalid movie id")
        else:
            break
    score = input("Enter rating: ")
    ratings[movie_id] = score

def actionAddFilter():
    genre = input("Enter genre filter: ")
    filters.add(genre)
    pass

def actionClear():
    ratings.clear()
    filters.clear()

def actionGetRecommendations():
    recs = recommender.getKRecommendations(ratings, filters, 10)
    for i, rec in enumerate(recs):
        print('{:<2} {:<56} Score={:>12}'.format(i+1, rec[0], rec[1]))

while(True):
    action = input("Enter action: ")
    if action == 'm':
        actionAddRating()
    elif action == 'f':
        actionAddFilter()
    elif action == 'r':
        actionGetRecommendations()
    elif action == 'c':
        actionClear()
    elif action == 'q':
        break
    else:
        print("Bad action")