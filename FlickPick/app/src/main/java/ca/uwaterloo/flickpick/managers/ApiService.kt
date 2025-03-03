package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Query
import ca.uwaterloo.flickpick.dataObjects.Responses.*
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.*
import retrofit2.Response

interface ApiService {

    // Movie Recommendations
    @POST("recommend")
    suspend fun getRecommendations(@Body query: Query): RecommendationResponse

    // Genres & Languages
    @GET("genres")
    suspend fun getGenres(): GenresResponse

    @GET("languages")
    suspend fun getLanguages(): LanguagesResponse

    // Movies
    @GET("movies/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: String): Movie

    @GET("movies")
    suspend fun getMovies(): Response<MovieResponse>


    @POST("movies")
    suspend fun addMovie(@Body movie: Movie): Movie

    @PUT("movies/{movie_id}")
    suspend fun updateMovie(@Path("movie_id") movieId: String, @Body movie: Movie): Movie

    @DELETE("movies/{movie_id}")
    suspend fun deleteMovie(@Path("movie_id") movieId: String)

    // ✅ User Authentication (if needed)
    @POST("users/login")
    suspend fun loginUser(@Body userLogin: UserLogin): UserResponse

    @POST("users/register")
    suspend fun registerUser(@Body userRegister: UserRegister): UserResponse

    // ✅ User Data (Watchlist & Watched Movies)
    @GET("userwatched/{user_id}")
    suspend fun getUserWatchedMovies(@Path("user_id") userId: String): List<Movie>

    @POST("userwatched/{user_id}")
    suspend fun addUserWatchedMovie(@Path("user_id") userId: String, @Body movie: Movie)

    @GET("userwatchlist/{user_id}")
    suspend fun getUserWatchlist(@Path("user_id") userId: String): List<Movie>

    @POST("userwatchlist/{user_id}")
    suspend fun addUserWatchlist(@Path("user_id") userId: String, @Body movie: Movie)

    // ✅ Reviews
    @GET("reviews/{movie_id}")
    suspend fun getMovieReviews(@Path("movie_id") movieId: String): List<Review>

    @POST("reviews/{movie_id}")
    suspend fun addMovieReview(@Path("movie_id") movieId: String, @Body review: Review)

    // ✅ Tags & Preferences
    @GET("tags")
    suspend fun getTags(): TagsResponse

    @POST("preferences/{user_id}")
    suspend fun updateUserPreferences(@Path("user_id") userId: String, @Body preferences: Preferences)

    // ✅ Groups
    @GET("groups/{group_id}")
    suspend fun getGroup(@Path("group_id") groupId: String): Group

    @POST("groups")
    suspend fun createGroup(@Body group: Group)

    @DELETE("groups/{group_id}")
    suspend fun deleteGroup(@Path("group_id") groupId: String)

    // ✅ Group Members
    @GET("groupusers/{group_id}")
    suspend fun getGroupUsers(@Path("group_id") groupId: String): List<User>

    @POST("groupusers/{group_id}")
    suspend fun addUserToGroup(@Path("group_id") groupId: String, @Body user: User)

    @DELETE("groupusers/{group_id}/{user_id}")
    suspend fun removeUserFromGroup(@Path("group_id") groupId: String, @Path("user_id") userId: String)
}

object Client {
    private const val BASE_URL = "http://10.0.2.2:8000"

    private val moshi = Moshi.Builder().build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}

class MovieRepository {
    private val api = Client.apiService

    suspend fun getMovie(movieId: String): Movie {
        return api.getMovie(movieId)
    }

    suspend fun getRecommendations(query: Query): List<String> {
        return api.getRecommendations(query).recommendations
    }

    suspend fun getGenres(): List<String> {
        return api.getGenres().genre
    }

    suspend fun getLanguages(): List<String> {
        return api.getLanguages().languages
    }
}
