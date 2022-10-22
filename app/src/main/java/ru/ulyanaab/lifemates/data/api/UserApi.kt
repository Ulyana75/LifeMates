package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ulyanaab.lifemates.data.dto.common.OtherUserDto
import ru.ulyanaab.lifemates.data.dto.response.GetUsersResponseDto

interface UserApi {

    @GET("users")
    fun getUsers(@Query("Limit") limit: Int, ): Call<GetUsersResponseDto>

    @GET("users/{userId}")
    fun getSingleUser(@Path("userId") userId: Long): Call<OtherUserDto>

    @POST("users/{userId}/like")
    fun like(@Path("userId") userId: Long): Call<Unit>

    @POST("users/{userId}/dislike")
    fun dislike(@Path("userId") userId: Long): Call<Unit>

//    @PUT("users/{userId}/status/{newStatus}")
//    fun changeStatus(
//        @Path("userId") userId: String,
//        @Path("newStatus") newStatus
//    )
}
