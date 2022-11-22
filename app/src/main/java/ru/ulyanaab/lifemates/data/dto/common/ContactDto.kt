package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json

data class ContactDto(
    @Json(name = "id")
    val id: Long? = null,

    @Json(name = "type")
    val type: ContactTypeDto,

    @Json(name = "value")
    val value: String,
)

enum class ContactTypeDto {
    @Json(name = "Telegram")
    TELEGRAM,

    @Json(name = "Vk")
    VK,

    @Json(name = "Viber")
    VIBER,

    @Json(name = "Whatsapp")
    WHATSAPP,

    @Json(name = "Instagram")
    INSTAGRAM
}
