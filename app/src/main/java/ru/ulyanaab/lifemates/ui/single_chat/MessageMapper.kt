package ru.ulyanaab.lifemates.ui.single_chat

import ru.ulyanaab.lifemates.domain.chats.model.ChatMessageModel
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatForMessagesFromBack
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterForMessages
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUniversal
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterUsual
import ru.ulyanaab.lifemates.ui.common.utils.dateFormatterWithTime
import ru.ulyanaab.lifemates.ui.single_chat.di.WITH_USER_ID
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Named

class MessageMapper @Inject constructor(
    @Named(WITH_USER_ID) private val withUserId: Long,
) {

    fun map(model: ChatMessageModel): MessageUiModel {
        return MessageUiModel.SentMessageUiModel(
            id = model.id,
            text = model.content ?: "",
            date = LocalDateTime.parse(model.createdAt, dateFormatForMessagesFromBack()).format(
                dateFormatterForMessages()
            ),
            isFromMe = model.userId != withUserId,
            isSeen = model.isSeen,
        )
    }
}
