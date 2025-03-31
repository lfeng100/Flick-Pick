package ca.uwaterloo.flickpick.data.recommender

import ca.uwaterloo.flickpick.data.recommender.query.GroupRecommendationQuery
import ca.uwaterloo.flickpick.data.recommender.query.RecommendationQuery
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ca.uwaterloo.flickpick.data.recommender.response.RecommendationResponse
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


interface RecommenderApiService {
    @POST("/recommend")
    suspend fun getRecommendations(@Body query: RecommendationQuery): RecommendationResponse

    @POST("/grouprec")
    suspend fun getGroupRecommendations(@Body query: GroupRecommendationQuery): RecommendationResponse
}

object RecommenderClient {
    private const val BASE_URL = "http://10.0.2.2:8000"

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    val apiService: RecommenderApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(RecommenderApiService::class.java)
    }
}


