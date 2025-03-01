package ca.uwaterloo.flickpick.managers.Recommendation

import ca.uwaterloo.flickpick.dataObjects.Movie
import ca.uwaterloo.flickpick.dataObjects.Group
class RecommendationManagerImpl(private var selectedGroup: Group?) : RecommendationManager {

    private val movieDatabase = listOf(
        Movie("Inception", "Sci-Fi", 8.8),
        Movie("Titanic", "Romance", 7.8),
        Movie("The Dark Knight", "Action", 9.0),
        Movie("Toy Story", "Animation", 8.3),
        Movie("Parasite", "Thriller", 8.6)
    )

    override fun getPersonalReco(): Movie {
        return movieDatabase.random() // Simulating a personal recommendation
    }

    override fun getGroupReco(selectedGroup: Group): Movie {
        // Recommend a random movie for the group (can be improved with real logic)
        return movieDatabase.random()
    }
}