package ru.ulyanaab.lifemates.ui.chats

import ru.ulyanaab.lifemates.domain.chats.model.ChatModel
import javax.inject.Inject

class ChatsMapper @Inject constructor() {

    fun map(model: ChatModel): ChatUiModel {
        return ChatUiModel(
            id = model.id,
            userId = model.user.id,
            userName = model.user.name,
            userImageUrl = model.user.imageUrls.firstOrNull() ?: "",
            message = model.message.let {
                val isFromMe = it.userId != model.user.id
                if (isFromMe) {
                    "Вы: ${it.content}"
                } else it.content ?: ""
            },
            unreadCount = model.unreadCount,
        )
    }
}
