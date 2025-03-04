package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Recommender.Querys.RecommendationQuery
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.*
import ca.uwaterloo.flickpick.dataObjects.Database.Movie
import ca.uwaterloo.flickpick.dataObjects.Database.Responses.MovieResponse
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.GenreResponse
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.LanguageResponse
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.RecommendationResponse
import retrofit2.http.Query


interface DatabaseApiService {

    // Movies
    @GET("movies/")
    suspend fun getAllMovies(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0
    ): MovieResponse

    @GET("movie/{id}")
    suspend fun getMovieById(@Path("id") movieId: String): Movie


}

object DatabaseClient {
    private const val BASE_URL = "http://10.0.2.2:9000"

    private val moshi = Moshi.Builder().build()

    val apiService: DatabaseApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(DatabaseApiService::class.java)
    }
}

interface RecommenderApiService {

    @POST("/recommend")
    suspend fun getRecommendations(@Body query: RecommendationQuery): RecommendationResponse

    @GET("/genres")
    suspend fun getGenres(): GenreResponse

    @GET("/languages")
    suspend fun getLanguages(): LanguageResponse
}

object RecommenderClient {
    private const val BASE_URL = "http://10.0.2.2:8000"

    private val moshi = Moshi.Builder().build()

    val apiService: RecommenderApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RecommenderApiService::class.java)
    }
}
