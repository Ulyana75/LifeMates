package ru.ulyanaab.lifemates.data.mapper

import ru.ulyanaab.lifemates.data.dto.response.ChatDto
import ru.ulyanaab.lifemates.data.dto.response.ChatMessageDto
import ru.ulyanaab.lifemates.data.dto.response.ChatUserDto
import ru.ulyanaab.lifemates.data.dto.response.MessageDto
import ru.ulyanaab.lifemates.domain.chats.model.ChatMessageModel
import ru.ulyanaab.lifemates.domain.chats.model.ChatModel
import ru.ulyanaab.lifemates.domain.chats.model.ChatUserModel
import javax.inject.Inject

class ChatsMapper @Inject constructor() {

    fun map(dto: ChatUserDto): ChatUserModel {
        return ChatUserModel(
            id = dto.id,
            name = dto.name,
            imageUrls = dto.imageUrls,
        )
    }

    fun map(dto: ChatMessageDto): ChatMessageModel {
        return ChatMessageModel(
            id = 0,
            content = dto.content,
            createdAt = dto.createdAt,
            userId = dto.user.id,
            isSeen = dto.isSeen,
        )
    }

    fun map(dto: MessageDto): ChatMessageModel {
        return ChatMessageModel(
            id = dto.id,
            content = dto.content,
            createdAt = dto.createdAt,
            userId = dto.userId,
            isSeen = dto.isSeen,
        )
    }

    fun map(dto: ChatDto): ChatModel {
        return ChatModel(
            id = dto.id,
            message = map(dto.message),
            user = map(dto.user),
        )
    }
}
