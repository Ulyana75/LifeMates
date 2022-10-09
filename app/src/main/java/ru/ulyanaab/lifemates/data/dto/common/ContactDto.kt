package ru.ulyanaab.lifemates.data.dto.common

data class ContactDto(
    val type: Int,
    val value: String,
)

enum class ContactType(type: Int) {
    TELEGRAM(0), VK(1), VIBER(2), WHATSAPP(3), INSTAGRAM(4)
}
