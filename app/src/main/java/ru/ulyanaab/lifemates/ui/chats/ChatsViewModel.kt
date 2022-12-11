package ru.ulyanaab.lifemates.ui.chats

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.chats.interactor.ChatsInteractor
import ru.ulyanaab.lifemates.domain.match.interactor.MatchInteractor
import ru.ulyanaab.lifemates.ui.single_chat.MessageUiModel
import ru.ulyanaab.lifemates.ui.single_chat.SingleChatViewModel
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
    private val matchInteractor: MatchInteractor,
    private val chatsInteractor: ChatsInteractor,
    private val chatsMapper: ChatsMapper,
) {
    private val _chatsStateFlow: MutableStateFlow<MutableList<ChatUiModel>> =
        MutableStateFlow(mutableListOf())
    val chatsStateFlow: StateFlow<List<ChatUiModel>> = _chatsStateFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _hasMatches = MutableStateFlow(false)
    val hasMatches: StateFlow<Boolean> = _hasMatches.asStateFlow()

    private var pollingJob: Job? = null
    private var chatsAreFinished = false

    fun attach() {
        pollingJob?.cancel()
        pollingJob = CoroutineScope(Dispatchers.Default).launch {
            val matchesCount = matchInteractor.getMatches(0, 0)?.count
            _hasMatches.value = matchesCount != null && matchesCount > 0

            chatsInteractor.chatsFlow.collect { updatedChats ->
                _chatsStateFlow.value.removeIf { chat ->
                    updatedChats.map { it.id }.contains(chat.id)
                }
                val newList =
                    (updatedChats.map(chatsMapper::map) + _chatsStateFlow.value).toMutableList()

                _chatsStateFlow.value = newList
                _isLoading.value = false
            }
        }
    }

    fun detach() {
        pollingJob?.cancel()
        _hasMatches.value = false
    }

    fun requestNext() {
        CoroutineScope(Dispatchers.Default).launch {
            if (!chatsAreFinished) {
                val nextChatsList = chatsInteractor.getChats(
                    _chatsStateFlow.value.size,
                    CHATS_REQUEST_COUNT
                )

                if (nextChatsList.isNullOrEmpty()) {
                    chatsAreFinished = true
                } else {
                    _chatsStateFlow.value =
                        (_chatsStateFlow.value + nextChatsList.map(chatsMapper::map))
                            .toMutableList()
                }

                _isLoading.value = false
            }
        }
    }

    companion object {
        private const val CHATS_REQUEST_COUNT = 20
    }
}
