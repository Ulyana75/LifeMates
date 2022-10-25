package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.ulyanaab.lifemates.data.dto.request.MeRequestDto
import ru.ulyanaab.lifemates.data.dto.response.MeResponseDto

interface MeApi {

    @GET("me")
    fun getMe(): Call<MeResponseDto>

    @POST("me")
    fun updateMe(@Body meRequest: MeRequestDto): Call<Unit>
}
