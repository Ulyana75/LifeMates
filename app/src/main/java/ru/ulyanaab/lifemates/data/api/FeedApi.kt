package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ulyanaab.lifemates.data.dto.response.GetFeedResponseDto

interface FeedApi {

    @GET("Feed")
    fun getFeed(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Call<GetFeedResponseDto>

    @POST("Feed/{userId}/like")
    fun like(@Path("userId") userId: String): Call<Unit>

    @POST("Feed/{userId}/dislike")
    fun dislike(@Path("userId") userId: String): Call<Unit>
}
