package ru.ulyanaab.lifemates.ui.single_chat

import java.util.UUID

sealed class MessageUiModel {

    data class SentMessageUiModel(
        val id: Long,
        val text: String,
        val date: String,
        val isFromMe: Boolean,
        val isSeen: Boolean,
    ) : MessageUiModel() {

        override fun hashCode(): Int {
            return id.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as SentMessageUiModel

            if (id != other.id) return false

            return true
        }
    }

    data class StubMessageUiModel(
        val id: UUID,
        val text: String,
        val date: String,
        val isFailed: Boolean = false,
    ) : MessageUiModel() {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as StubMessageUiModel

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }
}
