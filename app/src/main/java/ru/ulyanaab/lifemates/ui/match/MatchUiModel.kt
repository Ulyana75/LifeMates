package ru.ulyanaab.lifemates.ui.match

import ru.ulyanaab.lifemates.ui.common.model.OtherUserUiModel

data class MatchUiModel(
    val id: Long,
    val user: OtherUserUiModel,
    val isSeen: Boolean,
)
