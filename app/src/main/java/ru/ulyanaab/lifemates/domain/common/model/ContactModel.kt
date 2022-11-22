package ru.ulyanaab.lifemates.domain.common.model

data class ContactModel(
    val type: ContactType,
    val value: String,
)

enum class ContactType {
    TELEGRAM, VK, VIBER, WHATSAPP, INSTAGRAM
}
