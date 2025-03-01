package ca.uwaterloo.flickpick.managers.Recommendation

import ca.uwaterloo.flickpick.dataObjects.Movie
import ca.uwaterloo.flickpick.dataObjects.Group
interface RecommendationManager {
    fun getPersonalReco(): Movie
    fun getGroupReco(selectedGroup: Group): Movie
}