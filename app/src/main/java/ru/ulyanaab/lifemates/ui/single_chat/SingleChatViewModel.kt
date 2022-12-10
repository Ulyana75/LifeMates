package ru.ulyanaab.lifemates.ui.single_chat

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ulyanaab.lifemates.domain.chats.interactor.SingleChatInteractor
import ru.ulyanaab.lifemates.ui.single_chat.di.WITH_USER_ID
import javax.inject.Inject
import javax.inject.Named

class SingleChatViewModel @Inject constructor(
    @Named(WITH_USER_ID) private val withUserId: Long,
    private val singleChatInteractor: SingleChatInteractor,
) {

    private val _messagesStateFlow: MutableStateFlow<MutableList<MessageUiModel>> =
        MutableStateFlow(mutableListOf())
    val messagesStateFlow: StateFlow<List<MessageUiModel>> = _messagesStateFlow.asStateFlow()
}
