package ca.uwaterloo.flickpick.dataObjects.Database.Responses

import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class UserResponse(
    val items: List<User>,
    val total: Int,
    val page: Int,
    val pages: Int
)