package ru.ulyanaab.lifemates.domain.chats.interactor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.chats.model.ChatMessageModel
import ru.ulyanaab.lifemates.domain.chats.repository.ChatsRepository
import ru.ulyanaab.lifemates.domain.common.interactor.TokenInteractor
import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import javax.inject.Inject
import javax.inject.Named

const val CHAT_ID = "chat_id"

class SingleChatInteractor @Inject constructor(
    @Named(CHAT_ID) private val chatId: Long,
    private val chatsRepository: ChatsRepository,
    private val tokenInteractor: TokenInteractor,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {

    private val previousMessages: List<ChatMessageModel> = emptyList()

    val messagesFlow = flow {
        while (true) {
            when (val messagesResult = chatsRepository.getMessages(chatId, MESSAGE_COUNT, 0)) {
                is Result.Success -> {
                    messagesResult.data?.let {
                        emit(compareEndFilterNewMessages(it).toSet())
                    }
                }
                is Result.Failure -> {
                    when (messagesResult.error) {
                        is Error.Unauthorized -> tokenInteractor.getAndSaveNewTokens()
                        else -> return@flow
                    }
                }
            }

            delay(POLLING_TIMEOUT_MS)
        }
    }

    suspend fun getMessages(offset: Int, limit: Int): List<ChatMessageModel>? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                chatsRepository.getMessages(chatId, offset, limit)
            },
            onTokensRefreshedSuccessfully = {
                getMessages(offset, limit)
            }
        )
    }

    suspend fun sendMessage(text: String): Unit? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                chatsRepository.sendMessage(chatId, text)
            },
            onTokensRefreshedSuccessfully = {
                sendMessage(text)
            }
        )
    }

    private fun compareEndFilterNewMessages(newMessages: List<ChatMessageModel>): List<ChatMessageModel> {
        val firstInOld = previousMessages.firstOrNull()
        val lastIndexOfNew = newMessages.indexOf(firstInOld)
        return if (lastIndexOfNew != -1) {
            newMessages.subList(0, lastIndexOfNew)
        } else newMessages
    }

    companion object {
        private const val POLLING_TIMEOUT_MS = 2000L
        private const val MESSAGE_COUNT = 10
    }
}
