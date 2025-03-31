package ca.uwaterloo.flickpick.data.database.response

import ca.uwaterloo.flickpick.data.database.model.User
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class UserResponse(
    val items: List<User>,
    val total: Int,
    val page: Int,
    val pages: Int
)