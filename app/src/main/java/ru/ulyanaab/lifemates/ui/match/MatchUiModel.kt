package ru.ulyanaab.lifemates.ui.match

import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel

data class MatchUiModel(
    val user: OtherUserUiModel,
    val isSeen: Boolean,
)
