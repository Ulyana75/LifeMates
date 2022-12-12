package ru.ulyanaab.lifemates.ui.common.model

class OtherUserUiModel(
    val id: Long,
    val title: String,
    val subtitle: String,
    val imageUrl: String?,
    val description: String?,
    val contacts: List<ContactUiModel>,
    val interests: List<String>,
    val actualName: String,
)
