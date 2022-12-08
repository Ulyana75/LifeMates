package ru.ulyanaab.lifemates.data.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ulyanaab.lifemates.data.dto.response.GetChatsResponseDto

interface ChatsApi {

    @GET("chats")
    fun getAllChats(
        @Query("Offset") offset: Int,
        @Query("Limit") limit: Int
    ): Call<GetChatsResponseDto>

    @POST("chats/{chatId}/message")
    fun sendMessage(
        @Path("chatId") chatId: String,
        @Body message: String
    ): Call<Unit>

    @GET("chats/{chatId}/message")
    fun getMessages(
        @Path("chatId") chatId: String,
        @Query("Offset") offset: Int,
        @Query("Limit") limit: Int
    )
}
