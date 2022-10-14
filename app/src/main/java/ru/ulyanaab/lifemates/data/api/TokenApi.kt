package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ulyanaab.lifemates.data.dto.common.TokensDto

interface TokenApi {

    @POST("Token/refresh")
    fun refresh(@Body tokensDto: TokensDto): Call<TokensDto>

    @POST("Token/revoke")
    fun revoke(@Body tokensDto: TokensDto): Call<TokensDto>
}
