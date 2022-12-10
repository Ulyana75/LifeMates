package ru.ulyanaab.lifemates.data.repositoryimpl

import retrofit2.awaitResponse
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.data.api.ChatsApi
import ru.ulyanaab.lifemates.data.mapper.ChatsMapper
import ru.ulyanaab.lifemates.domain.chats.model.ChatMessageModel
import ru.ulyanaab.lifemates.domain.chats.model.ChatModel
import ru.ulyanaab.lifemates.domain.chats.repository.ChatsRepository
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val chatsApi: ChatsApi,
    private val chatsMapper: ChatsMapper,
) : ChatsRepository {

    override suspend fun getAllChats(offset: Int, limit: Int): Result<List<ChatModel>?> {
        val response = chatsApi.getAllChats(offset, limit).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.chats?.map(chatsMapper::map))
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun sendMessage(chatId: Long, message: String): Result<Unit> {
        val response = chatsApi.sendMessage(chatId, message).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(Unit)
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }

    override suspend fun getMessages(
        chatId: Long,
        offset: Int,
        limit: Int
    ): Result<List<ChatMessageModel>?> {
        val response = chatsApi.getMessages(chatId, offset, limit).awaitResponse()
        return when (response.code()) {
            200 -> Result.Success(response.body()?.messages?.map(chatsMapper::map))
            401 -> Result.Failure(Error.Unauthorized)
            else -> Result.Failure(Error.Unknown)
        }
    }


}
