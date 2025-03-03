package ca.uwaterloo.flickpick.dataObjects

data class Preferences(
    val user_id: String,
    val preferred_genres: List<String>,
    val disliked_genres: List<String>,
    val preferred_languages: List<String>
)