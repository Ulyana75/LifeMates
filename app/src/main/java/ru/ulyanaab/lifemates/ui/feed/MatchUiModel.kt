package ru.ulyanaab.lifemates.ui.feed

import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel

data class MatchUiModel(
    val title: String,
    val contacts: List<ContactUiModel>,
    val imageUrl: String?,
)
