package ru.ulyanaab.lifemates.ui.interests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ulyanaab.lifemates.domain.common.interactor.InterestsInteractor
import ru.ulyanaab.lifemates.domain.common.model.InterestModel
import ru.ulyanaab.lifemates.ui.common.model.RoundedBlockUiModel
import javax.inject.Inject

class InterestsViewModel @Inject constructor(
    private val interestsInteractor: InterestsInteractor,
    private val interestsRepository: InterestsRepository,
) {

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var allInterests: List<RoundedBlockUiModel> = emptyList()

    fun attach() {
        if (allInterests.isEmpty()) {
            fetchInterests()
        }
    }

    fun onDoneButtonClick(chosenInterests: List<RoundedBlockUiModel>) {
        interestsRepository.chosenInterests = chosenInterests.mapNotNull {
            it.id?.let { id -> InterestModel(id, it.text) }
        }
    }

    fun getAllInterests(): List<RoundedBlockUiModel> {
        return allInterests.map { interest ->
            interest.copy(
                isChosen = interestsRepository.chosenInterests
                    .map { it.id }
                    .contains(interest.id)
            )
        }
    }

    fun getChosenInterests(): List<RoundedBlockUiModel> {
        return interestsRepository.chosenInterests.map {
            RoundedBlockUiModel(
                text = it.value,
                isChosen = true,
                id = it.id,
            )
        }
    }

    private fun fetchInterests() {
        val chosenInterests = interestsRepository.chosenInterests

        CoroutineScope(Dispatchers.IO).launch {
            _isLoading.value = true
            val interests = interestsInteractor.getInterests()

            allInterests = interests.map { model ->
                RoundedBlockUiModel(
                    text = model.value,
                    isChosen = chosenInterests.map { it.id }.contains(model.id),
                    id = model.id,
                )
            }

            _isLoading.value = false
        }
    }
}
