package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import ru.ulyanaab.lifemates.data.dto.request.MeRequestDto
import ru.ulyanaab.lifemates.data.dto.response.MeResponseDto

interface MeApi {

    @GET("me")
    fun getMe(): Call<MeResponseDto>

    @PUT("me")
    fun putMe(meRequest: MeRequestDto): Call<Unit>
}
