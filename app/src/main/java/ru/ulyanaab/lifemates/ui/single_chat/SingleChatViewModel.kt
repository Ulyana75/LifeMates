package ru.ulyanaab.lifemates.ui.single_chat

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.chats.interactor.SingleChatInteractor
import ru.ulyanaab.lifemates.domain.report.interactor.ReportsInteractor
import ru.ulyanaab.lifemates.domain.report.model.ReportType
import ru.ulyanaab.lifemates.ui.single_chat.di.WITH_USER_ID
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named

class SingleChatViewModel @Inject constructor(
    @Named(WITH_USER_ID) private val withUserId: Long,
    private val singleChatInteractor: SingleChatInteractor,
    private val messageMapper: MessageMapper,
    private val reportsInteractor: ReportsInteractor,
) {

    private val _messagesStateFlow: MutableStateFlow<MutableList<MessageUiModel>> =
        MutableStateFlow(mutableListOf())
    val messagesStateFlow: StateFlow<List<MessageUiModel>> = _messagesStateFlow.asStateFlow()

    private val _themesStateFlow: MutableStateFlow<List<ThemeUiModel>> =
        MutableStateFlow(emptyList())
    val themesStateFlow: StateFlow<List<ThemeUiModel>> = _themesStateFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _messagesAreFinishedFlow = MutableStateFlow(false)
    val messagesAreFinishedFlow: StateFlow<Boolean> = _messagesAreFinishedFlow.asStateFlow()

    private var messagesPollingJob: Job? = null

    private val stubsForRemoving: MutableList<UUID> = mutableListOf()

    fun attach() {
        messagesPollingJob?.cancel()
        messagesPollingJob = CoroutineScope(Dispatchers.IO).launch {
            fetchThemes()

            singleChatInteractor.messagesFlow.collect { newMessages ->
                val newList =
                    (newMessages.map(messageMapper::map) + _messagesStateFlow.value).toMutableList()

                val removed = mutableListOf<UUID>()
                stubsForRemoving.forEach { id ->
                    newList.removeIf {
                        if (it is MessageUiModel.StubMessageUiModel && it.id == id) {
                            removed.add(id)
                            true
                        } else false
                    }
                }

                _messagesStateFlow.value = newList
                _isLoading.value = false

                stubsForRemoving.removeAll(removed)

            }
        }
    }

    fun detach() {
        messagesPollingJob?.cancel()
        _themesStateFlow.value = emptyList()
    }

    fun sendMessage(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val stub = MessageUiModel.StubMessageUiModel(
                id = UUID.randomUUID(),
                text = text,
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

    fun onReportClick(reportType: ReportType) {
        reportsInteractor.report(withUserId, reportType)
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
                    _messagesStateFlow.value =
                        (_messagesStateFlow.value + nextMessagesList.map(messageMapper::map))
                            .toSet()
                            .toMutableList()
                }

                _isLoading.value = false
            }
        }
    }

    private suspend fun fetchThemes() {
        if (_themesStateFlow.value.isEmpty()) {
            val themes = singleChatInteractor.getThemes(0, 99) ?: return
            _themesStateFlow.value = themes.shuffled().map {
                ThemeUiModel(it.value)
            }.take(THEMES_COUNT)
        }
    }

    companion object {
        private const val MESSAGES_REQUEST_COUNT = 20
        const val MESSAGES_TILL_END_TO_REQUEST = 8
        private const val THEMES_COUNT = 10
    }
}
