package ru.ulyanaab.lifemates.ui.feed

import ru.ulyanaab.lifemates.ui.common.model.ContactUiModel

data class MatchUiModel(
    val userId: Long,
    val title: String,
    val contacts: List<ContactUiModel>,
    val imageUrl: String?,
    val chatId: Long,
    val actualUserName: String,
)
