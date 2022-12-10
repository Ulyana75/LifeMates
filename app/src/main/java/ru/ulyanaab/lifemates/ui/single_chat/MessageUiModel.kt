package ru.ulyanaab.lifemates.ui.single_chat

data class MessageUiModel(
    val text: String,
    val date: String,
    val isFromMe: Boolean,
)
