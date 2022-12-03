package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.GET
import ru.ulyanaab.lifemates.data.dto.response.GetInterestsResponse

interface InterestsApi {
    @GET("interests")
    fun getInterests(): Call<GetInterestsResponse>
}
