package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.ulyanaab.lifemates.data.dto.response.GetMatchesResponseDto

interface MatchApi {
    @GET("match")
    fun getMatches(
        @Query("Offset") offset: Int,
        @Query("Limit") limit: Int,
    ): Call<GetMatchesResponseDto>
}
