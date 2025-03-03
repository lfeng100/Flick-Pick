package ca.uwaterloo.flickpick.managers.Recommendation

import ca.uwaterloo.flickpick.dataObjects.Database.Group

class RecommendationManager(private var selectedGroup: Group?) {
//    suspend fun getPersonalReco(): Movie {
//        return withContext(Dispatchers.IO) {
//            //for now uses averngers endgame
//            val userRatings = listOf(
//                Rating("avengers-endgame", 4.5f)
//            )
//
//            val filters = Filters(
//                included_genres = listOf("Action"),
//                min_score = 4.0f
//            )
//
//            val query = Query(userRatings, filters)
//
//
//            val response = Client.apiService.getRecommendations(query)
//
//            if (response is RecommendationResponse) {
//                val recommendations = response.recommendations
//                if (recommendations.isNotEmpty()) {
//                    println(recommendations[0])
//                    return@withContext Movie(recommendations[0], "Unknown", 0.0)
//                }
//            }
//            return@withContext Movie("No Recommendation", "N/A", 0.0)
//
//        }
//    }


//    override fun getGroupReco(selectedGroup: Group): Movie {
//        // Recommend a random movie for the group (can be improved with real logic)
//        return getPersonalReco()
//    }
}