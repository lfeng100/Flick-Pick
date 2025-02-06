from surprise import dump
import numpy as np

_, model = dump.load("./model")

all_inner_ids = model.trainset.all_items()
all_film_ids = [model.trainset.to_raw_iid(inner_id) for inner_id in all_inner_ids]
score_map = {film_id: 0 for film_id in all_film_ids}

target_film_ids = ["taxi-driver", "mean-streets", "reservoir-dogs", "pulp-fiction", "the-king-of-comedy", "avengers-infinity-war"]
weights = [5, 5, 3.5, 3.5, 5, 5, 1.5]
for i in range(len(weights)):
    weights[i] -= 2.5 # normalize weights to [-2.5, 2.5]

for target_film_id, weight in zip(target_film_ids, weights):
    inner_id = model.trainset.to_inner_iid(target_film_id)

    sims = model.sim[inner_id, :]
    min_sim = np.min(sims)
    max_sim = np.max(sims)
    norm_sims = (sims - min_sim) / (max_sim - min_sim)

    for other_inner_id in all_inner_ids:
        other_film_id = model.trainset.to_raw_iid(other_inner_id)
        if other_film_id in target_film_ids:
            continue
        score = norm_sims[other_inner_id]
        score_map[other_film_id] += score * weight

    print("Finished processing: ", target_film_id)

for i, film_id in enumerate(sorted(score_map, key=lambda x: score_map[x], reverse=True)[:50]):
    print(i + 1, ". ", film_id, ", Similarity: ", score_map[film_id])