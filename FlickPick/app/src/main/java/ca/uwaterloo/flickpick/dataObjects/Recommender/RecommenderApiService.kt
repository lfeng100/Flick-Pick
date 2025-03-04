package ca.uwaterloo.flickpick.dataObjects.Recommender

import ca.uwaterloo.flickpick.dataObjects.Recommender.Querys.RecommendationQuery
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.GenreResponse
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.LanguageResponse
import ca.uwaterloo.flickpick.dataObjects.Recommender.Reponses.RecommendationResponse


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


