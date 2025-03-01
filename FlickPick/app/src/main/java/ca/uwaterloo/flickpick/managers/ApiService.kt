package ca.uwaterloo.flickpick.managers

import ca.uwaterloo.flickpick.dataObjects.Query
import com.squareup.moshi.Moshi
import retrofit2.http.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory



// Response Models
data class RecommendationResponse(
    val recommendations: List<String>
)

data class GenresResponse(
    val genres: List<String>
)

data class LanguagesResponse(
    val languages: List<String>
)

interface ApiService {
    @POST("recommend")
    suspend fun getRecommendations(@Body query: Query): RecommendationResponse

    @GET("genres")
    suspend fun getGenres(): GenresResponse

    @GET("languages")
    suspend fun getLanguages(): LanguagesResponse
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
