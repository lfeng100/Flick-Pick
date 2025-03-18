package ca.uwaterloo.flickpick.dataObjects.Database

import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.Models.Tag
import ca.uwaterloo.flickpick.dataObjects.Database.Models.User
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.MovieResponse
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.TagResponse
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.http.Query


interface DatabaseApiService {
    // users
    @POST("users/")
    suspend fun createUser(@Body newUser: User)

    // movies
    @GET("movies/")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movies/search/")
    suspend fun searchMovies(
        @Query("title_query") titleQuery: String? = null,
        @Query("tag_ids") selectedTags: List<String>? = null,
        @Query("limit") limit: Int = 12,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(@Path("id") movieId: String): Movie

    // tags
    @GET("tags/")
    suspend fun getTags(): List<Tag>
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
