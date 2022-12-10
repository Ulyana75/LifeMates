package ru.ulyanaab.lifemates.ui.single_chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.chats.interactor.SingleChatInteractor
import ru.ulyanaab.lifemates.ui.single_chat.di.WITH_USER_ID
import java.util.Collections
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class SingleChatViewModel @Inject constructor(
    @Named(WITH_USER_ID) private val withUserId: Long,
    private val singleChatInteractor: SingleChatInteractor,
    private val messageMapper: MessageMapper,
) {

    private val _messagesStateFlow: MutableStateFlow<MutableList<MessageUiModel>> =
        MutableStateFlow(mutableListOf())
    val messagesStateFlow: StateFlow<List<MessageUiModel>> = _messagesStateFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _messagesAreFinishedFlow = MutableStateFlow(false)
    val messagesAreFinishedFlow: StateFlow<Boolean> = _messagesAreFinishedFlow.asStateFlow()

    private val _messagesInProgress: MutableStateFlow<MutableList<Long>> =
        MutableStateFlow(mutableListOf())
    val messagesInProgress: StateFlow<List<Long>> = _messagesInProgress.asStateFlow()

    private var messagesPollingJob: Job? = null

    private val stubsForRemoving: MutableList<UUID> =
        Collections.synchronizedList(emptyList())

    fun attach() {
        messagesPollingJob = CoroutineScope(Dispatchers.IO).launch {
            singleChatInteractor.messagesFlow.collect { newMessages ->
                val newList =
                    (newMessages.map(messageMapper::map) + _messagesStateFlow.value).toMutableList()

                val removed = mutableListOf<UUID>()
                stubsForRemoving.forEach { id ->
                    newList.removeIf {
                        it is MessageUiModel.StubMessageUiModel && it.id == id
                        removed.add(id)
                    }
                }

                _messagesStateFlow.value = newList

                stubsForRemoving.removeAll(removed)

            }
        }
    }

    fun detach() {
        messagesPollingJob?.cancel()
    }

    fun sendMessage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val stub = MessageUiModel.StubMessageUiModel(
                id = UUID.randomUUID(),
                text = text,
                date = "", // TODO
            )
            _messagesStateFlow.value = (listOf(stub) + _messagesStateFlow.value).toMutableList()

            val result = singleChatInteractor.sendMessage(text)

            if (result == null) {
                _messagesStateFlow.value.replaceAll {
                    if (it is MessageUiModel.StubMessageUiModel && it.id == stub.id) {
                        stub.copy(isFailed = true)
                    } else it
                }
            } else {
                stubsForRemoving.add(stub.id)
            }
        }
    }

    fun requestNext() {
        CoroutineScope(Dispatchers.Default).launch {
            if (!_messagesAreFinishedFlow.value) {
                val nextMessagesList = singleChatInteractor.getMessages(
                    _messagesStateFlow.value.size,
                    MESSAGES_REQUEST_COUNT
                )

                if (nextMessagesList.isNullOrEmpty()) {
                    _messagesAreFinishedFlow.value = true
                } else {
                    _messagesStateFlow.value.addAll(
                        nextMessagesList.map(messageMapper::map)
                    )
                }

                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val MESSAGES_REQUEST_COUNT = 20
        const val MESSAGES_TILL_END_TO_REQUEST = 8
    }
}
