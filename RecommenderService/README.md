Tested on Python 3.10

Datasets used for training are found here:

https://www.kaggle.com/datasets/samlearner/letterboxd-movie-ratings-data
https://www.kaggle.com/datasets/freeth/letterboxd-film-ratings

Steps to preprocess data and train model:
1. Extract datasets to ./data/raw/
2. Run ./scripts/prep_data.py
3. Run ./scripts/train.py

Or you can download the preprocessed datasets and pretrained model [here](https://drive.google.com/drive/folders/1Qyxc8nsFuLJ76knzpUGfneqgnS4aSZyY?usp=drive_link):
1. Extract processed datasets to ./data/processed/
2. Extract model to ./model/

Once the model is extracted:

Start the recommender service: uvicorn src.main:app --host 0.0.0.0 --port 8000 --reload --env-file .env

Command line interface for manual testing: ./scripts/cli.py

