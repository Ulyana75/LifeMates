package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ulyanaab.lifemates.data.dto.request.LoginRequestDto
import ru.ulyanaab.lifemates.data.dto.request.RegisterUserRequestDto
import ru.ulyanaab.lifemates.data.dto.common.TokensDto

interface AuthApi {

    @POST("auth/register")
    fun register(@Body registerUserRequestDto: RegisterUserRequestDto): Call<TokensDto>

    @POST("auth/login")
    fun login(@Body loginRequestDto: LoginRequestDto): Call<TokensDto>
}
