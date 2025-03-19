package ca.uwaterloo.flickpick.dataObjects.Database

import ca.uwaterloo.flickpick.dataObjects.Database.Models.Group
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Review
import ca.uwaterloo.flickpick.dataObjects.Database.Models.ReviewCreate
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.GroupUsers
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.MovieResponse
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.GroupResponse
import ca.uwaterloo.flickpick.dataObjects.Database.Models.UserCreate
import ca.uwaterloo.flickpick.dataObjects.Database.Models.UserWatched
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.ReviewResponse
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.http.Query


interface DatabaseApiService {
    // users
    @POST("users/")
    suspend fun createUser(@Body userCreate: UserCreate): User

    @GET("users/{user_id}")
    suspend fun getUser(@Path("user_id") userID: String): Response<User?>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") userId: String): User

    // Movies
    @GET("movies/")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movies/search/")
    suspend fun searchMovies(
        @Query("title_query") titleQuery: String? = null,
        @Query("tag_ids") tagIDs: List<String>? = null,
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(@Path("id") movieID: String): Movie

    // tags
    @GET("tags/")
    suspend fun getTags(): List<Tag>

    // Groups
    @GET("groups/{groupID}")
    suspend fun getGroupsById(@Path("groupID") groupId: String): GroupUsers
 
    @GET("groups/")
    suspend fun getAllGroups(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): GroupResponse

    @GET("group/{id}")
    suspend fun getGroupById(@Path("id") groupId: String): Group

    // reviews
    @POST("reviews/")
    suspend fun createReview(@Body review: ReviewCreate): Review

    @DELETE("reviews/{review_id}")
    suspend fun deleteReview(@Path("review_id") reviewID: String): Review

    @GET("reviews/user/{user_id}")
    suspend fun getReviewsForUser(
        @Path("user_id") userID: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ) : ReviewResponse

    // userwatched
    @POST("userwatched/")
    suspend fun addUserWatched(@Body userWatched: UserWatched)

    @DELETE("userwatched/{user_id}/{movie_id}")
    suspend fun deleteUserWatched(
        @Path("user_id") userID: String,
        @Path("movie_id") movieID: String
    )

    @GET("userwatched/{user_id}")
    suspend fun getUserWatched(
        @Path("user_id") userID: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    // userwatchlist
    @POST("userwatchlist/")
    suspend fun addUserWatchlist(@Body userWatched: UserWatched)

    @DELETE("userwatchlist/{user_id}/{movie_id}")
    suspend fun deleteUserWatchlist(
        @Path("user_id") userID: String,
        @Path("movie_id") movieID: String
    )

    @GET("userwatchlist/{user_id}")
    suspend fun getUserWatchlist(
        @Path("user_id") userID: String,
        @Query("limit") limit: Int = 100,
        @Query("offset") offset: Int = 0
    ): MovieResponse
}

object DatabaseClient {
    private const val BASE_URL = "http://10.0.2.2:9000"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val apiService: DatabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DatabaseApiService::class.java)
    }
}
