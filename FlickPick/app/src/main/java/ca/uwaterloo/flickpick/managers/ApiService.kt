package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Querys.RecommendationQuery
import ca.uwaterloo.flickpick.dataObjects.Responses.*
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.*
import retrofit2.Response
import retrofit2.http.Query


interface ApiService {

    // Movie Recommendations
    @POST("recommend")
    suspend fun getRecommendations(@Body query: RecommendationQuery): RecommendationResponse

    // Movies
    @GET("movies/{movie_id}")
    suspend fun getMovie(@Path("movie_id") movieId: String): Movie

    @GET("movies/")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movies/{id}")
    suspend fun getMovieById(@Path("id") movieId: String): Movie

    @PUT("movies/{movie_id}")
    suspend fun updateMovie(@Path("movie_id") movieId: String, @Body movie: Movie): Movie
}

object DatabaseClient {
    private const val BASE_URL = "http://10.0.2.2:9000"

    private val moshi = Moshi.Builder().build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ApiService::class.java)
    }
}

object RecommenderClient {
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
