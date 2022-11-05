package ru.ulyanaab.lifemates.ui.chats

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.match.interactor.MatchInteractor
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
    private val matchInteractor: MatchInteractor
) {
    
    private val _hasMatches = MutableStateFlow(false)
    val hasMatches: StateFlow<Boolean> = _hasMatches.asStateFlow()

    fun attach() {
        CoroutineScope(Dispatchers.Default).launch {
            val matchesList = matchInteractor.getMatches(0, 1)
            _hasMatches.value = !matchesList.isNullOrEmpty()
        }
    }

    fun detach() {
        _hasMatches.value = false
    }
}
