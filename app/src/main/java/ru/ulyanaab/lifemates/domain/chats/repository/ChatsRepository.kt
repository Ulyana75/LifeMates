package ru.ulyanaab.lifemates.domain.chats.repository

import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.chats.model.ChatMessageModel
import ru.ulyanaab.lifemates.domain.chats.model.ChatModel
import ru.ulyanaab.lifemates.domain.chats.model.ThemeModel

interface ChatsRepository {
    suspend fun getAllChats(offset: Int, limit: Int): Result<List<ChatModel>?>
    suspend fun sendMessage(chatId: Long, message: String): Result<Unit>
    suspend fun getMessages(
        chatId: Long,
        offset: Int,
        limit: Int
    ): Result<List<ChatMessageModel>?>
    suspend fun getThemes(chatId: Long, offset: Int, limit: Int): Result<List<ThemeModel>?>
}
