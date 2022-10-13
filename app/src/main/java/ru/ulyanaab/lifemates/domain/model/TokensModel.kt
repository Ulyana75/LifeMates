package ru.ulyanaab.lifemates.domain.model

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
) {

    companion object {
        val EMPTY = TokensModel("", "")
    }
}
