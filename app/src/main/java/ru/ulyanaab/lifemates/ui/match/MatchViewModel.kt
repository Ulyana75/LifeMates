package ru.ulyanaab.lifemates.ui.match

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.match.interactor.MatchInteractor
import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel
import javax.inject.Inject

class MatchViewModel @Inject constructor(
    private val matchInteractor: MatchInteractor,
    private val matchMapper: MatchMapper,
) {

    private val _matchesStateFlow: MutableStateFlow<MutableList<MatchUiModel>> =
        MutableStateFlow(mutableListOf())
    val matchesStateFlow: StateFlow<List<MatchUiModel>> = _matchesStateFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _matchesAreFinishedFlow = MutableStateFlow(false)
    val matchesAreFinishedFlow: StateFlow<Boolean> = _matchesAreFinishedFlow.asStateFlow()

    fun attach() {
        if (_matchesStateFlow.value.isEmpty()) {
            requestNext()
        }
    }

    fun detach() {
        _matchesStateFlow.value = mutableListOf()
        _matchesAreFinishedFlow.value = false
    }

    fun requestNext() {
        CoroutineScope(Dispatchers.Default).launch {
            if (!_matchesAreFinishedFlow.value) {
                val nextMatchesList = matchInteractor.getMatches(
                    _matchesStateFlow.value.size,
                    MATCHES_REQUEST_COUNT
                )?.matches

                if (nextMatchesList.isNullOrEmpty()) {
                    _matchesAreFinishedFlow.value = true
                } else {
                    _matchesStateFlow.value.addAll(
                        nextMatchesList.map {
                            MatchUiModel(
                                user = matchMapper.mapToUiModel(it.user),
                                isSeen = it.isSeen
                            )
                        }
                    )
                }

                _isLoading.value = false
            }
        }
    }

    companion object {
        const val MATCHES_REQUEST_COUNT = 5
        const val MATCHES_TILL_END_TO_REQUEST = 2
    }
}
