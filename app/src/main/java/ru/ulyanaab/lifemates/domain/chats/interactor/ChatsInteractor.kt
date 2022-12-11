package ru.ulyanaab.lifemates.domain.chats.interactor

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ru.ulyanaab.lifemates.common.Error
import ru.ulyanaab.lifemates.common.Result
import ru.ulyanaab.lifemates.domain.chats.model.ChatModel
import ru.ulyanaab.lifemates.domain.chats.repository.ChatsRepository
import ru.ulyanaab.lifemates.domain.common.interactor.TokenInteractor
import ru.ulyanaab.lifemates.domain.common.utils.ResultProcessorWithTokensRefreshing
import javax.inject.Inject

class ChatsInteractor @Inject constructor(
    private val chatsRepository: ChatsRepository,
    private val tokenInteractor: TokenInteractor,
    private val resultProcessorWithTokensRefreshing: ResultProcessorWithTokensRefreshing,
) {
    private var previousChats: List<ChatModel>? = null
    private var isFirstRequest = true

    val chatsFlow = flow {
        while (true) {
            when (
                val result = chatsRepository.getAllChats(
                    0,
                    if (isFirstRequest) CHATS_FIRST_REQUEST_COUNT else CHATS_REQUEST_COUNT
                )
            ) {
                is Result.Success -> {
                    result.data?.let {
                        val hasChanges = it != previousChats

                        if (hasChanges) {
                            previousChats = it
                            emit(it)
                        }
                    }
                }
                is Result.Failure -> {
                    when (result.error) {
                        is Error.Unauthorized -> tokenInteractor.getAndSaveNewTokens()
                        else -> return@flow
                    }
                }
            }

            delay(POLLING_TIMEOUT_MS)
        }
    }

    suspend fun getChats(offset: Int, limit: Int): List<ChatModel>? {
        return resultProcessorWithTokensRefreshing.proceedAndReturn(
            resultProducer = {
                chatsRepository.getAllChats(offset, limit)
            },
            onTokensRefreshedSuccessfully = {
                getChats(offset, limit)
            }
        )
    }

    companion object {
        private const val POLLING_TIMEOUT_MS = 10000L
        private const val CHATS_REQUEST_COUNT = 10
        private const val CHATS_FIRST_REQUEST_COUNT = 20
    }
}
