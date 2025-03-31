class ExcludedMovieIdFilter:
    def __init__(self, excludedMovieIDs: list):
        self.excludedMovieIDs = set(excludedMovieIDs)

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return movie_id not in self.excludedMovieIDs

class IncludedGenresFilter:
    def __init__(self, includedGenres: list):
        self.includedGenres = includedGenres

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return not movie_info['genres'].isdisjoint(self.includedGenres)

class ExcludedGenresFilter:
    def __init__(self, excludedGenres: list):
        self.excludedGenres = excludedGenres

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return movie_info['genres'].isdisjoint(self.excludedGenres)

class MinYearFilter:
    def __init__(self, minYear: int):
        self.minYear = minYear

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return self.minYear < movie_info['year']

class MaxYearFilter:
    def __init__(self, maxYear: int):
        self.maxYear = maxYear

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return movie_info['year'] < self.maxYear

class LanguagesFilter:
    def __init__(self, languages: list):
        self.languages = set(languages)

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return movie_info['original_language'] in self.languages

class RuntimeFilter:
    def __init__(self, maxRuntime: int):
        self.maxRuntime = maxRuntime

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return self.maxRuntime > movie_info['runtime']

class ScoreFilter:
    def __init__(self, minScore: int):
        self.minScore = minScore

    def allow(self, movie_id: str, movie_info: dict) -> bool:
        return self.minScore < movie_info['vote_average']