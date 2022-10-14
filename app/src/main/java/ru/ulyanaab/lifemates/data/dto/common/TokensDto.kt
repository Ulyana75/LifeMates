package ru.ulyanaab.lifemates.data.dto.common

import com.squareup.moshi.Json
import ru.ulyanaab.lifemates.domain.model.TokensModel

data class TokensDto(
    @Json(name = "accessToken")
    val accessToken: String,

    @Json(name = "refreshToken")
    val refreshToken: String,
)

fun TokensDto.toTokensModel(): TokensModel {
    return TokensModel(accessToken, refreshToken)
}

fun TokensModel.toTokensDto(): TokensDto {
    return TokensDto(accessToken, refreshToken)
}
